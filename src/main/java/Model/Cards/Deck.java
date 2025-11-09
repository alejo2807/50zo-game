package Model.Cards;

import java.util.*;

public class Deck {
    private Deque<Card> deck;
    private final List<String> symbols = List.of("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A");
    private final List<String> suits = List.of("clubs", "diamonds", "hearts", "spades");
    public Deck() {
        deck  = new ArrayDeque<>();
        initializeDeck();

    }

    private void initializeDeck() {
        List<Card> tempList = new ArrayList<>();

        for (String symbol : symbols) {
            for (String suit : suits) {
                String fileName = String.format("/deck/%s_of_%s.png", symbol, suit);
                Card card = new Card(symbol, fileName);
                tempList.add(card);
            }
        }
        Collections.shuffle(tempList);
        for (Card card : tempList) {
            deck.push(card);
        }
    }


    public void addCard(Card card) {
        deck.push(card);
    }
    public Card getCard() {
        return deck.pop();
    }
    public void makeNewDeck(List<Card> cards) {
        Collections.shuffle(cards);
        for (Card card : cards) {
            addCard(card);
        }


    }
    public Deque<Card> getDeck() {return deck;}

    public static void main(String[] args) {
        Deck deck = new Deck();
        Card card = deck.getCard();
        System.out.printf("Card: %s\n", card.getSymbol());
       for(Card c : deck.getDeck()){
           System.out.println(c.getSymbol());
       }
    }

}
