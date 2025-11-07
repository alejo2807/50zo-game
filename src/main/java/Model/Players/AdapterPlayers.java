package Model.Players;

import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;

import java.util.ArrayList;
import java.util.List;

public abstract class AdapterPlayers extends Thread implements IPlayers {
    protected List<Card> hand = new ArrayList<>();
    protected boolean isPlaying;
    protected int turn;
    protected Object lock;
    protected TurnManager turnManager;
    protected CardPile cardPile;
    protected Deck deck;
    public AdapterPlayers(Deck deck, int myTurn, Object lock, TurnManager turnManager, CardPile cardPile){
        this.deck = deck;
        takeHand(deck);
        this.isPlaying = false;
        this.turn = myTurn;
        this.lock = lock;
        this.turnManager = turnManager;
        this.cardPile = cardPile;
        }

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


    boolean getIsPlaying(){
        return isPlaying;
    }
    void setIsPlaying(boolean isPlaying){
        this.isPlaying = isPlaying;
    }

    public void hasValidCards(){
        int cont = 0;
        for (Card card : hand) {
            if(card.getValue() + cardPile.getValuePile() <= 50){
                cont ++;
            }
        }
        isPlaying = cont != 0;
    }
    public void initializePlayer(){
        isPlaying = true;
    }


}
