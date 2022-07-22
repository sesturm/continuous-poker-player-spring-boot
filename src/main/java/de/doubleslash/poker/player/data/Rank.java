package de.doubleslash.poker.player.data;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Rank {
   ACE("A",12),
   KING("K",11),
   QUEEN("Q",10),
   JACK("J",9),
   TEN("10",8),
   NINE("9",7),
   EIGHT("8",7),
   SEVEN("7",6),
   SIX("6",5),
   FIVE("5",4),
   FOUR("4",3),
   THREE("3",2),
   TWO("2",1);

   private final String token;
   public final int value;


   Rank(final String token,int value) {
      this.token = token;
      this.value = value;
   }

   public String getToken() {
      return token;
   }

   @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
   public static Rank forToken(final String token) {
      return Arrays.stream(Rank.values()).filter(r -> r.getToken().equals(token)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Token not allowed: " + token));
   }
}
