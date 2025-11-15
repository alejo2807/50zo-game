package Model.Players;

import Controller.GameWindowController;
import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;
import View.Eliminate;
import View.ShowWiner;
import javafx.application.Platform;
import java.io.IOException;

/**
 * Represents a human-controlled player in the card game.
 * <p>
 * This class extends {@link AdapterPlayers} and provides the interaction logic
 * for real users through the graphical interface. Unlike {@link PlayerGPU},
 * the human player waits for input from the UI before performing actions.
 * </p>
 *
 * @author Juan-David-Brandon
 * @since 2025
 * @version 1.0
 */
public class PlayerHuman extends AdapterPlayers {

    /** The thread executing the player's main loop. */
    private Thread thread;

    /** Index of the card selected by the user during their turn. */
    private int indexCard;

    /** Indicates whether the player has completed their turn. */
    private boolean turnFinished = false;

    /** Reference to the game controller responsible for UI updates. */
    private GameWindowController controller;

    /** Indicates whether the human player has already won the match. */
    private boolean isWin = false;

    /** Window used to display turn, win, or elimination messages. */
    private ShowWiner showWiner;

    /**
     * Constructs a new human player.
     *
     * @param deck        the main deck used to draw cards
     * @param myTurn      the player's assigned turn number
     * @param lock        shared synchronization object used for turn control
     * @param turnManager the manager controlling turn order
     * @param cardPile    the shared pile where cards are placed
     * @param playerType  identifier for the player type
     */
    public PlayerHuman(Deck deck, int myTurn, Object lock, TurnManager turnManager, CardPile cardPile, String playerType) {
        super(deck, myTurn, lock, turnManager, cardPile, playerType);
        takeHand();
        controller = new GameWindowController();
    }

    /**
     * Alternative constructor used for standalone initialization or testing.
     *
     * @param deck deck used to initialize the player's hand
     */
    public PlayerHuman(Deck deck) {
        super(deck);
    }

    /**
     * Main execution loop executed in the player's thread.
     * <p>
     * The human player waits until their turn begins. When it starts, the thread
     * suspends until user interaction completes the turn. If the player is
     * eliminated or the game ends, the loop stops.
     * </p>
     */
    @Override
    public void run() {

        while (isPlaying) {
            synchronized (lock) {

                // Check if player is the last remaining (winner)
                if (turnManager.getTotalTurns().size() == 1) {
                    isPlaying = false;
                    lock.notifyAll();
                    break;
                }

                // Wait until it's this player's turn
                while (isPlaying && turnManager.getActualTurn() != turn) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }

                // Check again after waking up
                if (turnManager.getTotalTurns().size() == 1) {
                    isPlaying = false;
                    lock.notifyAll();
                    break;
                }

                // If the player is no longer active, exit safely
                if (!isPlaying) {
                    lock.notifyAll();
                    break;
                }

                // Eliminate the player if they have no valid cards
                if (!hasValidCards()) {

                    Platform.runLater(() -> {
                        try {
                            Eliminate.getInstance().show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    returnCardsToDecK();

                    Platform.runLater(() -> {
                        controller.notifyHumanEliminated();
                    });

                    turnManager.setLasTurnEliminate(turn);

                    isPlaying = false;

                    turnManager.passTurn();

                    lock.notifyAll();

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // Ignore
                    }

                    break;
                }

                // Wait for the human player to perform an action
                turnFinished = false;

                while (!turnFinished && turnManager.getActualTurn() == turn && isPlaying) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        }
    }

    /**
     * Returns all cards from the human player back to the deck when eliminated.
     * <p>
     * The deck is automatically reshuffled afterward.
     * </p>
     */
    public void returnCardsToDecK() {
        if (hand.isEmpty()) {
            return;
        }

        for (Card card : hand) {
            deck.getDeck().add(card);
        }

        hand.clear();
        deck.shuffle();
    }

    /**
     * Marks the player's turn as finished.
     * <p>
     * This method is typically triggered from the UI once the player selects
     * a card and completes their move.
     * </p>
     */
    public void finishTurn() {
        synchronized (lock) {
            turnFinished = true;
            lock.notifyAll();
        }
    }

    /**
     * Sets whether the player's turn is finished.
     *
     * @param finished {@code true} if the turn is complete, {@code false} otherwise
     */
    public void setTurnFinished(boolean finished) {
        this.turnFinished = finished;
    }

    /**
     * Sets the index of the card selected by the human player.
     *
     * @param indexCard index of the chosen card in the player's hand
     */
    public void setIndexCard(int indexCard) {
        this.indexCard = indexCard;
    }
}
