package Model.Players;

import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;
import Model.Exceptions.InvalidCardException;

import java.util.List;

/**
 * The {@code IPlayers} interface defines the fundamental behaviors and actions
 * that any player in the card game must implement.
 * 
 * This interface establishes a clear contract for handling a player's hand,
 * drawing cards from the deck, and playing cards onto a pile. Concrete player
 * implementations (such as human or AI players) must provide specific logic for
 * each of these operations.
 * 
 *
 * @author Juan-David-Brandon
 * @since 2025
 */
public interface IPlayers {

    /**
     * Adds a card to the player's hand.
     *
     * @param card the card to be added to the player's hand
     */
    void takeCard(Card card);

    /**
     * Plays a card from the player's hand to the specified card pile.
     * 
     * The card is identified by its index position in the player's hand.
     * This method should verify that the card can be legally played according
     * to the current game rules.
     * 
     *
     * @param indexCard the index of the card in the player's hand to be played
     * @param cardPile  the pile where the card will be placed
     * @throws InvalidCardException if the card cannot be played according to the game rules
     * @throws IllegalArgumentException if the provided card index is invalid
     */
    void putCard(int indexCard, CardPile cardPile) throws InvalidCardException;

    /**
     * Draws or initializes the player's starting hand.
     * 
     * Typically called at the beginning of a game or round to give the player
     * their initial set of cards.
     * 
     */
    void takeHand();

    /**
     * Returns the list of cards currently in the player's hand.
     *
     * @return a list containing all cards currently held by the player
     */
    List<Card> getHand();
}
