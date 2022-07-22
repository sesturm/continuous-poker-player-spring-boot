package de.doubleslash.poker.player.logic;

import de.doubleslash.poker.player.data.Card;
import de.doubleslash.poker.player.data.Table;

import java.util.List;

public class Strategy {

   private boolean hasPair (List<Card> card){
      for (int i = 0; i < card.size() - 1; i++) {
         if (card.get(i).getRank() == card.get(i + 1).getRank()) {
            return true;
         }
      }
      return false;
   }

   private boolean hasFlush(List<Card> cards){
      for (int i = 0; i < cards.size() - 1; i++) {
         if (cards.get(i).getSuit() != cards.get(i + 1).getSuit()) {
            return false;
         }
      }
      return true;
   }

   public int decide(final Table table) {

      System.out.println(table);
      return 1;
   }

}
