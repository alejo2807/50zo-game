package Model.Players;

import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;

import java.util.ArrayList;
import java.util.List;

public class PlayerHuman extends AdapterPlayers {
    private Thread thread;
    private int indexCard;


    public PlayerHuman(Deck deck, int myTurn, Object lock, TurnManager turnManager, CardPile cardPile) {
        super(deck, myTurn, lock, turnManager, cardPile);
        takeHand();
    }
    public PlayerHuman(Deck deck) {
        super(deck);
    }
    @Override
    public void run() {

        while (isPlaying) {
            synchronized (lock) {
                while (turnManager.getActualTurn() != turn) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                hasValidCards();
                if (!isPlaying) {
                    System.out.println("Jugador " + turn + " ya no puede jugar.");
                    turnManager.passTurn();
                    lock.notifyAll();
                    break; // o continue según tu lógica
                }
                // 🕐 Esperar hasta que el controlador le notifique que terminó su jugada
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setIndexCard(int indexCard) {
        this.indexCard = indexCard;
    }



}
