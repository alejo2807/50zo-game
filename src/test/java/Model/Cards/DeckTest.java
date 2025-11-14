package Model.Cards;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Deck} that verifies the functionality of deck operations
 * including card drawing, adding cards, and deck recreation from existing card collections.
 * This class contains unit tests that validate the behavior of deck management
 * under various conditions, including edge cases and error scenarios.
 * <p>
 * The tests cover:
 * <ul>
 *   <li>Error handling when drawing from an empty deck</li>
 *   <li>Proper card addition to the top of the deck</li>
 *   <li>Correct recreation of decks from card lists with shuffling</li>
 * </ul>
 * </p>
 *
 * @author Juan-David-Brandon
 * @version 1.0
 * @since 2025
 */
class DeckTest {

    /**
     * Tests that attempting to draw a card from an empty deck throws the appropriate exception.
     * <p>
     * Verifies that the {@link Deck#getCard()} method throws a {@link NoSuchElementException}
     * when the deck is empty, ensuring proper error handling and preventing silent failures
     * in game logic that depends on card availability.
     * </p>
     * <p>
     * Expected behavior: Empty deck should throw NoSuchElementException when getCard() is called.
     * </p>
     */
    @Test
    void testGetCard_WithEmptyDeck_ShouldThrowNoSuchElementException() {
        Deck deck = new Deck();
        deck.getDeck().clear();

        assertThrows(NoSuchElementException.class, () -> {
            deck.getCard();
        });
    }

    /**
     * Tests that adding a card places it at the top of the deck and increases the deck size.
     * <p>
     * Verifies that the {@link Deck#addCard(Card)} method correctly adds the specified card
     * to the top of the deck (making it the next card to be drawn) and properly increments
     * the deck size. This ensures proper deck management when recycling cards during gameplay.
     * </p>
     * <p>
     * Expected behavior: Deck size should increase by 1 and the added card should be at the top.
     * </p>
     */
    @Test
    void testAddCard_ShouldAddCardToTopOfDeck() {
        Deck deck = new Deck();
        Card testCard = new Card("A", "test_url");
        int initialSize = deck.getDeck().size();

        deck.addCard(testCard);

        assertEquals(initialSize + 1, deck.getDeck().size());
        assertEquals(testCard, deck.getDeck().peek());
    }

    /**
     * Tests that creating a new deck from a card list properly adds all cards in shuffled order.
     * <p>
     * Verifies that the {@link Deck#makeNewDeck(List)} method correctly processes a list of cards
     * by shuffling them and adding all cards to the deck. This functionality is crucial for
     * recycling discarded cards back into play and maintaining game continuity.
     * </p>
     * <p>
     * Expected behavior: All cards from the input list should be added to the deck after shuffling.
     * </p>
     */
    @Test
    void testMakeNewDeck_WithCardList_ShouldAddAllCardsInShuffledOrder() {
        Deck deck = new Deck();
        deck.getDeck().clear();
        List<Card> cards = Arrays.asList(
                new Card("A", "test1"),
                new Card("K", "test2"),
                new Card("Q", "test3")
        );

        deck.makeNewDeck(cards);

        assertEquals(3, deck.getDeck().size());
    }
}