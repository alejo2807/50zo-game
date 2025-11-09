package Model.Players;

import Model.Cards.CardPile;
import Model.Cards.Deck;
import Model.Cards.Card;
import Controller.GameWindowController;
import javafx.application.Platform;

public class PlayerGPU extends AdapterPlayers {

    private GameWindowController controller;

    public PlayerGPU(Deck deck, int myTurn, Object lock, TurnManager turnManager, CardPile cardPile, GameWindowController controller) {
        super(deck, myTurn, lock, turnManager, cardPile);
        this.controller = controller;
    }

    @Override
    public void run() {
        hasValidCards();
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
                    System.out.println("GPU " + turn + " queda fuera: sin jugadas válidas.");
                    turnManager.passTurn();
                    lock.notifyAll();
                    continue;
                }
                try {
                    // Simula tiempo de "pensar"
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // GPU juega la primera carta de su mano
                if (!hand.isEmpty()) {
                    Card cardPlayed = hand.get(0);
                    putCard(0, cardPile);

                    // Actualiza la pila visualmente
                    Platform.runLater(() -> controller.updatePileImage(cardPlayed));
                }

                try {
                    // Pequeña pausa antes de tomar carta
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Toma nueva carta si el mazo no está vacío
                if (!deck.getDeck().isEmpty()) {
                    takeCard(deck.getCard());
                }

                // Actualiza interfaz (cartas de GPU con reverso)
                Platform.runLater(() -> controller.printCardsGPU());

                // Pasa turno al siguiente jugador
                turnManager.passTurn();
                lock.notifyAll();
            }
        }
    }
}
