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

/**
 * Main controller class for the game window interface.
 * This controller manages the game flow, player interactions, card displays,
 * and turn management for both human and GPU players. It handles the visual
 * representation of cards, the deck, and the central card pile, while
 * coordinating between multiple player threads.
 *
 * @author Juan-David-Brandon
 * @version 1.0
 * @since 2025
 */
public class GameWindowController {

    /**
     * HBox container displaying the human player's hand of cards.
     */
    @FXML private HBox playerCards;

    /**
     * HBox container displaying GPU player 1's cards.
     */
    @FXML private HBox cardsGPU1;

    /**
     * HBox container displaying GPU player 2's cards.
     */
    @FXML private HBox cardsGPU2;

    /**
     * HBox container displaying GPU player 3's cards.
     */
    @FXML private HBox cardsGPU3;

    /**
     * ImageView displaying the top card of the central pile.
     */
    @FXML private ImageView cardPile;

    /**
     * Label displaying the current numerical value of the card pile.
     */
    @FXML private Label valuePile;

    /**
     * ImageView displaying the deck of cards.
     */
    @FXML
    private ImageView deckImg;

    /**
     * StackPane container for the deck visual representation.
     */
    @FXML
    private StackPane deckStack;

    /**
     * Label displaying the human player's name or identifier.
     */
    @FXML private Label labelPlayer;

    /**
     * Label displaying Bot 1's name or identifier.
     */
    @FXML private Label labelBot1;

    /**
     * Label displaying Bot 2's name or identifier.
     */
    @FXML private Label labelBot2;

    /**
     * Label displaying Bot 3's name or identifier.
     */
    @FXML private Label labelBot3;

    /**
     * Label displaying which player's turn it currently is.
     */
    @FXML private Label turnLabel;

    /**
     * Recharge deck thread that manages deck replenishment from the discard pile.
     */
    private RechargeDeck rechargeDeck;

    /**
     * The main deck of cards used in the game.
     */
    private Deck deck;

    /**
     * Manager responsible for controlling turn order and progression.
     */
    private TurnManager turnManager;

    /**
     * Synchronization lock object for thread coordination between players.
     */
    private Object lock;

    /**
     * The central card pile where players place their cards.
     */
    private CardPile pile;

    /**
     * The human player instance.
     */
    private PlayerHuman playerHuman;

    /**
     * Reference to a GPU player instance (used during initialization loop).
     */
    private PlayerGPU playerGPU;

    /**
     * List containing all active GPU players in the game.
     */
    private List<PlayerGPU> playerGPUList;

    /**
     * Total number of GPU (bot) players in the game.
     */
    private int totalPlayersGPU;

    /**
     * Flag indicating whether the human player has played a card in the current turn.
     * Used to enforce turn flow control.
     */
    private boolean cardPlayed = false;

    /**
     * Sets the total number of GPU players for the game.
     *
     * @param totalPlayersGPU the number of GPU players to include in the game
     */
    public void getPlayersGPU(int totalPlayersGPU){
        this.totalPlayersGPU = totalPlayersGPU;
    }

    /**
     * Initializes the game controller and all game components.
     * This method sets up the deck, turn manager, players, card pile, and UI elements.
     * It creates and starts all player threads (human and GPU), initializes the discard pile,
     * and begins the game flow. The total number of turns is calculated as totalPlayersGPU + 1
     * to include the human player.
     *
     * @throws IOException if an I/O error occurs during initialization
     */
    @FXML
    public void initialize() throws IOException {
        deck = new Deck();
        turnManager = new TurnManager(totalPlayersGPU+1);

        lock = new Object();
        // Officially starts turn 1
        synchronized (lock) {
            turnManager.startGame();
            lock.notifyAll();
        }
        pile = new CardPile(deck);
        rechargeDeck = new RechargeDeck(deck, pile);
        rechargeDeck.start();
        playerGPUList = new ArrayList<>();
        playerHuman = new PlayerHuman(deck, 1, lock, turnManager, pile);
        playerHuman.initializePlayer();
        for(int i = 2; i <= totalPlayersGPU+1; i++){
            playerGPU = new PlayerGPU(deck, i, lock, turnManager, pile, this);
            playerGPUList.add(playerGPU);
            playerGPU.initializePlayer();
        }

        printCardsHuman();
        printCardsGPU();
        updatePileImage(pile.getTopCard());

        // Dynamically configure player labels visibility
        configureLabelVisibility();
        updateTurnLabel();

        // Start all player threads
        playerHuman.start();
        for(PlayerGPU playerGPU : playerGPUList){
            playerGPU.start();
        }
        printCardsGPU();
    }

    /**
     * Displays the human player's cards in the UI.
     * Updates the ImageView elements in the playerCards HBox with the corresponding
     * card images from the player's hand. If the hand has fewer than 4 cards,
     * the remaining ImageViews are set to null.
     */
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

    /**
     * Displays the back side of cards for all active GPU players.
     * For each active GPU player, this method creates or updates 4 ImageViews
     * showing the card back image. If a GPU player is not currently playing,
     * their card container is cleared.
     */
    public void printCardsGPU() {
        List<HBox> boxes = List.of(cardsGPU1, cardsGPU2, cardsGPU3);
        Image backImage = new Image(getClass().getResourceAsStream("/deck/back_red.png"));

        for (int i = 0; i < playerGPUList.size(); i++) {
            HBox box = boxes.get(i);
            PlayerGPU playerGPU = playerGPUList.get(i);

            // If the player is currently playing
            if (playerGPU.getIsplaying()) {
                // Ensure the HBox has 4 ImageViews
                if (box.getChildren().isEmpty()) {
                    for (int k = 0; k < 4; k++) {
                        ImageView iv = new ImageView(backImage);
                        iv.setFitWidth(60);
                        iv.setFitHeight(90);
                        box.getChildren().add(iv);
                    }
                } else {
                    // Update existing ImageViews
                    for (int j = 0; j < box.getChildren().size(); j++) {
                        ImageView iv = (ImageView) box.getChildren().get(j);
                        iv.setImage(backImage);
                    }
                }
            }
            // If not playing, clear all cards
            else {
                box.getChildren().clear();
            }
        }
        // Update labels dynamically
        configureLabelVisibility();
    }

    /**
     * Handles mouse click events on the human player's cards.
     * This method validates that it's the player's turn and that they haven't already
     * played a card this turn. It then attempts to play the selected card on the pile.
     * If the card is valid, it updates the pile display and the player's hand.
     * If invalid, an error message is displayed and the player must choose another card.
     *
     * @param event the mouse event triggered by clicking on a card
     */
    @FXML
    void handleCardClick(MouseEvent event) {
        if (turnManager.getActualTurn() != playerHuman.getTurn()) {
            System.out.println("⚠ No es tu turno.");
            return;
        }

        if (cardPlayed) {
            System.out.println("⚠ Ya jugaste una carta este turno.");
            return;
        }

        ImageView clickedImage = (ImageView) event.getSource();
        int index = playerCards.getChildren().indexOf(clickedImage);

        // Verify the index is valid
        if (index < 0 || index >= playerHuman.getHand().size()) {
            System.out.println("⚠ Carta no válida.");
            return;
        }

        playerHuman.setIndexCard(index);

        try {
            // Attempt to play the card
            playerHuman.putCard(index, pile);
            cardPlayed = true;

            Card top = pile.getTopCard();
            if (top != null) {
                Image pileImage = new Image(getClass().getResourceAsStream(top.getUrl()));
                cardPile.setImage(pileImage);
                valuePile.setText(String.valueOf(pile.getValuePile()));
            }

            printCardsHuman();
            System.out.println(" Jugaste: " + top.getSymbol() + " | Nuevo valor pila: " + pile.getValuePile());

        } catch (InvalidCardException e) {
            // Invalid card - show message and do nothing
            System.out.println(e.getDetailedMessage());
            // The card was NOT played, player must choose another
        }
    }

    /**
     * Handles the action of taking a card from the deck and passing the turn.
     * This method is triggered by a button click and validates that it's the player's turn
     * and that they have already played a card. If valid, the player draws a new card,
     * their turn is finalized, and the turn passes to the next player.
     *
     * @param event the action event triggered by the take card button
     */
    @FXML
    void takeCard(ActionEvent event) {
        System.out.println("Mazo actual: " + deck.getDeck().size() + " cartas");

        // Verify it's the human's turn
        if (turnManager.getActualTurn() != playerHuman.getTurn()) {
            System.out.println("⚠ No es tu turno.");
            return;
        }

        if (!cardPlayed) {
            System.out.println("⚠ Debes jugar una carta antes de tomar una nueva.");
            return;
        }

        Card newCard = deck.getCard();
        if (newCard == null) {
            System.out.println("⚠ El mazo está vacío.");
            return;
        }

        playerHuman.takeCard(newCard);
        System.out.println(" Robaste: " + newCard.getSymbol() + " (valor=" + newCard.getValue() + ")");
        printCardsHuman();

        // Finalize the human player's turn
        synchronized (lock) {
            cardPlayed = false;         // Reset for next turn
            playerHuman.finishTurn();   // Notify the thread that it's done
            turnManager.passTurn();     // Move to next player
            lock.notifyAll();           // Wake up all threads
        }

        updateTurnLabel();
        System.out.println(" Turno pasado a jugador " + turnManager.getActualTurn());
    }

    /**
     * Updates the visual representation of the card pile with the top card.
     * Sets the pile ImageView to display the provided card and updates the
     * value label with the current pile value.
     *
     * @param topCard the card currently on top of the pile
     */
    public void updatePileImage(Card topCard) {
        if (topCard != null) {
            Image image = new Image(getClass().getResourceAsStream(topCard.getUrl()));
            cardPile.setImage(image);
        }
        valuePile.setText(String.valueOf(pile.getValuePile()));
    }

    /**
     * Updates the visual representation of the deck by removing the top layer.
     * This method removes the last visual element from the deck stack to simulate
     * cards being drawn from the deck.
     */
    public void updateDeckVisual() {
        if (deckStack.getChildren().size() > 1) {
            deckStack.getChildren().remove(deckStack.getChildren().size() - 1);
        }
    }

    /**
     * Configures the visibility of player labels based on active players.
     * The human player label is always visible, while bot labels are only
     * visible if the corresponding GPU player is active and currently playing.
     */
    private void configureLabelVisibility() {
        // Player always visible
        if (labelPlayer != null) {
            labelPlayer.setVisible(true);
        }

        // List of bot labels
        List<Label> botLabels = List.of(labelBot1, labelBot2, labelBot3);

        // Configure visibility according to playerGPUList
        for (int i = 0; i < botLabels.size(); i++) {
            if (i < playerGPUList.size() && playerGPUList.get(i).getIsplaying()) {
                botLabels.get(i).setVisible(true);
            } else {
                botLabels.get(i).setVisible(false);
            }
        }
    }

    /**
     * Updates the turn label to display the current player's turn.
     * Shows "Player" for the human player's turn or "Bot N" for GPU players,
     * where N is the bot number (1, 2, or 3).
     */
    private void updateTurnLabel() {
        int currentTurn = turnManager.getActualTurn();

        if (currentTurn == playerHuman.getTurn()) {
            turnLabel.setText("Turno de: Player");
        } else {
            // Bot's turn (turn 2, 3, or 4 = Bot 1, 2, or 3)
            int botNumber = currentTurn - 1; // Turn 2 = Bot 1, Turn 3 = Bot 2, etc.
            turnLabel.setText("Turno de: Bot " + botNumber);
        }
    }

    /**
     * Notifies the UI of a bot turn change and updates the display accordingly.
     * This method is called from GPU player threads and uses Platform.runLater
     * to ensure UI updates occur on the JavaFX application thread.
     */
    public void notifyBotTurnChange() {
        javafx.application.Platform.runLater(() -> {
            updateTurnLabel();
            printCardsGPU(); // Also update cards
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