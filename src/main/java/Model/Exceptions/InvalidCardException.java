package Model.Exceptions;

import Model.Cards.Card;

/**
 * Exception thrown when a player attempts to play a card that would cause
 * the pile value to exceed the maximum allowed value of 50.
 * <p>
 * This exception captures detailed information about the invalid play attempt,
 * including the card that was attempted, the current pile value, and what
 * the resulting value would have been.
 * </p>
 *
 * @author [Your Name]
 * @version 1.0
 */
public class InvalidCardException extends Exception {
    /**
     * The card that was attempted to be played.
     */
    private Card attemptedCard;

    /**
     * The value of the pile before the attempted play.
     */
    private int currentPileValue;

    /**
     * The value that would result from playing the attempted card.
     */
    private int resultingValue;

    /**
     * Constructs a new InvalidCardException with detailed information about the invalid play.
     * <p>
     * The exception message is automatically formatted to include the card symbol,
     * card value, current pile value, and the resulting value that exceeds 50.
     * </p>
     *
     * @param card the card that cannot be played
     * @param currentPileValue the current value of the pile before the attempted play
     */
    public InvalidCardException(Card card, int currentPileValue) {
        super(String.format("La carta %s (valor=%d) no puede jugarse. Valor pila: %d, Resultado: %d (supera 50)",
                card.getSymbol(), card.getValue(), currentPileValue, card.getValue() + currentPileValue));
        this.attemptedCard = card;
        this.currentPileValue = currentPileValue;
        this.resultingValue = card.getValue() + currentPileValue;
    }

    /**
     * Returns the card that was attempted to be played.
     *
     * @return the attempted card
     */
    public Card getAttemptedCard() {
        return attemptedCard;
    }

    /**
     * Returns the value of the pile before the invalid play attempt.
     *
     * @return the current pile value
     */
    public int getCurrentPileValue() {
        return currentPileValue;
    }

    /**
     * Returns the value that would result from playing the attempted card.
     *
     * @return the resulting value (current pile value + attempted card value)
     */
    public int getResultingValue() {
        return resultingValue;
    }

    /**
     * Returns a detailed, user-friendly message describing why the card is invalid.
     * <p>
     * The message includes:
     * <ul>
     *   <li>The symbol of the invalid card</li>
     *   <li>The current pile value</li>
     *   <li>The value of the attempted card</li>
     *   <li>What the resulting value would be</li>
     *   <li>The maximum allowed value (50)</li>
     * </ul>
     * </p>
     *
     * @return a formatted string with detailed information about the invalid play
     */
    public String getDetailedMessage() {
        return String.format("⚠️ Carta inválida: %s\n" +
                        "   Valor actual de la pila: %d\n" +
                        "   Valor de la carta: %d\n" +
                        "   Resultado sería: %d (Máximo permitido: 50)",
                attemptedCard.getSymbol(), currentPileValue, attemptedCard.getValue(), resultingValue);
    }
}