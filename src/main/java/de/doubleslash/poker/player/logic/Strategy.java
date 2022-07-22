package de.doubleslash.poker.player.logic;

import de.doubleslash.poker.player.data.Card;
import de.doubleslash.poker.player.data.Rank;
import de.doubleslash.poker.player.data.Table;

import java.util.List;

public class Strategy {

   private boolean hasPair (List<Card> cards){
      for (int i = 0; i < cards.size() - 1; i++) {
          return cards.stream().filter(c -> c.getRank() == cards.get(i).getRank()).count()==2;
      }
      return false;
   }

   private boolean hasStraight(List<Card> card){
      for (int i = 0; i < card.size() - 3; i++) {
         if (card.get(i).getRank() == card.get(i + 1).getRank() && card.get(i).getRank() == card.get(i + 2).getRank() && card.get(i).getRank() == card.get(i + 3).getRank()) {
            return true;
         }
      }
      return false;
   }

   private boolean hasTriple(List<Card> cards){
       for (int i = 0; i < cards.size() - 1; i++) {
           return cards.stream().filter(c -> c.getRank() == cards.get(i).getRank()).count()==3;
       }
      return false;
   }

   private boolean hasTwoPair(List<Card> card){
       Rank foundRank = null;
      for (int i = 0; i < card.size() - 1; i++) {
          if (card.get(i).getRank() == foundRank)
              continue;
         if (card.get(i).getRank() == card.get(i + 1).getRank()) {
            if(foundRank!=null)
                return true;
            foundRank = card.get(i).getRank();
         }
      }
      return false;
   }

   private boolean hasRoyalFush(List<Card> cards){
       cards.stream().sorted((c1, c2) -> c1.getRank().compareTo(c2.getRank()));
      if (cards.get(0).getRank() == Rank.ACE && cards.get(1).getRank() == Rank.KING && cards.get(2).getRank() == Rank.QUEEN && cards.get(3).getRank() == Rank.JACK && cards.get(4).getRank() == Rank.TEN) {
         return true;
      }
      return false;
   }
  public int decide(Table table){
      List<Card> cards = table.getCommunityCards();
     cards.addAll(table.getOwnPlayer().getCards());
      if (hasRoyalFush(cards)) {
         return table.getOwnPlayer().getStack();
      }
      if (hasStraight(cards)) {
         return 4;
      }
      if (hasTriple(cards)) {
         return 3;
      }
      if (hasTwoPair(cards)) {
         return 2;
      }
      if (hasPair(cards)) {
         return 1;
      }
      return 0;
   }

}
