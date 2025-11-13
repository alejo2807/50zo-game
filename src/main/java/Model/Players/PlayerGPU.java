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
 * <p>
 * This class extends {@link AdapterPlayers} and defines the behavior
 * of a non-human player that plays automatically during its turn.
 * It runs as a separate thread, synchronizing its actions with other
 * players through a shared lock mechanism.
 * </p>
 *
 * <p>
 * The GPU player is capable of:
 * <ul>
 *   <li>Waiting for its turn before taking any action.</li>
 *   <li>Validating and playing cards according to game rules.</li>
 *   <li>Drawing new cards when necessary.</li>
 *   <li>Returning all its cards to the deck when eliminated.</li>
 *   <li>Updating the game interface asynchronously using {@link Platform#runLater(Runnable)}.</li>
 * </ul>
 * </p>
 *
 * @author Juan-David-Brandon
 * @since 2025
 */
public class PlayerGPU extends AdapterPlayers {

    /** Reference to the game window controller for UI updates. */
    private final GameWindowController controller;

    /**
     * Constructs a new GPU player.
     *
     * @param deck         the main deck used to draw or return cards
     * @param myTurn       the player's assigned turn number
     * @param lock         a shared synchronization object to manage turns safely
     * @param turnManager  the object responsible for controlling the game turns
     * @param cardPile     the shared pile of cards where players place their cards
     * @param controller   the controller responsible for UI updates
     */
    public PlayerGPU(Deck deck, int myTurn, Object lock, TurnManager turnManager, CardPile cardPile, GameWindowController controller) {
        super(deck, myTurn, lock, turnManager, cardPile);
        this.controller = controller;
    }

    /**
     * Main execution method of the GPU player.
     * <p>
     * The method runs continuously in a separate thread while {@code isPlaying} is true.
     * It waits for its turn, plays a valid card, draws a new one, and passes control
     * to the next player. The method ensures thread-safety using the {@code lock} object
     * and updates the interface on the JavaFX application thread.
     * </p>
     */
    @Override
    public void run() {
        System.out.println("🤖 GPU " + turn + " thread started");

        while (isPlaying) {
            synchronized (lock) {
                System.out.println(turnManager.getTotalTurns());
                System.out.println("CURRENT TURN  " + turnManager.getActualTurn());

                // Wait until it's this player's turn
                while (isPlaying && turnManager.getActualTurn() != turn) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        System.err.println("❌ GPU " + turn + " interrupted while waiting for turn");
                        return;
                    }
                }

                // If player has no valid cards, return them to the deck and leave the game
                if (!hasValidCards()) {
                    System.out.println("🚫 GPU " + turn + " eliminated (no valid cards)");

                    returnCardsToDecK();
                    Platform.runLater(() -> controller.printCardsGPU());

                    turnManager.setLasTurnEliminate(turn);
                    this.isPlaying = false;
                    turnManager.passTurn();
                    controller.notifyBotTurnChange();
                    lock.notifyAll();
                    break;
                }

                System.out.println("🤖 GPU " + turn + " starts its turn");

                // Simulate thinking time before playing
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(2000, 4001));
                } catch (InterruptedException e) {
                    System.err.println("❌ GPU " + turn + " interrupted while thinking");
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

                            System.out.println("🤖 GPU " + turn + " played: " + cardToPlay.getSymbol() +
                                    " | New pile value: " + cardPile.getValuePile());
                            cardPlayed = true;

                        } catch (InvalidCardException e) {
                            System.out.println("🤖 GPU " + turn + " attempted invalid card, trying next...");
                        }
                    }

                    if (!cardPlayed) {
                        System.out.println("⚠️ GPU " + turn + " couldn't play any card (unexpected state)");
                    }
                } else {
                    System.out.println("⚠️ GPU " + turn + " has no cards to play");
                }

                // Small delay before drawing a new card
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2000));
                } catch (InterruptedException e) {
                    System.err.println("❌ GPU " + turn + " interrupted before drawing a card");
                    return;
                }

                // Draw a new card from the deck
                Card newCard = deck.getCard();
                if (newCard != null) {
                    takeCard(newCard);
                    System.out.println("🤖 GPU " + turn + " drew: " + newCard.getSymbol());
                } else {
                    System.out.println("⚠️ GPU " + turn + ": deck empty, cannot draw card");
                }

                // Update interface after drawing
                Platform.runLater(() -> controller.printCardsGPU());

                // End of turn
                System.out.println("🤖 GPU " + turn + " ends its turn");
                turnManager.passTurn();
                controller.notifyBotTurnChange();

                lock.notifyAll();
            }
        }

        Platform.runLater(() -> controller.printCardsGPU());
        System.out.println("💤 GPU " + turn + " thread finished");
    }

    /**
     * Returns all cards from this player's hand back into the main deck.
     * <p>
     * This method is typically used when a player is eliminated or leaves the game.
     * It ensures that the cards are reintroduced into the deck and shuffled.
     * </p>
     */
    public void returnCardsToDecK() {
        if (hand.isEmpty()) {
            System.out.println("⚠️ Player " + turn + " has no cards to return");
            return;
        }

        int returnedCards = hand.size();
        System.out.println("🔄 Player " + turn + " returns " + returnedCards + " cards to the deck");

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
     * Sets the turn number for this player.
     *
     * @param turn the new turn number assigned to the player
     */
    public void setTurn(int turn) {
        this.turn = turn;
    }
}
