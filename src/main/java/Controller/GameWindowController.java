package Controller;

import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;
import Model.Players.PlayerHuman;
import Model.Players.TurnManager;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.awt.event.MouseEvent;
import java.util.List;

public class GameWindowController {
    @FXML private ImageView card1, card2, card3, card4;
    private List<Card> playerCards;
    @FXML private HBox playerHand;


    Deck deck = new Deck();
    TurnManager turnManager = new TurnManager();
    Object lock = new Object();
    CardPile cardPile = new CardPile();
    PlayerHuman playerHuman = new PlayerHuman(deck, 1, lock,turnManager, cardPile);
    @FXML
    private void handleCardClick(MouseEvent event) {
        ImageView clickedImage = (ImageView) event.getSource();
        int index = playerHand.getChildren().indexOf(clickedImage);
        Card selectedCard = playerHuman.getCards().get(index);
        System.out.println(selectedCard.getSymbol() + " " + selectedCard.getValue());
    }

}
