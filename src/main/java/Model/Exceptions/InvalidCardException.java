package Model.Exceptions;

import Model.Cards.Card;

public class InvalidCardException extends Exception {
    private Card attemptedCard;
    private int currentPileValue;
    private int resultingValue;

    public InvalidCardException(Card card, int currentPileValue) {
        super(String.format("La carta %s (valor=%d) no puede jugarse. Valor pila: %d, Resultado: %d (supera 50)",
                card.getSymbol(), card.getValue(), currentPileValue, card.getValue() + currentPileValue));
        this.attemptedCard = card;
        this.currentPileValue = currentPileValue;
        this.resultingValue = card.getValue() + currentPileValue;
    }

    public Card getAttemptedCard() {
        return attemptedCard;
    }

    public int getCurrentPileValue() {
        return currentPileValue;
    }

    public int getResultingValue() {
        return resultingValue;
    }

    public String getDetailedMessage() {
        return String.format("⚠️ Carta inválida: %s\n" +
                        "   Valor actual de la pila: %d\n" +
                        "   Valor de la carta: %d\n" +
                        "   Resultado sería: %d (Máximo permitido: 50)",
                attemptedCard.getSymbol(), currentPileValue, attemptedCard.getValue(), resultingValue);
    }
}