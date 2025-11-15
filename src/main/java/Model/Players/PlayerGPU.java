package Model.Players;

import Model.Cards.CardPile;
import Model.Cards.Deck;
import Model.Cards.Card;
import Model.Exceptions.InvalidCardException;
import Controller.GameWindowController;
import javafx.application.Platform;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents an AI-controlled player (GPU) in the card game.
 *
 * <p>This class extends {@link AdapterPlayers} and defines the automated
 * decision-making logic of a non-human player. GPU players perform actions
 * such as validating cards, playing valid moves, drawing cards, and updating
 * the game interface without user input.</p>
 *
 * <p>GPU players run within their own threads and synchronize their actions
 * with other players using a shared lock mechanism. All UI interactions are
 * executed on the JavaFX application thread using
 * {@link Platform#runLater(Runnable)}.</p>
 *
 * @author Juan-David-Brandon
 * @since 2025
 */
public class PlayerGPU extends AdapterPlayers {

    /** Controller responsible for updating the game UI. */
    private final GameWindowController controller;

    /**
     * Constructs a new GPU player.
     *
     * @param deck         the main deck from which cards are drawn
     * @param myTurn       the turn number assigned to this player
     * @param lock         shared lock used for thread synchronization
     * @param turnManager  manager that controls turn order
     * @param cardPile     the pile where this player places cards
     * @param controller   the controller used to update the interface
     * @param playerType   label identifying the player type
     */
    public PlayerGPU(Deck deck, int myTurn, Object lock, TurnManager turnManager,
                     CardPile cardPile, GameWindowController controller, String playerType) {
        super(deck, myTurn, lock, turnManager, cardPile, playerType);
        this.controller = controller;
    }

    /**
     * Main execution method for the GPU player.
     *
     * <p>The thread runs continuously while {@code isPlaying} is true.
     * During each cycle, the GPU waits for its turn, attempts to play a valid
     * card, draws a new one, and then passes the turn to the next player.</p>
     *
     * <p>All updates to the graphical interface are done via the JavaFX
     * application thread.</p>
     */
    @Override
    public void run() {

        while (isPlaying) {
            synchronized (lock) {

                // Check if this GPU is the last remaining player
                if (turnManager.getTotalTurns().size() == 1) {
                    isPlaying = false;
                    lock.notifyAll();
                    break;
                }

                // Wait until this GPU's turn starts
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

                // Stop if the GPU is no longer in the game
                if (!isPlaying) {
                    break;
                }

                // Eliminate GPU if it has no valid cards
                if (!hasValidCards()) {

                    returnCardsToDecK();
                    Platform.runLater(controller::printCardsGPU);

                    turnManager.setLasTurnEliminate(turn);
                    isPlaying = false;

                    turnManager.passTurn();
                    controller.notifyBotTurnChange();
                    lock.notifyAll();
                    break;
                }

                // Simulate decision-making delay
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(2000, 4001));
                } catch (InterruptedException e) {
                    return;
                }

                // Attempt to play a valid card
                if (!hand.isEmpty()) {
                    boolean cardPlayed = false;

                    for (int i = 0; i < hand.size() && !cardPlayed; i++) {
                        try {
                            Card cardToPlay = hand.get(i);
                            putCard(i, cardPile);

                            final Card finalCard = cardToPlay;
                            Platform.runLater(() -> controller.updatePileImage(finalCard));

                            cardPlayed = true;

                        } catch (InvalidCardException e) {
                            // Try next card
                        }
                    }
                }

                // Delay before drawing a card
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2000));
                } catch (InterruptedException e) {
                    return;
                }

                // Draw a new card
                Card newCard = deck.getCard();
                if (newCard != null) {
                    takeCard(newCard);
                }

                // Update GPU hand on UI
                Platform.runLater(controller::printCardsGPU);

                // End of turn
                turnManager.passTurn();
                controller.notifyBotTurnChange();
                lock.notifyAll();
            }
        }

        // Update UI when thread ends
        Platform.runLater(controller::printCardsGPU);
    }

    /**
     * Returns all cards from this player's hand back into the deck.
     * <p>
     * This is used when the GPU is eliminated. The deck is reshuffled
     * afterward to maintain random distribution of cards.
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
     * Sets the turn number assigned to this player.
     *
     * @param turn the new turn number
     */
    public void setTurn(int turn) {
        this.turn = turn;
    }
}
