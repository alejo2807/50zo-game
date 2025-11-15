package Model.Cards;

import java.util.Collections;
import java.util.List;

/**
 * Thread responsible for automatically recharging the deck when it runs empty.
 * This class monitors the main deck and, when it becomes empty, transfers cards
 * from the card pile (excluding the top card) back into the deck to ensure
 * continuous gameplay without interruption.
 * 
 * The recharge process works as follows:
 * 
 *   Continuously monitors the deck every 500 milliseconds
 *   When the deck is empty, retrieves all cards from the pile except the top card
 *   Shuffles the retrieved cards randomly
 *   Adds the shuffled cards back to the deck
 * 
 * 
 * 
 * This thread runs continuously in the background throughout the game,
 * using synchronization to prevent concurrent modification issues with the deck.
 * 
 *
 * @author Juan-David-Brandon
 * @version 1.0
 * @since 2025
 */
public class RechargeDeck extends Thread {
    /**
     * The main deck that needs to be recharged when empty.
     */
    private final Deck deck;

    /**
     * The card pile from which cards are retrieved for recharging the deck.
     */
    private final CardPile cardPile;

    /**
     * Constructs a new RechargeDeck thread with the specified deck and card pile.
     * The thread must be started explicitly using {@link #start()} to begin
     * monitoring and recharging operations.
     *
     * @param deck the deck to monitor and recharge when empty
     * @param cardPile the pile from which to retrieve cards for recharging
     */
    public RechargeDeck(Deck deck, CardPile cardPile) {
        this.deck = deck;
        this.cardPile = cardPile;
    }

    /**
     * Runs the continuous deck monitoring and recharging process.
     * This method executes in an infinite loop, checking the deck status
     * every 500 milliseconds. When the deck is empty, it retrieves all cards
     * from the card pile (except the top card), shuffles them, and adds them
     * back to the deck.
     * 
     * The deck is synchronized during the recharge operation to prevent
     * concurrent access issues. If no cards are available in the pile
     * for recharging, a warning message is displayed.
     * 
     * 
     * The thread continues running until it is interrupted, at which point
     * it logs a termination message and exits gracefully.
     * 
     */
    @Override
    public void run() {
        try {
            while (true) {
                // Wait a bit before checking (avoid CPU overload)
                Thread.sleep(500);

                synchronized (deck) {
                    if (deck.getDeck().size()==2) {
                        // Get cards from the bottom (except the top card)
                        List<Card> backCards = cardPile.getBackCards(); // this method excludes the top card
                        if (!backCards.isEmpty()) {
                            Collections.shuffle(backCards);
                            for (Card card : backCards) {
                                deck.addCard(card);
                            }
                            System.out.println(" Deck recharged with " + backCards.size() + " cards.");
                        } else {
                            System.out.println("⚠ No cards available to recharge the deck.");
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Recharge thread interrupted.");
        }
    }
}

