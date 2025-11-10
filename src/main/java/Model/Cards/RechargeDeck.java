package Model.Cards;

import java.util.Collections;
import java.util.List;

public class RechargeDeck extends Thread {
    private final Deck deck;
    private final CardPile cardPile;

    public RechargeDeck(Deck deck, CardPile cardPile) {
        this.deck = deck;
        this.cardPile = cardPile;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Esperar un poco antes de revisar (no sobrecargar CPU)
                Thread.sleep(500);

                synchronized (deck) {
                    if (deck.getDeck().isEmpty()) {
                        // Obtener las cartas del fondo (excepto la superior)
                        List<Card> backCards = cardPile.getBackCards(); // este método debe excluir el top
                        if (!backCards.isEmpty()) {
                            Collections.shuffle(backCards);
                            for (Card card : backCards) {
                                deck.addCard(card);
                            }
                            System.out.println("🔄 Se recargó el deck con " + backCards.size() + " cartas.");
                        } else {
                            System.out.println("⚠️ No hay cartas para recargar el mazo.");
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Hilo de recarga interrumpido.");
        }
    }
}

