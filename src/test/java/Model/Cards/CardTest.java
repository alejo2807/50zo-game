package Model.Cards;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for {@link Card} that verifies the card value assignment system
 * based on different card symbols according to the game rules.
 * This class contains unit tests that validate the automatic value calculation
 * when setting card symbols, ensuring proper implementation of the card value rules.
 * <p>
 * The tests cover all symbol categories defined in the game rules:
 * <ul>
 *   <li>Numeric cards (2-8 and 10) that retain their face value</li>
 *   <li>Face cards (J, Q, K) that have a value of -10</li>
 *   <li>Special cards: 9 with value 0 and Ace with default value 10</li>
 * </ul>
 * </p>
 *
 * @author Juan-David-Brandon
 * @version 1.0
 * @since 2025
 */
class CardTest {

    /**
     * Tests that numeric card symbols (2-8 and 10) correctly set their corresponding face values.
     * <p>
     * Verifies that when setting symbols representing numeric cards, the card's value
     * is automatically assigned the integer equivalent of the symbol according to
     * the game rules. This includes testing boundary values (2, 8) and the special
     * case of 10 which is also treated as a numeric card.
     * </p>
     * <p>
     * Expected behavior: Symbols "2", "8", and "10" should set values 2, 8, and 10 respectively.
     * </p>
     */
    @Test
    void testSetSymbol_ConNumbers2To8And10_ShouldSetCorrespondingNumericValue() {
        Card card = new Card("A", "test_url");

        card.setSymbol("2");
        assertEquals(2, card.getValue());
        assertEquals("2", card.getSymbol());

        card.setSymbol("8");
        assertEquals(8, card.getValue());
        assertEquals("8", card.getSymbol());

        card.setSymbol("10");
        assertEquals(10, card.getValue());
        assertEquals("10", card.getSymbol());
    }

    /**
     * Tests that special cards (9 and Ace) correctly set their predefined special values.
     * <p>
     * Verifies the special value assignments for cards that don't follow the standard
     * numeric pattern: card 9 has a value of 0, and Ace has a default value of 10.
     * These special cases are important game mechanics that affect scoring strategy.
     * </p>
     * <p>
     * Expected behavior: Symbol "9" should set value 0, symbol "A" should set value 10.
     * </p>
     */
    @Test
    void testSetSymbol_Con9yA_ShouldSetSpecialValues() {
        Card card = new Card("2", "test_url");

        card.setSymbol("9");
        assertEquals(0, card.getValue());
        assertEquals("9", card.getSymbol());

        card.setSymbol("A");
        assertEquals(10, card.getValue());
        assertEquals("A", card.getSymbol());
    }

    /**
     * Tests that face cards (Jack, Queen, King) correctly set their value to -10.
     * <p>
     * Verifies that all three face card symbols (J, Q, K) consistently assign
     * the same value of -10, implementing the game rule that face cards have
     * negative point values. This negative value is a key strategic element
     * in the game's scoring system.
     * </p>
     * <p>
     * Expected behavior: Symbols "J", "Q", and "K" should all set value -10.
     * </p>
     */
    @Test
    void testSetSymbol_ConJQK_ShouldSetMinus10Value() {
        Card card = new Card("A", "test_url");

        card.setSymbol("J");
        assertEquals(-10, card.getValue());
        assertEquals("J", card.getSymbol());

        card.setSymbol("Q");
        assertEquals(-10, card.getValue());
        assertEquals("Q", card.getSymbol());

        card.setSymbol("K");
        assertEquals(-10, card.getValue());
        assertEquals("K", card.getSymbol());
    }
}