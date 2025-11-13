package Model.Players;

import Controller.GameWindowController;
import Model.Cards.CardPile;
import Model.Cards.Deck;
import View.GameWindow;
import View.Messages;
import javafx.application.Platform;
import java.io.IOException;

/**
 * Represents a human player in the card game.
 * 
 * This class extends {@link AdapterPlayers} and defines the interaction logic
 * for a user-controlled player. Unlike {@link PlayerGPU}, this class relies on
 * user input to play cards and end turns.
 * 
 *
 * 
 * The human player:
 * 
 *   Waits for its turn before performing actions.
 *   Interacts with the game interface through {@link GameWindowController}.
 *   Receives visual messages through the {@link Messages} view class.
 *   Handles win/lose events using asynchronous UI operations.
 * 
 * 
 *
 * @author Juan-David-Brandon
 * @since 2025
 * @version 1.0
 */
public class PlayerHuman extends AdapterPlayers {

    /** The thread managing the player’s actions (if used externally). */
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
    public PlayerHuman(Deck deck, int myTurn, Object lock, TurnManager turnManager, CardPile cardPile) {
        super(deck, myTurn, lock, turnManager, cardPile);
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
     * This method runs in a separate thread, waiting for the player’s turn to start.
     * During its turn, it allows the user to play a card manually via the interface.
     * The thread remains active while {@code isPlaying} is true.
     * 
     */
    @Override
    public void run() {
        while (isPlaying) {
            synchronized (lock) {

                // Wait until it’s the human player’s turn
                while (turnManager.getActualTurn() != turn) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // If no valid cards are available, eliminate the player
                if (!hasValidCards()) {
                    Platform.runLater(() -> {
                        new Thread(() -> {
                            try {
                                Thread.sleep(300); // Small delay before showing message
                                Platform.runLater(() -> {
                                    try {
                                        new Messages(2).show();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            } catch (InterruptedException ignored) {}
                        }).start();
                    });
                    System.out.println(" Human player eliminated (no valid cards)");
                    isPlaying = false;
                    lock.notifyAll();
                    break;
                }

                // Wait for the human player to play and draw a card
                turnFinished = false;
                System.out.println(" It's the human player's turn. Waiting for action...");

                if (turnManager.getActualTurn() == turn && !isWin) {
                    Platform.runLater(() -> {
                        try {
                            GameWindow window = GameWindow.getInstance(3);
                            window.getScene().getRoot().setMouseTransparent(true);
                            messages = new Messages(3);
                            messages.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }

                // Wait until the player finishes their turn
                while (!turnFinished && turnManager.getActualTurn() == turn) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println(" Human player finished their turn.");

                // Check if the human player is the last remaining participant
                if (turnManager.getTotalTurns().size() == 1 && isPlaying) {
                    Platform.runLater(() -> {
                        new Thread(() -> {
                            try {
                                Thread.sleep(300);
                                Platform.runLater(() -> {
                                    try {
                                        messages.close();
                                        new Messages(1).show();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            } catch (InterruptedException ignored) {}
                        }).start();
                    });
                }
            }
        }
    }

    /**
     * Signals that the player has finished their turn.
     * 
     * This method is typically called by the controller once the player
     * has played a card and drawn a new one.
     * 
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

    /**
     * Displays a win or lose message depending on the game outcome.
     * 
     * This method closes the game window and shows the appropriate message asynchronously.
     * 
     *
     * @param i the message type: {@code 1} for win, {@code 2} for loss
     */
    private void showWinLoseMessage(int i) {
        Platform.runLater(() -> {
            try {
                GameWindow.getInstance(3).close();
                new Thread(() -> {
                    try {
                        Thread.sleep(300);
                        Platform.runLater(() -> {
                            try {
                                new Messages(i).show();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } catch (InterruptedException ignored) {}
                }).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
