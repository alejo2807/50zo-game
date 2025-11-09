package Model.Cards;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CardPile {
    private Queue<Card> cardPile = new LinkedList<>();
    private int valuePile;

    public CardPile(Deck deck) {
        Card card = deck.getCard();
        cardPile.add(card);
        valuePile = card.getValue();

    }

    /** Agrega una carta a la pila y actualiza su valor total **/
    public void addCard(Card card) {
        cardPile.add(card);
        valuePile += card.getValue();

        // Ajuste especial para cartas de valor 10 cuando superan 50
        if (card.getValue() == 10 && valuePile > 50) {
            valuePile -= 9;
        }
    }

    /** Devuelve una lista de las cartas que están debajo de la última jugada (para descartar o mostrar historial) **/
    public List<Card> getBackCards() {
        List<Card> backCards = new ArrayList<>();
        int size = cardPile.size();

        // Evitar que se vacíe toda la pila
        if (size > 1) {
            for (int i = 0; i < size - 1; i++) {
                backCards.add(cardPile.poll());
            }
        }
        return backCards;
    }

    /** 🔹 Devuelve la carta superior de la pila (última jugada) **/
    public Card getTopCard() {
        // Si la pila está vacía, devuelve null
        if (cardPile.isEmpty()) return null;

        // Convertimos a lista temporal para acceder al último elemento
        List<Card> temp = new ArrayList<>(cardPile);
        return temp.get(temp.size() - 1);
    }

    /** Devuelve el valor acumulado de la pila **/
    public int getValuePile() {
        return valuePile;
    }


}
