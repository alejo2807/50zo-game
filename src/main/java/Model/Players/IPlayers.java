package Model.Players;

import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;
import Model.Exceptions.InvalidCardException;

import java.util.List;

public interface IPlayers {
    void takeCard(Card card);
    void putCard(int IndexCard, CardPile cardPile) throws InvalidCardException;

    void takeHand();
    List<Card> getHand();

}
