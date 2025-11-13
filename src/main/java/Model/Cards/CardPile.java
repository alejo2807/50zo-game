package Model.Cards;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Represents a pile of cards with an accumulated value in the game.
 * This class manages a queue-based card pile and calculates the total value
 * by summing all card values. It implements special game rules, including
 * automatic value adjustments for Aces when the pile exceeds certain thresholds.
 * <p>
 * The pile maintains cards in FIFO (First-In-First-Out) order and provides
 * methods to add cards, retrieve the top card, and access cards below the top.
 * </p>
 * <p>
 * Special rule: When a card with value 10 (typically an Ace) is added and causes
 * the pile to exceed 50 points, the pile value is reduced by 9, effectively
 * treating the Ace as having a value of 1 instead of 10.
 * </p>
 *
 * @author Juan-David-Brandon
 * @version 1.0
 * @since 2025
 */
public class CardPile {
    /**
     * Queue containing the cards in this pile in FIFO order.
     * The front of the queue represents older cards, while the rear
     * represents more recently added cards.
     */
    Queue<Card> cardPile;

    /**
     * The accumulated value of all cards currently in the pile.
     * This value is updated automatically when cards are added,
     * including any special adjustments based on game rules.
     */
    int valuePile;

    /**
     * Constructs a new CardPile initialized with one card drawn from the specified deck.
     * The initial pile value is set to the value of this first card.
     *
     * @param deck the deck from which to draw the initial card for the pile
     */
    public CardPile(Deck deck) {
        cardPile = new LinkedList<>();
        Card card = deck.getCard();
        cardPile.add(card);
        valuePile = card.getValue();
    }

    /**
     * Adds a card to the pile and updates the total value accordingly.
     * The card is added to the rear of the queue, making it the new top card.
     * <p>
     * Special adjustment: If the added card has a value of 10 and adding it
     * causes the pile value to exceed 50, the pile value is reduced by 9.
     * This effectively treats the card as having a value of 1, simulating
     * the flexible value behavior of an Ace in many card games.
     * </p>
     *
     * @param card the card to add to the pile
     */
    public void addCard(Card card) {
        cardPile.add(card);
        valuePile += card.getValue();

        // Special adjustment for cards with value 10 when exceeding 50
        if (card.getValue() == 10 && valuePile > 50) {
            valuePile -= 9;
        }
    }

    /**
     * Returns a list of all cards beneath the top card in the pile.
     * This method removes all cards except the top one from the pile and
     * returns them in the order they were added. This is useful for
     * transferring cards to a discard pile or recharge deck.
     * <p>
     * <strong>Important:</strong> This method modifies the pile by removing cards.
     * After calling this method, only the top card remains in the pile.
     * If the pile has only one card or is empty, an empty list is returned
     * and the pile remains unchanged.
     * </p>
     *
     * @return a list containing all cards below the top card in the order they
     * were added, or an empty list if pile size is 1 or less
     */
    public List<Card> getBackCards() {
        List<Card> backCards = new ArrayList<>();
        int size = cardPile.size();

        // Prevent emptying the entire pile - keep at least one card
        if (size > 1) {
            for (int i = 0; i < size - 1; i++) {
                backCards.add(cardPile.poll());
            }
        }
        return backCards;
    }

    /**
     * Returns the top card of the pile (most recently played card) without removing it.
     * The top card is the last card that was added to the pile and represents
     * the current state of play.
     *
     * @return the card at the top of the pile, or {@code null} if the pile is empty
     */
    public Card getTopCard() {
        // If the pile is empty, return null
        if (cardPile.isEmpty()) return null;

        // Convert to temporary list to access the last element
        List<Card> temp = new ArrayList<>(cardPile);
        return temp.get(temp.size() - 1);
    }

    /**
     * Returns the accumulated value of all cards in the pile.
     * This value reflects the sum of all card values, including any
     * special adjustments that have been applied (such as Ace value changes).
     *
     * @return the total value of the card pile
     */
    public int getValuePile() {
        return valuePile;
    }
}

