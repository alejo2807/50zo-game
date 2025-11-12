package Model.Cards;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Represents a pile of cards with an accumulated value.
 * <p>
 * This class manages a queue of cards and calculates the total value of the pile.
 * It includes special logic for cards with value 10 when the pile exceeds 50 points.
 * </p>
 *
 * @author [Your Name]
 * @version 1.0
 */
public class CardPile {
    /**
     * Queue containing the cards in this pile.
     */
    private Queue<Card> cardPile;

    /**
     * The accumulated value of all cards in the pile.
     */
    private int valuePile;

    /**
     * Constructs a new CardPile initialized with one card from the specified deck.
     * The initial pile value is set to the value of the first card.
     *
     * @param deck the deck from which to draw the initial card
     */
    public CardPile(Deck deck) {
        cardPile = new LinkedList<>();
        Card card = deck.getCard();
        cardPile.add(card);
        valuePile = card.getValue();
    }

    /**
     * Adds a card to the pile and updates the total value.
     * <p>
     * Special adjustment: If the added card has a value of 10 and the pile value
     * exceeds 50 after adding it, the pile value is reduced by 9.
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
     * <p>
     * This method removes all cards except the top one from the pile and returns them
     * in a list. Useful for discarding or displaying card history.
     * </p>
     * <p>
     * Note: This method modifies the pile by removing cards. If the pile has only one card,
     * an empty list is returned and the pile remains unchanged.
     * </p>
     *
     * @return a list containing all cards below the top card, or an empty list if pile size is 1 or less
     */
    public List<Card> getBackCards() {
        List<Card> backCards = new ArrayList<>();
        int size = cardPile.size();

        // Prevent emptying the entire pile
        if (size > 1) {
            for (int i = 0; i < size - 1; i++) {
                backCards.add(cardPile.poll());
            }
        }
        return backCards;
    }

    /**
     * Returns the top card of the pile (most recently played card) without removing it.
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
     *
     * @return the total value of the card pile
     */
    public int getValuePile() {
        return valuePile;
    }
}