package Model.Cards;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CardPile {
    private Queue<Card> CardPile = new LinkedList<>();
    public CardPile() {}
    public void addCard(Card card) {
        CardPile.add(card);
    }
    public List<Card> getBackCards() {
        List<Card> backCards = new ArrayList<>();
        for(int i = 0; i<CardPile.size()-2; i++){
            backCards.add(CardPile.poll());
        }
        return backCards;
    }


}
