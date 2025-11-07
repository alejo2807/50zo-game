package Model.Cards;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CardPile {
    private Queue<Card> CardPile = new LinkedList<>();
    private int valuePile;
    public CardPile() {}
    public void addCard(Card card)
    {
        CardPile.add(card);
        valuePile += card.getValue();
        if(card.getValue() == 10 && valuePile > 50){
            valuePile += -9;
        }

    }
    public List<Card> getBackCards() {
        List<Card> backCards = new ArrayList<>();
        for(int i = 0; i<CardPile.size()-2; i++){
            backCards.add(CardPile.poll());
        }
        return backCards;
    }
    public  int getValuePile(){
        return  valuePile;
    }


}
