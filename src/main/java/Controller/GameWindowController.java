package Controller;

import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;
import Model.Cards.RechargeDeck;
import Model.Exceptions.InvalidCardException;
import Model.Players.PlayerGPU;
import Model.Players.PlayerHuman;
import Model.Players.TurnManager;
import View.GameWindow;
import View.Messages;
import View.SelectionPlayers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;
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

    @FXML private Label labelPlayer, labelBot1, labelBot2, labelBot3;
    @FXML private Label turnLabel;

    private RechargeDeck rechargeDeck;
    private Deck deck;
    private TurnManager turnManager;
    private Object lock;
    private CardPile pile;
    private PlayerHuman playerHuman;
    private PlayerGPU playerGPU;
    private List<PlayerGPU> playerGPUList;
    private int totalPlayersGPU ;
    private boolean cardPlayed = false; // control de flujo humano

   /* public GameWindowController(int totalPlayersGPU) {
        this.totalPlayersGPU = totalPlayersGPU;
    }*/
    public void getPlayersGPU(int totalPlayersGPU){
        this.totalPlayersGPU = totalPlayersGPU;
    }
    @FXML
    public void initialize() throws  IOException {
        deck = new Deck();
        turnManager = new TurnManager(totalPlayersGPU+1);//porque mas 1? porque el turnmanager tiene en cuenta el turno del jugador, osea, si son 2 gpu es 1 jugador, entonces son 3 turnos

        lock = new Object();
        // Inicia oficialmente el turno 1
        synchronized (lock) {
            turnManager.startGame();
            lock.notifyAll();
        }
        pile = new CardPile(deck);
        rechargeDeck = new RechargeDeck(deck, pile);
        rechargeDeck.start();
        playerGPUList = new ArrayList<>();
        playerHuman = new PlayerHuman(deck, 1, lock, turnManager, pile);//se inicializa el jugador
        playerHuman.initializePlayer();
        for(int i =2; i <= totalPlayersGPU+1; i++){
            playerGPU = new PlayerGPU(deck, i, lock, turnManager, pile, this);
            playerGPUList.add(playerGPU);
            playerGPU.initializePlayer();
        }
        /*
        playerGPU2 = new PlayerGPU(deck, 3, lock, turnManager, pile, this);
        playerGPU3 = new PlayerGPU(deck, 4, lock, turnManager, pile, this);
        playerGPUList = List.of(playerGPU, playerGPU2, playerGPU3);
         Todos los gpui inician con isPlaying en falso y sin cartas, y todos los objtetos se ponen en la lista playerGPYList
        * Cual es la idea? primero como notas*/

       // playerGPU1.initializePlayer();
       // playerGPU2.initializePlayer();
       // playerGPU3.initializePlayer();
        printCardsHuman();
        printCardsGPU();
        updatePileImage(pile.getTopCard());

        //Here we are changing in a dynamic way the labels of the players
        configureLabelVisibility();
        updateTurnLabel();



        // Arrancan los hilos PERO el turno aún no está activo
        playerHuman.start();
        for(PlayerGPU playerGPU : playerGPUList){
            playerGPU.start();
        }
       // playerGPU1.start();
       // playerGPU2.start();
       // playerGPU3.start();
        printCardsGPU();
       // popFinalMessage();
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

        for (int i = 0; i < playerGPUList.size(); i++) {
            HBox box = boxes.get(i);
            PlayerGPU playerGPU = playerGPUList.get(i);

            // Si el jugador está jugando
            if (playerGPU.getIsplaying()) {
                // Aseguramos que el HBox tenga 4 ImageViews
                if (box.getChildren().isEmpty()) {
                    for (int k = 0; k < 4; k++) {
                        ImageView iv = new ImageView(backImage);
                        iv.setFitWidth(60);
                        iv.setFitHeight(90);
                        box.getChildren().add(iv);
                    }
                } else {
                    // Si ya tiene hijos, simplemente actualizamos las imágenes
                    for (int j = 0; j < box.getChildren().size(); j++) {
                        ImageView iv = (ImageView) box.getChildren().get(j);
                        iv.setImage(backImage);
                    }
                }
            }
            // Si no está jugando
            else {
                box.getChildren().clear(); // elimina todos los hijos de una vez
            }
        }
        //this line will update the labels dynamically
        configureLabelVisibility();

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

        updateTurnLabel();
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


    private void configureLabelVisibility() {
        // Player siempre visible
        if (labelPlayer != null) {
            labelPlayer.setVisible(true);
        }

        // Lista de labels de bots
        List<Label> botLabels = List.of(labelBot1, labelBot2, labelBot3);

        // Configurar visibilidad según playerGPUList
        for (int i = 0; i < botLabels.size(); i++) {
            if (i < playerGPUList.size() && playerGPUList.get(i).getIsplaying()) {
                botLabels.get(i).setVisible(true);
            } else {
                botLabels.get(i).setVisible(false);
            }
        }
    }


    private void updateTurnLabel() {
        int currentTurn = turnManager.getActualTurn();

        if (currentTurn == playerHuman.getTurn()) {
            turnLabel.setText("Turno de: Player");
        } else {
            // Es turno de un bot (turno 2, 3, o 4 = Bot 1, 2, o 3)
            int botNumber = currentTurn - 1; // Turno 2 = Bot 1, Turno 3 = Bot 2, etc.
            turnLabel.setText("Turno de: Bot " + botNumber);
        }
    }

    public void notifyBotTurnChange() {
        javafx.application.Platform.runLater(() -> {
            updateTurnLabel();
            printCardsGPU(); // Actualizar cartas también
        });
    }
    @FXML
    Button backButton, closeButton;
    @FXML
    void back() throws IOException{
        GameWindow.destroyInstance();
        SelectionPlayers.getInstance().show();
    }
    @FXML
    void close() throws IOException {
        GameWindow.destroyInstance();
    }
}