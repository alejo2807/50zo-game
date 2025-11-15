package Model.Players;

import Controller.GameWindowController;
import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;
import View.Eliminate;
import View.GameWindow;
import View.Messages;
import javafx.application.Platform;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Represents a human player in the card game.
 *
 * This class extends {@link AdapterPlayers} and defines the interaction logic
 * for a user-controlled player. Unlike {@link PlayerGPU}, this class relies on
 * user input to play cards and end turns.
 *
 * @author Juan-David-Brandon
 * @since 2025
 * @version 1.0
 */
public class PlayerHuman extends AdapterPlayers {

    /** The thread managing the player's actions (if used externally). */
    private Thread thread;

    /** Index of the selected card in the player's hand. */
    private int indexCard;

    /** Indicates whether the player has finished their turn. */
    private boolean turnFinished = false;

    /** Reference to the game controller responsible for managing UI updates. */
    private GameWindowController controller;

    /** Flag indicating if the player has already won. */
    private boolean isWin = false;

    /** Instance used to display UI messages for win/lose or turn notifications. */
    private Messages messages;

    /**
     * Constructs a new human player.
     *
     * @param deck        the main deck used to draw cards
     * @param myTurn      the player's assigned turn number
     * @param lock        shared synchronization object used for turn control
     * @param turnManager the manager controlling player turns
     * @param cardPile    the shared pile where cards are played
     */
    public PlayerHuman(Deck deck, int myTurn, Object lock, TurnManager turnManager, CardPile cardPile, String playerType) {
        super(deck, myTurn, lock, turnManager, cardPile, playerType);
        takeHand();
        controller = new GameWindowController();
    }

    /**
     * Alternative constructor for standalone initialization (used for testing or setup).
     *
     * @param deck the deck used to initialize the player
     */
    public PlayerHuman(Deck deck) {
        super(deck);
    }

    /**
     * Main execution loop of the human player.
     *
     * This method runs in a separate thread, waiting for the player's turn to start.
     * During its turn, it allows the user to play a card manually via the interface.
     * The thread remains active while {@code isPlaying} is true.
     */
    @Override
    public void run() {
        System.out.println("👤 Human player thread started");

        while (isPlaying) {
            synchronized (lock) {

                // VERIFICAR SI ES EL ÚNICO JUGADOR RESTANTE (GANADOR)
                if (turnManager.getTotalTurns().size() == 1) {
                    System.out.println("🏆 Human player is the only player left - WINNER!");
                    isPlaying = false;
                    lock.notifyAll();
                    break;
                }

                // Wait until it's the human player's turn
                while (isPlaying && turnManager.getActualTurn() != turn) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        System.out.println("👤 Human player interrupted");
                        return;
                    }
                }

                // VERIFICAR DE NUEVO después de despertar
                if (turnManager.getTotalTurns().size() == 1) {
                    System.out.println("🏆 Human player is the winner after waking up!");
                    isPlaying = false;
                    lock.notifyAll();
                    break;
                }

                // Verificar si sigue jugando después de despertar
                if (!isPlaying) {
                    System.out.println("👤 Human player no longer playing, exiting cleanly");
                    lock.notifyAll(); // ASEGURAR QUE OTROS THREADS DESPIERTEN
                    break;
                }

                // If no valid cards are available, eliminate the player
                if (!hasValidCards()) {
                    System.out.println("❌ Human player eliminated (no valid cards)");
                    System.out.println("📊 Players before elimination: " + turnManager.getTotalTurns());

                    // MOSTRAR VENTANA DE ELIMINACIÓN EN EL THREAD DE JAVAFX
                    Platform.runLater(() -> {
                        try {
                            Eliminate.getInstance().show();
                        } catch (IOException e) {
                            System.err.println("❌ Error showing elimination window: " + e.getMessage());
                            e.printStackTrace();
                        }
                    });

                    returnCardsToDecK();

                    // Actualizar la UI
                    Platform.runLater(() -> {
                        controller.notifyHumanEliminated();
                    });

                    // ORDEN CRÍTICO: primero eliminar, luego pasar turno
                    turnManager.setLasTurnEliminate(turn);
                    System.out.println("📊 Players after elimination: " + turnManager.getTotalTurns());

                    isPlaying = false;

                    // Pasar el turno al siguiente jugador
                    turnManager.passTurn();
                    System.out.println("➡️ Next turn after human elimination: " + turnManager.getActualTurn());

                    // DESPERTAR A TODOS LOS THREADS (CRÍTICO)
                    System.out.println("🔔 Human player notifying ALL threads to continue");
                    lock.notifyAll();

                    // Pequeña pausa para asegurar que otros threads se despierten
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;
                }


                // Wait for the human player to play and draw a card
                turnFinished = false;
                System.out.println("👤 It's the human player's turn. Waiting for action...");

                // Wait until the player finishes their turn
                while (!turnFinished && turnManager.getActualTurn() == turn && isPlaying) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        System.out.println("👤 Human player interrupted while waiting for action");
                        return;
                    }
                }

                System.out.println("👤 Human player finished their turn.");
            }
        }

        System.out.println("💤 Human player thread finished");
    }

    /**
     * Devuelve las cartas del jugador humano al mazo cuando es eliminado
     */
    public void returnCardsToDecK() {
        if (hand.isEmpty()) {
            System.out.println("⚠ Human player has no cards to return");
            return;
        }

        int returnedCards = hand.size();
        System.out.println("📤 Human player returns " + returnedCards + " cards to the deck");

        // Add all cards back to the deck
        for (Card card : hand) {
            deck.getDeck().add(card);
        }

        // Clear player's hand
        hand.clear();

        // Shuffle the deck
        deck.shuffle();

        System.out.println("🔀 Deck shuffled. Available cards: " + deck.getDeck().size());
    }

    /**
     * Signals that the player has finished their turn.
     *
     * This method is typically called by the controller once the player
     * has played a card and drawn a new one.
     */
    public void finishTurn() {
        synchronized (lock) {
            turnFinished = true;
            lock.notifyAll();
        }
    }

    /**
     * Sets whether the player has finished their turn.
     *
     * @param finished {@code true} if the player has completed their turn, {@code false} otherwise
     */
    public void setTurnFinished(boolean finished) {
        this.turnFinished = finished;
    }

    /**
     * Sets the index of the card chosen by the player to play.
     *
     * @param indexCard the index of the selected card in the player's hand
     */
    public void setIndexCard(int indexCard) {
        this.indexCard = indexCard;
    }
}