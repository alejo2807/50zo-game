package Model.Exceptions;

import Model.Cards.Card;

/**
 * Exception thrown when a player attempts to play a card that would cause
 * the pile value to exceed the maximum allowed value of 50.
 * This exception is a core part of the game's rule enforcement mechanism,
 * preventing invalid moves and maintaining game integrity.
 * <p>
 * This exception captures comprehensive information about the invalid play attempt,
 * including:
 * <ul>
 *   <li>The card that was attempted to be played</li>
 *   <li>The current pile value before the attempt</li>
 *   <li>The resulting value that would exceed the limit</li>
 * </ul>
 * </p>
 * <p>
 * The exception provides both a basic error message (via {@link #getMessage()})
 * and a detailed, user-friendly message (via {@link #getDetailedMessage()}) that
 * can be displayed to players to explain why their move was rejected.
 * </p>
 *
 * @author Juan-David-Brandon
 * @version 1.0
 * @since 2025
 */
public class InvalidCardException extends Exception {
    /**
     * The card that was attempted to be played but was rejected.
     */
    private Card attemptedCard;

    /**
     * The value of the pile immediately before the attempted play.
     */
    private int currentPileValue;

    /**
     * The value that would result from playing the attempted card.
     * This value exceeds the maximum allowed value of 50.
     */
    private int resultingValue;

    /**
     * Constructs a new InvalidCardException with detailed information about the invalid play.
     * The exception message is automatically formatted in Spanish to include the card symbol,
     * card value, current pile value, and the resulting value that exceeds 50.
     * <p>
     * The resulting value is calculated as the sum of the current pile value
     * and the attempted card's value.
     * </p>
     *
     * @param card the card that cannot be played due to exceeding the pile limit
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
     * @return the attempted card that caused the exception
     */
    public Card getAttemptedCard() {
        return attemptedCard;
    }

    /**
     * Returns the value of the pile before the invalid play attempt.
     *
     * @return the current pile value at the time of the exception
     */
    public int getCurrentPileValue() {
        return currentPileValue;
    }

    /**
     * Returns the value that would result from playing the attempted card.
     * This value is the sum of the current pile value and the attempted card's value,
     * and it exceeds the maximum allowed value of 50.
     *
     * @return the resulting value (current pile value + attempted card value)
     */
    public int getResultingValue() {
        return resultingValue;
    }

    /**
     * Returns a detailed, user-friendly message in Spanish describing why the card is invalid.
     * This message is formatted for display to players and includes all relevant
     * information about the failed play attempt.
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
     * @return a formatted string with detailed information about the invalid play,
     *         suitable for display to the player
     */
    public String getDetailedMessage() {
        return String.format("⚠️ Carta inválida: %s\n" +
                        "   Valor actual de la pila: %d\n" +
                        "   Valor de la carta: %d\n" +
                        "   Resultado sería: %d (Máximo permitido: 50)",
                attemptedCard.getSymbol(), currentPileValue, attemptedCard.getValue(), resultingValue);
    }
}