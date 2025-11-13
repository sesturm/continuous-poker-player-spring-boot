## Poker Player – Dokumentation

### Überblick
- Spring-Boot-Anwendung, die als Poker-Spieler-Bot fungiert und über einen einzigen HTTP-Endpunkt Entscheidungen zurückliefert.
- Dient als Vorlage für Turniere, bei denen mehrere Bots automatisch gegeneinander antreten.
- Enthält vordefiniertes Datenmodell für Tische, Spieler und Karten sowie eine referenzielle Entscheidungslogik in `Strategy`.

### Architektur
- **`PlayerApplication`**: Bootstrap-Klasse, startet den Spring-Boot-Context und aktiviert Auto-Konfiguration (`src/main/java/de/doubleslash/poker/player/PlayerApplication.java`).
- **`PlayerController`**: REST-Controller, stellt `POST /` bereit, nimmt `Table`-Payload entgegen und ruft `Strategy.decide` auf (`src/main/java/de/doubleslash/poker/player/PlayerController.java`).
- **`Strategy`**: Kapselt die Entscheidungslogik basierend auf übergebenen Karten- und Tischinformationen (`src/main/java/de/doubleslash/poker/player/logic/Strategy.java`).
- **Datenklassen**: Immutable Value Objects (`Card`, `Player`, `Status`, `Rank`, `Suit`, `Table`) zur Deserialisierung der Dealer-Nachrichten (`src/main/java/de/doubleslash/poker/player/data`).
- **Deployment**: JKube-Deployment-Manifest mit Ressourcenvorgaben (`src/main/jkube/deployment.yml`).

### Datenmodell
- `Table`: Enthält Runden-, Blind-, Pot- und Dealer-Informationen sowie Referenzen auf `communityCards` und `players`. Liefert zusätzlich `getOwnPlayer()` für den aktiven Bot.
- `Player`: Beschreibt Name, Status, Stack, aktuellen Einsatz und optionale Hole Cards des jeweiligen Spielers. Kartenliste ist unveränderlich.
- `Card`: Kombination aus `Rank` (Zahlen- und Bildkarten; beinhaltet Wert für Vergleiche) und `Suit` (Herz, Karo, Kreuz, Pik).
- `Status`: Enum zur Darstellung des Spielerstatus (z. B. `active`, `folded`).

### HTTP-Schnittstelle
- **Endpunkt**: `POST /`
- **Content-Type**: `application/json`, Response `text/plain`
- **Payload**: Struktur wie im README.md dokumentiert; entspricht dem `Table`-Datentransferobjekt.
- **Antwort**: Ganzzahlige Bet-Entscheidung (Chips), die der Dealer übernimmt.
- Interaktionen erfolgen synchron; der Bot blockiert controllerseitig bis zur Entscheidung (inkl. künstlicher 5 s Verzögerung in `Strategy.decide`).

### Entscheidungslogik (`Strategy`)
- Fügt Community Cards und eigene Karten zusammen und bewertet die Kombination.
- Prüft in Reihenfolge: Royal Flush, Straight Flush, Flush, Straight, Drilling, Zwei Paare, Ein Paar.
- Gibt je nach Handstärke unterschiedliche Raise-Beträge zurück; fällt andernfalls auf minimalen Fold (`1`) zurück.
- Enthält rudimentäre Hilfsfunktionen (`hasPair`, `hasTwoPair`, etc.), die aktuell vereinfachte/teilweise fehleranfällige Erkennungen nutzen (z. B. Sortierung, Wertvergleiche).
- `Thread.sleep(5000)` simuliert Überlegungszeit; kann für produktive Einsätze verkürzt oder entfernt werden.
- Logging über `java.util.logging.Logger`; derzeit minimale Logausgaben.

### Ausführung & Entwicklung
- Projekt baut mit Maven Wrapper (`./mvnw clean package`) oder lokalem Maven (`mvn clean package`).
- Lokaler Start: `./mvnw spring-boot:run`; Anwendung lauscht auf Port `8080`, sofern nicht über `application.properties` überschrieben.
- Tests: `./mvnw test` führt aktuell nur den Context-Load-Test (`PlayerApplicationTests`) aus.
- Abhängigkeiten und Plugins werden in `pom.xml` verwaltet (Spring Boot Starter Web/Test, Jackson, Apache Commons Lang, JKube).

### Konfiguration & Deployment
- `src/main/resources/application.properties` ist leer; Konfiguration (Port, Logging, externe Services) kann hier ergänzt werden.
- Für Kubernetes-Deployments unterstützt das Projekt Eclipse JKube; Variablen wie `${deployment.name}` werden über Build-Pipeline gesetzt.
- Secrets/Umgebungsvariablen (TOKEN, PORT, TEAMNAME) müssen in der CI/CD-Umgebung bereitgestellt werden, wie in `README.md` beschrieben.

### Erweiterungstipps
- Präzisere Handbewertung (Straight-Logik, Kickers, Evaluierung von Draws).
- Stack- und Pot-abhängige Strategien, Risikoanalyse und Gegner-Modellierung.
- Entfernen/Parametrisieren der künstlichen Verzögerung, um Timeout-Gefahren zu minimieren.
- Zusätzliche Tests (Unit-Tests für Strategiefunktionen, Integrationstest für Controller mit MockMvc).

### Referenzen
- Ursprüngliche Kurzbeschreibung: `README.md`
- Spring Boot Dokumentation: https://docs.spring.io/spring-boot/docs/current/reference/html/
- JKube Dokumentation: https://www.eclipse.dev/jkube/docs/
