package Controller;

import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;
import Model.Cards.RechargeDeck;
import Model.Exceptions.InvalidCardException;
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
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

public class GameWindowController {

    @FXML private HBox playerCards, cardsGPU1, cardsGPU2, cardsGPU3;
    @FXML private ImageView cardPile; // Imagen de la pila en el centro
    @FXML private Label valuePile;
    @FXML
    private ImageView deckImg;

    @FXML
    private StackPane deckStack;

    private RechargeDeck rechargeDeck;
    private Deck deck;
    private TurnManager turnManager;
    private Object lock;
    private CardPile pile;
    private PlayerHuman playerHuman;
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

        playerHuman = new PlayerHuman(deck, 1, lock, turnManager, pile);
        playerHuman.initializePlayer();

        playerGPUList = new ArrayList<>();
        for(int i=2;i<= turnManager.getTotalPlayers(); i++){
            PlayerGPU gpu = new PlayerGPU(deck, i,lock,turnManager, pile, this);
            gpu.initializePlayer();
            playerGPUList.add(gpu);
        }

        printCardsHuman();
        printCardsGPU();
        updatePileImage(pile.getTopCard());

        // Arrancan los hilos PERO el turno aún no está activo
        playerHuman.start();
        for(PlayerGPU gpu : playerGPUList){
            gpu.start();
        }

        // Inicia oficialmente el turno 1
        synchronized (lock) {
            turnManager.startGame();
            lock.notifyAll();
        }
    }

    // ------------------------------------------------------------
    // Muestra las cartas del jugador humano
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
    // Muestra el reverso de las cartas de las GPUs activas
    public void printCardsGPU() {
        List<HBox> boxes = List.of(cardsGPU1, cardsGPU2, cardsGPU3);
        Image backImage = new Image(getClass().getResourceAsStream("/deck/back_red.png"));

        //Este for limpia todas las imágenes que se hayan podido haber creado
        for (int i = 0; i < boxes.size(); i++) {
            HBox box = boxes.get(i);
            for (int j = 0; j < 4; j++) {
                ImageView iv = (ImageView) box.getChildren().get(j);
                iv.setImage(null);
            }

            //Luego de eliminarlas hace que la GPU que esté en la posición i genere las cartas
            if(i<playerGPUList.size()){
                PlayerGPU player = playerGPUList.get(i);
                if(player.getIsplaying()){
                    for(int j = 0; j < 4; j++){
                        ImageView iv = (ImageView) box.getChildren().get(j);
                        iv.setImage(backImage);
                    }
                }
            }
        }
    }

    // ------------------------------------------------------------
    // Evento de clic en carta del jugador humano
    @FXML
    void handleCardClick(MouseEvent event) {
        if (turnManager.getActualTurn() != playerHuman.getTurn()) {
            System.out.println("⚠️ No es tu turno.");
            return;
        }

        if (cardPlayed) {
            System.out.println("⚠️ Ya jugaste una carta este turno.");
            return;
        }

        ImageView clickedImage = (ImageView) event.getSource();
        int index = playerCards.getChildren().indexOf(clickedImage);

        // Verificar que el índice sea válido
        if (index < 0 || index >= playerHuman.getHand().size()) {
            System.out.println("⚠️ Carta no válida.");
            return;
        }

        playerHuman.setIndexCard(index);

        try {
            // Intentar jugar la carta
            playerHuman.putCard(index, pile);
            cardPlayed = true;

            Card top = pile.getTopCard();
            if (top != null) {
                Image pileImage = new Image(getClass().getResourceAsStream(top.getUrl()));
                cardPile.setImage(pileImage);
                valuePile.setText(String.valueOf(pile.getValuePile()));
            }

            printCardsHuman();
            System.out.println("✅ Jugaste: " + top.getSymbol() + " | Nuevo valor pila: " + pile.getValuePile());

        } catch (InvalidCardException e) {
            // Carta inválida - mostrar mensaje y no hacer nada
            System.out.println(e.getDetailedMessage());
            // La carta NO se jugó, el jugador debe elegir otra
        }
    }

    // ------------------------------------------------------------
    // Botón para tomar carta y pasar turno
    @FXML
    void takeCard(ActionEvent event) {
        System.out.println("Mazo actual: " + deck.getDeck().size() + " cartas");

        // Verificar que sea el turno del humano
        if (turnManager.getActualTurn() != playerHuman.getTurn()) {
            System.out.println("⚠️ No es tu turno.");
            return;
        }

        if (!cardPlayed) {
            System.out.println("⚠️ Debes jugar una carta antes de tomar una nueva.");
            return;
        }

        Card newCard = deck.getCard();
        if (newCard == null) {
            System.out.println("⚠️ El mazo está vacío.");
            return;
        }

        playerHuman.takeCard(newCard);
        System.out.println("✅ Robaste: " + newCard.getSymbol() + " (valor=" + newCard.getValue() + ")");
        printCardsHuman();

        // Finaliza el turno del jugador humano
        synchronized (lock) {
            cardPlayed = false;         // Resetear para el próximo turno
            playerHuman.finishTurn();   // Notificar al hilo que terminó
            turnManager.passTurn();     // Cambiar al siguiente jugador
            lock.notifyAll();           // Despertar todos los hilos
        }

        System.out.println("🔄 Turno pasado a jugador " + turnManager.getActualTurn());
    }

    // ------------------------------------------------------------
    // Actualiza la imagen de la pila
    public void updatePileImage(Card topCard) {
        if (topCard != null) {
            Image image = new Image(getClass().getResourceAsStream(topCard.getUrl()));
            cardPile.setImage(image);
        }
        valuePile.setText(String.valueOf(pile.getValuePile()));
    }

    public void updateDeckVisual() {
        if (deckStack.getChildren().size() > 1) {
            deckStack.getChildren().remove(deckStack.getChildren().size() - 1);
        }
    }

}