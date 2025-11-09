package Controller;

import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;
import Model.Cards.RechargeDeck;
import Model.Players.PlayerGPU;
import Model.Players.PlayerHuman;
import Model.Players.TurnManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class GameWindowController {

    @FXML private HBox playerCards, cardsGPU1, cardsGPU2, cardsGPU3;
    @FXML private ImageView cardPile; // Imagen de la pila en el centro
    private RechargeDeck rechargeDeck;
    private Deck deck;
    private TurnManager turnManager;
    private Object lock;
    private CardPile pile;
    private PlayerHuman playerHuman;
    private PlayerGPU playerGPU1, playerGPU2, playerGPU3;
    private List<PlayerGPU> playerGPUList;

    private boolean cardPlayed = false; // control de flujo humano

    @FXML
    public void initialize() {
        deck = new Deck();
        turnManager = new TurnManager();
        lock = new Object();
        pile = new CardPile(deck);
        rechargeDeck = new RechargeDeck(deck, pile);
        rechargeDeck.start();
        // Crear jugadores
        playerHuman = new PlayerHuman(deck, 1, lock, turnManager, pile);
        playerGPU1 = new PlayerGPU(deck, 2, lock, turnManager, pile, this);
        playerGPU2 = new PlayerGPU(deck, 3, lock, turnManager, pile, this);
        playerGPU3 = new PlayerGPU(deck, 4, lock, turnManager, pile, this);

        // Inicializar manos
        playerHuman.initializePlayer();
        playerGPU1.initializePlayer();
        playerGPU2.initializePlayer();
        playerGPU3.initializePlayer();

        playerGPUList = List.of(playerGPU1, playerGPU2, playerGPU3);

        printCardsHuman();
        printCardsGPU();
        updatePileImage(pile.getTopCard());

        // Iniciar los hilos
        playerHuman.start();
        playerGPU1.start();
        playerGPU2.start();
        playerGPU3.start();

    }

    // ------------------------------------------------------------
    // 🂠 Muestra las cartas del jugador humano
    void printCardsHuman() {
        for (int i = 0; i < 4; i++) {
            ImageView imageView = (ImageView) playerCards.getChildren().get(i);
            if (i < playerHuman.getHand().size()) {
                Image image = new Image(getClass().getResourceAsStream(playerHuman.getHand().get(i).getUrl()));
                imageView.setImage(image);
            } else {
                imageView.setImage(null);
            }
        }
    }

    // ------------------------------------------------------------
    // 🂡 Muestra el reverso de las cartas de las GPUs activas
   public void printCardsGPU() {
        List<HBox> boxes = List.of(cardsGPU1, cardsGPU2, cardsGPU3);
        Image backImage = new Image(getClass().getResourceAsStream("/deck/back_red.png"));

        for (int i = 0; i < playerGPUList.size(); i++) {
            HBox box = boxes.get(i);
            PlayerGPU player = playerGPUList.get(i);

            for (int j = 0; j < 4; j++) {
                ImageView iv = (ImageView) box.getChildren().get(j);
                if (player.getIsplaying()) {
                    iv.setImage(backImage);
                } else {
                    iv.setImage(null);
                }
            }
        }
    }

    // ------------------------------------------------------------
    // 🖱 Evento de clic en carta del jugador humano
    @FXML
    void handleCardClick(MouseEvent event) {
        if (turnManager.getActualTurn() != playerHuman.getTurn()) return; // No es su turno
        if (cardPlayed) return; // Ya jugó

        ImageView clickedImage = (ImageView) event.getSource();
        int index = playerCards.getChildren().indexOf(clickedImage);
        playerHuman.setIndexCard(index);

        // 🃏 Poner la carta en la pila
        playerHuman.putCard(index, pile);
        cardPlayed = true;

        // Mostrar la carta superior en la pila
        Card top = pile.getTopCard();
        if (top != null) {
            Image pileImage = new Image(getClass().getResourceAsStream(top.getUrl()));
            cardPile.setImage(pileImage);
            valuePile.setText(String.valueOf(pile.getValuePile()));
        }

        // Actualizar mano
        printCardsHuman();
    }

    // ------------------------------------------------------------
    // 🃏 Botón para tomar carta y pasar turno
    @FXML
    void takeCard(ActionEvent event) {
        System.out.println(deck.getDeck());
        if (!cardPlayed) {
            System.out.println("takeCard() ignorado: aún no se ha jugado carta.");
            return; // Solo si ya jugó
        }

        Card newCard = deck.getCard();
        if (newCard == null) {
            System.out.println("takeCard(): el mazo está vacío.");
            return;
        }

        playerHuman.takeCard(newCard);
        System.out.println("takeCard(): robó -> " + newCard.getSymbol() + " valor=" + newCard.getValue());
        printCardsHuman();

        // pasa turno y notifica
        synchronized (lock) {
            turnManager.passTurn(); // Pasa el turno al siguiente
            cardPlayed = false;
            lock.notifyAll(); // Despierta los demás hilos
        }
    }

    public void updatePileImage(Card topCard) {
        if (topCard != null) {
            Image image = new Image(getClass().getResourceAsStream(topCard.getUrl()));
            cardPile.setImage(image);
        }
        valuePile.setText(String.valueOf(pile.getValuePile()));
    }
    @FXML private Label valuePile;
}
