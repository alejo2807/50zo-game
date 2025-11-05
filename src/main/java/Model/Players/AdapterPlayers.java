package Model.Players;

import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;

import java.util.ArrayList;
import java.util.List;

public abstract class AdapterPlayers implements IPlayers {
    protected List<Card> hand = new ArrayList<>();
    protected boolean isPlaying;
    @Override
    public void takeCard(Card card) {
        hand.add(card);
    }
    @Override
    public void putCard(int indexCard, CardPile cardPile) {
        Card card = hand.get(indexCard);
        hand.remove(indexCard);
        cardPile.addCard(card);

    }
    @Override
    public void takeHand(Deck deck){
        hand.clear();
        for (int i = 0; i <4 ; i++) {
            hand.add(deck.getCard());
        }
    }

    public AdapterPlayers(Deck deck){
        takeHand(deck);
        this.isPlaying = false;
    }
    boolean getIsPlaying(){
        return isPlaying;
    }
    void setIsPlaying(boolean isPlaying){
        this.isPlaying = isPlaying;
    }
}
