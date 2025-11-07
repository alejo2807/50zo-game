package Model.Players;

import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;

public class PlayerHuman extends AdapterPlayers {
    private Thread thread;
    private int indexCard;


    public PlayerHuman(Deck deck, int myTurn, Object lock, TurnManager turnManager, CardPile cardPile) {
        super(deck, myTurn, lock, turnManager, cardPile);
    }
    @Override
    public void run() {
            while(isPlaying){
                synchronized (lock){
                        while(turnManager.getActualTurn() != turn){
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        putCard(indexCard, cardPile);
                        //turnManager.passTurn(); no deberia de venir aca
                        lock.notifyAll();
                }
            }
    }

    public void setIndexCard(int index){
        this.indexCard = index;
    }

}
