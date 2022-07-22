package de.doubleslash.poker.player.logic;

import ch.qos.logback.core.net.SyslogOutputStream;
import de.doubleslash.poker.player.data.Card;
import de.doubleslash.poker.player.data.Rank;
import de.doubleslash.poker.player.data.Table;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Strategy {


    Logger log = Logger.getLogger(Strategy.class.getName());
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
      cards =  cards.stream().sorted((c1, c2) -> c1.getRank().compareTo(c2.getRank())).collect(Collectors.toList());
      if (cards.get(0).getRank() == Rank.ACE && cards.get(1).getRank() == Rank.KING && cards.get(2).getRank() == Rank.QUEEN && cards.get(3).getRank() == Rank.JACK && cards.get(4).getRank() == Rank.TEN) {
          if (cards.get(0).getSuit() == cards.get(1).getSuit() && cards.get(0).getSuit() == cards.get(2).getSuit() && cards.get(0).getSuit() == cards.get(3).getSuit() && cards.get(0).getSuit() == cards.get(4).getSuit()) {
              return true;
          }
      }
      return false;
   }

    private boolean hasStraightFlush(List<Card> cards){
        cards =  cards.stream().sorted((c1, c2) -> c1.getRank().compareTo(c2.getRank())).collect(Collectors.toList());
        if (cards.get(1).getRank().value ==cards.get(0).getRank().value-1 && cards.get(2).getRank().value == cards.get(0).getRank().value-2 && cards.get(3).getRank().value ==cards.get(0).getRank().value-3 && cards.get(4).getRank().value == cards.get(0).getRank().value-4) {
            if (cards.get(0).getSuit() == cards.get(1).getSuit() && cards.get(0).getSuit() == cards.get(2).getSuit() && cards.get(0).getSuit() == cards.get(3).getSuit() && cards.get(0).getSuit() == cards.get(4).getSuit()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasFush(List<Card> cards){
        cards =  cards.stream().sorted((c1, c2) -> c1.getRank().compareTo(c2.getRank())).collect(Collectors.toList());
        if (cards.get(0).getSuit() == cards.get(1).getSuit() && cards.get(0).getSuit() == cards.get(2).getSuit() && cards.get(0).getSuit() == cards.get(3).getSuit() && cards.get(0).getSuit() == cards.get(4).getSuit()) {
            return true;
        }
        return false;
    }

  public int decide(Table table) throws InterruptedException {
       Thread.sleep(5000);
//      log.info(table);
      List<Card> cards = table.getCommunityCards();
     cards.addAll(table.getOwnPlayer().getCards());

     if(cards.size()<=5){
         log.info("not Enough information to decide");
         return table.getMinimumRaise();
     }

      if (hasRoyalFush(cards)) {
          log.info("AllIn");
         return table.getOwnPlayer().getStack();
      }
      if (hasStraightFlush(cards)) {
          return raise(table.getMinimumRaise() + 60,table);
      }
      if (hasFush(cards)) {
          return raise(table.getMinimumRaise() + 50,table);
      }
      if (hasStraight(cards)) {
         return raise(table.getMinimumRaise() + 40,table);
      }
      if (hasTriple(cards)) {
          return raise(table.getMinimumRaise() + 30,table);
      }
      if (hasTwoPair(cards)) {
          return raise(table.getMinimumRaise() + 20,table);
      }
      if (hasPair(cards)) {
          return raise(table.getMinimumRaise() + 10,table);
      }
      log.println("Fold");
      return 1;
   }

   private int raise(int target,Table table){
       if(target>table.getOwnPlayer().getStack()){
           log.println("AllIn because of no money");
           return table.getOwnPlayer().getStack();
       }
       log.println("Raise to " + target);
       return target;

   }

}
