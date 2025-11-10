package Model.Players;

import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;

import java.util.ArrayList;
import java.util.List;

public class PlayerHuman extends AdapterPlayers {
    private Thread thread;
    private int indexCard;
    private boolean turnFinished = false;

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
                // Esperar turno
                while (turnManager.getActualTurn() != turn) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Verificar si tiene cartas válidas
                if (!hasValidCards()) {
                    System.out.println("🚫 Jugador Humano queda fuera del juego");
                    turnManager.passTurn();
                    lock.notifyAll();
                    break;  // Sale del juego
                }

                // Esperar hasta que el jugador humano termine (juegue + tome carta)
                turnFinished = false;
                System.out.println("👤 Es turno del jugador humano. Esperando acción...");

                while (!turnFinished && turnManager.getActualTurn() == turn) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // El turno ya fue pasado por el controlador, solo notificamos
                System.out.println("👤 Jugador humano terminó su turno.");
            }
        }
    }

    public void finishTurn() {
        synchronized (lock) {
            turnFinished = true;
            lock.notifyAll();
        }
    }

    public void setTurnFinished(boolean finished) {
        this.turnFinished = finished;
    }

    public void setIndexCard(int indexCard) {
        this.indexCard = indexCard;
    }
}