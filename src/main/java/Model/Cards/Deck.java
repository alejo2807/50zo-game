package Model.Cards;

import java.util.*;

/**
 * Represents a standard deck of 52 playing cards.
 * This class manages a complete deck containing all combinations of 13 symbols
 * and 4 suits. The deck is implemented as a Deque (double-ended queue) to
 * efficiently support drawing cards from the top and adding cards back.
 *
 * The deck supports various operations including:
 *
 *   Drawing cards from the top
 *   Adding cards back to the deck
 *   Shuffling the deck randomly
 *   Creating new decks from existing card collections
 *
 *
 *
 * Upon creation, the deck is automatically initialized with all 52 cards
 * and shuffled randomly to ensure fair play.
 *
 *
 * @author Juan-David-Brandon
 * @version 1.0
 * @since 2025
 */
public class Deck {
    /**
     * The collection of cards in this deck, implemented as a Deque.
     * Cards are stored in LIFO (Last-In-First-Out) order with the
     * top of the deck at the head of the deque.
     */
    private Deque<Card> deck;

    /**
     * List of all card symbols in a standard deck.
     * Contains 13 symbols: 2, 3, 4, 5, 6, 7, 8, 9, 10, J (Jack), Q (Queen), K (King), A (Ace).
     */
    private final List<String> symbols = List.of("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A");

    /**
     * List of all card suits in a standard deck.
     * Contains 4 suits: clubs, diamonds, hearts, spades.
     */
    private final List<String> suits = List.of("clubs", "diamonds", "hearts", "spades");

    /**
     * Constructs a new Deck containing all 52 standard playing cards.
     * The deck is automatically initialized with one card for each combination
     * of symbol and suit, then shuffled randomly to ensure unpredictability.
     */
    public Deck() {
        deck = new ArrayDeque<>();
        initializeDeck();
    }

    /**
     * Initializes the deck with all 52 cards and shuffles them randomly.
     * Creates one card for each combination of the 13 symbols and 4 suits,
     * assigns the appropriate image file path to each card based on its
     * symbol and suit, then shuffles them using a secure random algorithm
     * before adding them to the deck.
     *
     * The image file path format is: {@code /deck/{symbol}_of_{suit}.png}
     *
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
     * The card becomes the next card to be drawn when {@link #getCard()} is called.
     *
     * @param card the card to add to the top of the deck
     */
    public void addCard(Card card) {
        deck.push(card);
    }

    /**
     * Removes and returns the top card from the deck.
     * This operation reduces the deck size by one.
     *
     * @return the card from the top of the deck
     * @throws NoSuchElementException if the deck is empty and no card can be drawn
     */
    public Card getCard() {
        return deck.pop();
    }

    /**
     * Creates a new deck from a list of cards and adds them to the current deck.
     * The provided cards are shuffled randomly before being added to ensure
     * randomness. This method is particularly useful for recycling discarded
     * cards back into play when the main deck runs low or empty.
     *
     * The cards are added to the top of the existing deck in shuffled order.
     *
     *
     * @param cards the list of cards to shuffle and add to the deck
     */
    public void makeNewDeck(List<Card> cards) {
        Collections.shuffle(cards);
        for (Card card : cards) {
            addCard(card);
        }
    }

    /**
     * Returns the underlying Deque containing all cards currently in the deck.
     * This provides direct access to the deck's internal structure and allows
     * for iteration or size checking.
     *
     * <strong>Note:</strong> Modifying the returned Deque directly will affect
     * the internal state of the deck.
     *
     *
     * @return the deck as a Deque of Card objects
     */
    public Deque<Card> getDeck() {
        return deck;
    }

    /**
     * Shuffles all cards currently in the deck randomly.
     * The deck is temporarily converted to a list, shuffled using
     * {@link Collections#shuffle(List)}, cleared, and then restored
     * with the shuffled cards. This ensures a secure random distribution
     * of cards throughout the deck.
     */
    public void shuffle() {
        // Simple and safe approach
        List<Card> tempList = new ArrayList<>(deck); // Direct copy
        deck.clear(); // Empty the deck
        Collections.shuffle(tempList); // Shuffle the temporary list
        deck.addAll(tempList); // Add all back
    }

    /**
     * Main method for testing the Deck class functionality.
     * Creates a new deck, draws one card from the top, prints its symbol,
     * and then prints the symbols of all remaining cards in the deck to
     * demonstrate proper initialization and card drawing.
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
