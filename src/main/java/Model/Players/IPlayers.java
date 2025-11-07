package Model.Players;

import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;

public interface IPlayers {
    void takeCard(Card card);
    void putCard(int IndexCard, CardPile cardPile);

    void takeHand(Deck deck);

}
