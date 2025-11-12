package Model.Players;

import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;
import Model.Exceptions.InvalidCardException;

import java.util.List;

/**
 * Interface defining the basic operations for a player in the card game.
 * <p>
 * This interface establishes the contract for player behavior, including
 * managing cards in hand, drawing cards, and playing cards to a pile.
 * All player implementations must provide concrete implementations of these methods.
 * </p>
 *
 * @author [Your Name]
 * @version 1.0
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
     * <p>
     * The card is identified by its index position in the player's hand.
     * This method may validate whether the card can be legally played according
     * to game rules.
     * </p>
     *
     * @param IndexCard the index of the card in the player's hand to be played
     * @param cardPile the pile where the card will be placed
     * @throws InvalidCardException if the card cannot be played according to game rules
     */
    void putCard(int IndexCard, CardPile cardPile) throws InvalidCardException;

    /**
     * Initializes or draws the player's starting hand.
     * <p>
     * This method is typically called at the beginning of a game or round
     * to give the player their initial set of cards.
     * </p>
     */
    void takeHand();

    /**
     * Returns the list of cards currently in the player's hand.
     *
     * @return a list containing all cards in the player's hand
     */
    List<Card> getHand();
}