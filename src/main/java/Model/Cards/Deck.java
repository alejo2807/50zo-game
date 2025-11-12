package Model.Cards;

import java.util.*;

/**
 * Represents a deck of playing cards.
 * <p>
 * This class manages a standard deck of 52 cards (13 symbols × 4 suits).
 * The deck is implemented as a Deque (double-ended queue) and supports operations
 * such as drawing cards, adding cards, shuffling, and creating new decks from existing cards.
 * </p>
 *
 * @author [Your Name]
 * @version 1.0
 */
public class Deck {
    /**
     * The collection of cards in this deck, implemented as a Deque.
     */
    private Deque<Card> deck;

    /**
     * List of all card symbols in a standard deck.
     * Contains: 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K, A.
     */
    private final List<String> symbols = List.of("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A");

    /**
     * List of all card suits in a standard deck.
     * Contains: clubs, diamonds, hearts, spades.
     */
    private final List<String> suits = List.of("clubs", "diamonds", "hearts", "spades");

    /**
     * Constructs a new shuffled Deck containing all 52 standard playing cards.
     * The deck is automatically initialized and shuffled upon creation.
     */
    public Deck() {
        deck = new ArrayDeque<>();
        initializeDeck();
    }

    /**
     * Initializes the deck with all 52 cards and shuffles them.
     * <p>
     * Creates one card for each combination of symbol and suit, assigns the appropriate
     * image file path to each card, shuffles them randomly, and adds them to the deck.
     * </p>
     */
    private void initializeDeck() {
        List<Card> tempList = new ArrayList<>();

        for (String symbol : symbols) {
            for (String suit : suits) {
                String fileName = String.format("/deck/%s_of_%s.png", symbol, suit);
                Card card = new Card(symbol, fileName);
                tempList.add(card);
            }
        }
        Collections.shuffle(tempList);
        for (Card card : tempList) {
            deck.push(card);
        }
    }

    /**
     * Adds a card to the top of the deck.
     *
     * @param card the card to add to the deck
     */
    public void addCard(Card card) {
        deck.push(card);
    }

    /**
     * Removes and returns the top card from the deck.
     *
     * @return the card from the top of the deck
     * @throws NoSuchElementException if the deck is empty
     */
    public Card getCard() {
        return deck.pop();
    }

    /**
     * Creates a new deck from a list of cards.
     * <p>
     * The provided cards are shuffled and then added to the deck.
     * This method is useful for recycling discarded cards back into play.
     * </p>
     *
     * @param cards the list of cards to create the new deck from
     */
    public void makeNewDeck(List<Card> cards) {
        Collections.shuffle(cards);
        for (Card card : cards) {
            addCard(card);
        }
    }

    /**
     * Returns the underlying Deque containing all cards in the deck.
     *
     * @return the deck as a Deque of cards
     */
    public Deque<Card> getDeck() {
        return deck;
    }

    /**
     * Shuffles all cards currently in the deck randomly.
     * <p>
     * The deck is converted to a list, shuffled using {@link Collections#shuffle(List)},
     * and then restored to the Deque structure.
     * </p>
     */
    public void shuffle() {
        // Simple and safe approach
        List<Card> tempList = new ArrayList<>(deck); // Direct copy
        deck.clear(); // Empty the deck
        Collections.shuffle(tempList); // Shuffle the temporary list
        deck.addAll(tempList); // Add all back
    }

    /**
     * Main method for testing the Deck class.
     * <p>
     * Creates a deck, draws one card, prints its symbol, and then prints
     * all remaining cards in the deck.
     * </p>
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        Deck deck = new Deck();
        Card card = deck.getCard();
        System.out.printf("Card: %s\n", card.getSymbol());
        for (Card c : deck.getDeck()) {
            System.out.println(c.getSymbol());
        }
    }
}