package Model.Players;

import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;
import Model.Exceptions.InvalidCardException;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code AdapterPlayers} abstract class serves as the base implementation
 * for all player types participating in the card game.
 * <p>
 * It implements the {@link IPlayers} interface and extends {@link Thread},
 * allowing each player to run concurrently if needed.
 * </p>
 *
 * <p>
 * Each player maintains:
 * </p>
 * <ul>
 *   <li>A hand of cards</li>
 *   <li>A turn identifier</li>
 *   <li>Access to the shared deck and card pile</li>
 *   <li>A reference to the {@link TurnManager}</li>
 *   <li>A synchronization lock for turn-based gameplay</li>
 * </ul>
 *
 * <p>
 * This class provides base behavior such as drawing cards, checking playable cards,
 * and placing cards onto the central pile. Subclasses must implement specific
 * gameplay behavior (e.g., human input or AI decisions).
 * </p>
 *
 * @author Juan
 * @author David
 * @author Brandon
 * @since 2025
 */
public abstract class AdapterPlayers extends Thread implements IPlayers {

    /** The list of cards currently held by the player. */
    protected List<Card> hand = new ArrayList<>();

    /** Indicates whether the player is still active in the game. */
    protected boolean isPlaying;

    /** The numeric turn assigned to the player. */
    protected int turn;

    /** Synchronization lock used for enforcing turn order. */
    protected Object lock;

    /** Manages the order and status of player turns. */
    protected TurnManager turnManager;

    /** The central pile where cards are played. */
    protected CardPile cardPile;

    /** The deck used for drawing new cards. */
    protected Deck deck;

    /** Describes the type of player (e.g., Human, GPU). */
    protected String playerType;

    /**
     * Creates a new player adapter with all necessary game components.
     *
     * @param deck        the shared deck used to draw cards
     * @param myTurn      the turn number assigned to the player
     * @param lock        the synchronization lock controlling turn access
     * @param turnManager the manager responsible for turn sequencing
     * @param cardPile    the central pile where played cards accumulate
     * @param playerType  a string identifying the player type
     */
    public AdapterPlayers(Deck deck,
                          int myTurn,
                          Object lock,
                          TurnManager turnManager,
                          CardPile cardPile,
                          String playerType) {

        this.deck = deck;
        this.isPlaying = false;
        this.turn = myTurn;
        this.lock = lock;
        this.turnManager = turnManager;
        this.cardPile = cardPile;
        this.playerType = playerType;
    }

    /**
     * Creates a simplified player adapter using only a deck.
     * This constructor automatically marks the player as active.
     *
     * @param deck the deck used for drawing cards
     */
    public AdapterPlayers(Deck deck) {
        this.deck = deck;
        this.isPlaying = true;
    }

    /**
     * Returns the identifier describing the type of player.
     *
     * @return the player type string
     */
    public String getPlayerType() {
        return playerType;
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card the card to add
     */
    @Override
    public void takeCard(Card card) {
        hand.add(card);
    }

    /**
     * Attempts to play the card at the given index.
     * <p>
     * Validates that the card does not cause the pile to exceed 50 points.
     * Special handling is applied for Ace cards (A), which may adopt two values.
     * </p>
     *
     * @param indexCard the position of the card in the player's hand
     * @param cardPile  the pile where the card should be placed
     * @throws InvalidCardException      if the resulting pile value exceeds 50
     * @throws IllegalArgumentException  if the index is outside the hand’s bounds
     */
    @Override
    public void putCard(int indexCard, CardPile cardPile) throws InvalidCardException {
        if (indexCard < 0 || indexCard >= hand.size()) {
            throw new IllegalArgumentException("Invalid card index: " + indexCard);
        }

        Card card = hand.get(indexCard);
        int newValue = card.getValue() + cardPile.getValuePile();

        // Handle Ace special case (value can be reduced by 9 if needed)
        if (card.getSymbol().equals("A")) {
            if (card.getValue() + cardPile.getValuePile() > 50 &&
                    card.getValue() - 9 + cardPile.getValuePile() <= 50) {
                newValue -= 9;
            }
        }

        if (newValue > 50) {
            throw new InvalidCardException(card, cardPile.getValuePile());
        }

        hand.remove(indexCard);
        cardPile.addCard(card);
    }

    /**
     * Clears the player's hand and draws a fresh hand containing up to 4 cards.
     * Fewer cards may be drawn if the deck is depleted.
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
     * Returns the current cards held by the player.
     *
     * @return a list of cards in the player's hand
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
     * @param isPlaying the new active status
     */
    void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    /**
     * Determines whether the player has any legal moves available.
     * <p>
     * A legal move is one in which a card can be played without the pile value
     * exceeding 50. Ace cards receive special handling due to their dual-value nature.
     * </p>
     * <p>
     * If the player has no valid cards, they are automatically eliminated.
     * </p>
     *
     * @return {@code true} if at least one card is playable; {@code false} otherwise
     */
    public boolean hasValidCards() {
        int validCount = 0;

        for (Card card : hand) {
            int pileValue = cardPile.getValuePile();

            if (card.getSymbol().equals("A")) {
                if (card.getValue() + pileValue <= 50 ||
                        card.getValue() - 9 + pileValue <= 50) {
                    validCount++;
                }
            } else if (card.getValue() + pileValue <= 50) {
                validCount++;
            }
        }

        boolean hasValid = validCount > 0;
        if (!hasValid) {
            isPlaying = false;
        }
        return hasValid;
    }

    /**
     * Returns whether the player is still participating in the game.
     * (Duplicate accessor maintained for compatibility.)
     *
     * @return {@code true} if the player is still active; {@code false} otherwise
     */
    public boolean getIsplaying() {
        return isPlaying;
    }

    /**
     * Returns the player's assigned turn number.
     *
     * @return the numeric turn identifier
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Initializes the player at the start of a match.
     * <p>
     * This method:
     * </p>
     * <ul>
     *   <li>Marks the player as active</li>
     *   <li>Draws an initial hand of cards</li>
     * </ul>
     */
    public void initializePlayer() {
        isPlaying = true;
        takeHand();
    }
}
