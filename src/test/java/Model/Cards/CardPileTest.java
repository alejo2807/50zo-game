package Model.Cards;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link CardPile} that verifies the functionality of card pile operations.
 * This class contains unit tests that validate the behavior of adding cards to the pile,
 * retrieving cards from the bottom, and accessing the top card under various conditions.
 * <p>
 * The tests cover:
 * <ul>
 *   <li>Special value adjustment rules when adding cards that cause the pile value to exceed thresholds</li>
 *   <li>Correct retrieval of cards beneath the top card while maintaining pile integrity</li>
 *   <li>Proper handling of edge cases such as empty piles</li>
 * </ul>
 * </p>
 *
 * @author Juan-David-Brandon
 * @version 1.0
 * @since 2025
 */
class CardPileTest {

    /**
     * Tests the special value adjustment rule when adding an Ace card to a pile
     * that already has a high accumulated value.
     * <p>
     * Verifies that when a card with value 10 (typically an Ace) is added to a pile
     * with value exceeding 50 points, the pile value is automatically reduced by 9,
     * effectively treating the Ace as having a value of 1 instead of 10.
     * </p>
     * <p>
     * Expected behavior: Initial pile value of 45 + Ace value of 10 - adjustment of 9 = 46
     * </p>
     */
    @Test
    void testAddCard_WithAceWhenValueExceeds50_ShouldAdjustValueByMinus9() {
        CardPile pile = new CardPile(new Deck());
        // Set up initial value to be high
        pile.valuePile = 45;
        Card aceCard = new Card("A", "test_url"); // Value 10

        pile.addCard(aceCard);

        assertEquals(46, pile.getValuePile()); // 45 + 10 - 9 = 46
    }

    /**
     * Tests the retrieval of all cards beneath the top card from a pile with multiple cards.
     * <p>
     * Verifies that the {@link CardPile#getBackCards()} method correctly returns all cards
     * except the top card (most recently added card) while maintaining the top card in the pile.
     * The pile size should be reduced to 1 after the operation, preserving only the top card.
     * </p>
     * <p>
     * Expected behavior: Pile with 3 cards (initial + 2 added) should return 2 back cards
     * and retain 1 card (the top card) in the pile.
     * </p>
     */
    @Test
    void testGetBackCards_WithMultipleCards_ShouldReturnAllButTopCard() {
        Deck deck = new Deck();
        CardPile pile = new CardPile(deck);
        Card card2 = new Card("2", "test_url");
        Card card3 = new Card("3", "test_url");
        pile.addCard(card2);
        pile.addCard(card3);

        List<Card> backCards = pile.getBackCards();

        assertEquals(2, backCards.size());
        assertEquals(1, pile.cardPile.size());
    }

    /**
     * Tests the behavior of retrieving the top card from an empty pile.
     * <p>
     * Verifies that the {@link CardPile#getTopCard()} method returns {@code null}
     * when the pile contains no cards, ensuring proper handling of empty pile conditions
     * and preventing null pointer exceptions in calling code.
     * </p>
     * <p>
     * Expected behavior: Empty pile should return {@code null} when attempting to get top card.
     * </p>
     */
    @Test
    void testGetTopCard_WithEmptyPile_ShouldReturnNull() {
        CardPile pile = new CardPile(new Deck());
        pile.cardPile.clear(); // Empty the pile

        Card topCard = pile.getTopCard();

        assertNull(topCard);
    }
}