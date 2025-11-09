package Model.Players;

import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;

import java.util.List;

public interface IPlayers {
    void takeCard(Card card);
    void putCard(int IndexCard, CardPile cardPile);

    void takeHand();
    List<Card> getHand();

}
