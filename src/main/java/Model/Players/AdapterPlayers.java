package Model.Players;

import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;
import Model.Exceptions.InvalidCardException;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code AdapterPlayers} class serves as an abstract base for different types of players
 * participating in the card game. It implements the {@link IPlayers} interface and extends
 * {@link Thread} to allow concurrent execution of player actions.
 * <p>
 * Each player has its own hand of cards, a turn order, and access to shared game elements
 * such as the deck, card pile, and turn manager.
 * </p>
 *
 * @author Juan-David-Brandon
 * @since 2025
 */
public abstract class AdapterPlayers extends Thread implements IPlayers {

    /** The list of cards currently held by the player. */
    protected List<Card> hand = new ArrayList<>();

    /** Indicates whether the player is still active in the game. */
    protected boolean isPlaying;

    /** The player’s assigned turn number. */
    protected int turn;

    /** Synchronization lock object for controlling turn-based execution. */
    protected Object lock;

    /** Manages the order and state of player turns. */
    protected TurnManager turnManager;

    /** The pile of cards played during the game. */
    protected CardPile cardPile;

    /** The main deck of cards used for drawing new ones. */
    protected Deck deck;

    /**
     * Constructs a new player adapter with the specified game components and configuration.
     *
     * @param deck         the main deck used for drawing cards
     * @param myTurn       the turn number assigned to the player
     * @param lock         the synchronization object used for turn control
     * @param turnManager  the manager that controls turn order
     * @param cardPile     the pile of cards currently in play
     */
    public AdapterPlayers(Deck deck, int myTurn, Object lock, TurnManager turnManager, CardPile cardPile) {
        this.deck = deck;
        this.isPlaying = false;
        this.turn = myTurn;
        this.lock = lock;
        this.turnManager = turnManager;
        this.cardPile = cardPile;
    }

    /**
     * Constructs a new player adapter initialized with a deck only.
     * This version automatically sets the player as active.
     *
     * @param deck the deck from which the player will draw cards
     */
    public AdapterPlayers(Deck deck) {
        this.deck = deck;
        this.isPlaying = true;
    }

    /**
     * Adds a specified card to the player's hand.
     *
     * @param card the card to add
     */
    @Override
    public void takeCard(Card card) {
        hand.add(card);
    }

    /**
     * Attempts to play a card from the player's hand at the given index.
     * If the card would cause the pile's total value to exceed 50, an {@link InvalidCardException} is thrown.
     *
     * @param indexCard the index of the card in the player's hand
     * @param cardPile  the card pile where the card will be placed
     * @throws InvalidCardException if playing the card exceeds the value limit
     * @throws IllegalArgumentException if the provided index is invalid
     */
    @Override
    public void putCard(int indexCard, CardPile cardPile) throws InvalidCardException {
        if (indexCard < 0 || indexCard >= hand.size()) {
            throw new IllegalArgumentException("Invalid card index: " + indexCard);
        }

        Card card = hand.get(indexCard);
        int newValue = card.getValue() + cardPile.getValuePile();

        // Validate that the new pile value does not exceed 50
        if (newValue > 50) {
            throw new InvalidCardException(card, cardPile.getValuePile());
        }

        // If valid, play the card
        hand.remove(indexCard);
        cardPile.addCard(card);
    }

    /**
     * Clears the player's hand and draws 4 new cards from the deck.
     * If the deck is empty, fewer than 4 cards may be drawn.
     */
    @Override
    public void takeHand() {
        hand.clear();
        for (int i = 0; i < 4; i++) {
            Card card = deck.getCard();
            if (card != null) {
                hand.add(card);
            }
        }
    }

    /**
     * Returns the current list of cards in the player's hand.
     *
     * @return a list of cards currently held by the player
     */
    @Override
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Returns whether the player is still active in the game.
     *
     * @return {@code true} if the player is active; {@code false} otherwise
     */
    public boolean getIsPlaying() {
        return isPlaying;
    }

    /**
     * Updates the player's active state.
     *
     * @param isPlaying the new state of the player
     */
    void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    /**
     * Checks if the player has any playable cards that can be placed on the pile
     * without exceeding a total value of 50.
     * <p>
     * If no valid cards exist, the player is marked as eliminated.
     * </p>
     *
     * @return {@code true} if the player has valid cards to play; {@code false} otherwise
     */
    public boolean hasValidCards() {
        int cont = 0;
        for (Card card : hand) {
            if (card.getValue() + cardPile.getValuePile() <= 50) {
                cont++;
            }
        }
        boolean hasValid = cont > 0;
        if (!hasValid) {
            isPlaying = false;
            System.out.println("❌ Player " + turn + " eliminated: no valid cards (pile=" + cardPile.getValuePile() + ")");
        }
        return hasValid;
    }

    /**
     * Returns the player's current active state (duplicate accessor).
     *
     * @return {@code true} if the player is still playing; {@code false} otherwise
     */
    public boolean getIsplaying() {
        return isPlaying;
    }

    /**
     * Returns the player's turn number.
     *
     * @return the player's turn identifier
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Initializes the player by activating them and dealing an initial hand.
     * Prints a confirmation message to the console.
     */
    public void initializePlayer() {
        isPlaying = true;
        takeHand();
        System.out.println("✅ Player " + turn + " initialized with " + hand.size() + " cards");
    }
}
