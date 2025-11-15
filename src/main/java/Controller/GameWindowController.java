package Controller;

import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;
import Model.Cards.RechargeDeck;
import Model.Exceptions.InvalidCardException;
import Model.Players.PlayerGPU;
import Model.Players.PlayerHuman;
import Model.Players.TakeWiner;
import Model.Players.TurnManager;
import View.GameWindow;
import View.SelectionPlayers;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
     * object that take the player winer
     */

    private TakeWiner takeWiner;
    /**
     * Sets the total number of GPU players for the game.
     *
     * @param totalPlayersGPU number of GPU players
     */
    public void getPlayersGPU(int totalPlayersGPU){
        this.totalPlayersGPU = totalPlayersGPU;
    }

    /**
     * Initializes the game environment, including the deck, turn manager,
     * players, pile, UI components, and player threads. This method prepares
     * the entire interface and logic required for gameplay.
     *
     * @throws IOException if an error occurs during resource loading
     */
    @FXML
    public void initialize() throws IOException {
        deck = new Deck();
        turnManager = new TurnManager(totalPlayersGPU + 1);
        lock = new Object();

        synchronized (lock) {
            turnManager.startGame();
            lock.notifyAll();
        }

        pile = new CardPile(deck);
        rechargeDeck = new RechargeDeck(deck, pile);
        rechargeDeck.start();

        playerGPUList = new ArrayList<>();
        playerHuman = new PlayerHuman(deck, 1, lock, turnManager, pile, "YOU");
        playerHuman.initializePlayer();

        for (int i = 2; i <= totalPlayersGPU + 1; i++) {
            playerGPU = new PlayerGPU(deck, i, lock, turnManager, pile, this, "GPU " + (i - 1));
            playerGPUList.add(playerGPU);
            playerGPU.initializePlayer();
        }

        takeWiner = new TakeWiner(turnManager, lock, playerHuman, playerGPUList);
        takeWiner.start();

        printCardsHuman();
        printCardsGPU();
        updatePileImage(pile.getTopCard());
        configureLabelVisibility();
        updateTurnLabel();

        playerHuman.start();
        for (PlayerGPU g : playerGPUList) {
            g.start();
        }
    }

    /**
     * Displays the human player's hand on the UI.
     * If the player has fewer than 4 cards, empty slots are shown.
     */
    public void printCardsHuman() {
        if (playerHuman.getIsPlaying()) {

            for (int i = 0; i < 4; i++) {
                ImageView imageView = (ImageView) playerCards.getChildren().get(i);
                if (i < playerHuman.getHand().size()) {
                    Image image = new Image(getClass().getResourceAsStream(playerHuman.getHand().get(i).getUrl()));
                    imageView.setImage(image);
                } else {
                    imageView.setImage(null);
                }
            }

        } else {
            playerCards.getChildren().clear();
        }
    }

    /**
     * Displays the back image for each GPU player's cards.
     * If a GPU player is out of the game, their display is cleared.
     */
    public void printCardsGPU() {
        List<HBox> boxes = List.of(cardsGPU1, cardsGPU2, cardsGPU3);
        Image backImage = new Image(getClass().getResourceAsStream("/deck/back_red.png"));

        for (int i = 0; i < playerGPUList.size(); i++) {
            HBox box = boxes.get(i);
            PlayerGPU gpu = playerGPUList.get(i);

            if (gpu.getIsplaying()) {
                if (box.getChildren().isEmpty()) {
                    for (int k = 0; k < 4; k++) {
                        ImageView iv = new ImageView(backImage);
                        iv.setFitWidth(60);
                        iv.setFitHeight(90);
                        box.getChildren().add(iv);
                    }
                } else {
                    for (Node child : box.getChildren()) {
                        ((ImageView) child).setImage(backImage);
                    }
                }
            } else {
                box.getChildren().clear();
            }
        }

        configureLabelVisibility();
    }

    /**
     * Hides the human player's cards when they are eliminated.
     */
    public void deleteCardsPlayer(){
        playerCards.setVisible(false);
    }

    /**
     * Notifies the UI that the human player has been eliminated.
     * Updates label style and hides their cards.
     */
    public void notifyHumanEliminated() {
        Platform.runLater(() -> {
            deleteCardsPlayer();
            if (labelPlayer != null) {
                labelPlayer.setText("Player ✖");
                labelPlayer.setStyle("-fx-text-fill: #FF0000;");
            }
            updateTurnLabel();
        });
    }

    /**
     * Handles a mouse click on a human player's card. Ensures the turn is valid
     * and the card has not been played yet. Attempts to play the selected card
     * and updates the pile and UI.
     *
     * @param event click event on a card
     */
    @FXML
    void handleCardClick(MouseEvent event) {
        if (turnManager.getActualTurn() != playerHuman.getTurn()) return;
        if (cardPlayed) return;

        ImageView clicked = (ImageView) event.getSource();
        int index = playerCards.getChildren().indexOf(clicked);

        if (index < 0 || index >= playerHuman.getHand().size()) return;

        playerHuman.setIndexCard(index);

        try {
            playerHuman.putCard(index, pile);
            cardPlayed = true;

            Card top = pile.getTopCard();
            if (top != null) {
                Image pileImage = new Image(getClass().getResourceAsStream(top.getUrl()));
                cardPile.setImage(pileImage);
                valuePile.setText(String.valueOf(pile.getValuePile()));
            }

            printCardsHuman();

        } catch (InvalidCardException e) {
            // Invalid card — silently ignored
        }
    }

    /**
     * Handles the action of taking a new card from the deck and passing the turn.
     *
     * @param event button click event
     */
    @FXML
    void takeCard(ActionEvent event) {
        if (turnManager.getActualTurn() != playerHuman.getTurn()) return;
        if (!cardPlayed) return;

        Card newCard = deck.getCard();
        if (newCard == null) return;

        playerHuman.takeCard(newCard);
        printCardsHuman();

        synchronized (lock) {
            cardPlayed = false;
            playerHuman.finishTurn();
            turnManager.passTurn();
            lock.notifyAll();
        }

        updateTurnLabel();
    }

    /**
     * Updates the pile display with the current top card.
     *
     * @param topCard the top card of the pile
     */
    public void updatePileImage(Card topCard) {
        if (topCard != null) {
            Image img = new Image(getClass().getResourceAsStream(topCard.getUrl()));
            cardPile.setImage(img);
        }
        valuePile.setText(String.valueOf(pile.getValuePile()));
    }

    /**
     * Updates the visual representation of the deck by removing one layer from the stack.
     */
    public void updateDeckVisual() {
        if (deckStack.getChildren().size() > 1) {
            deckStack.getChildren().remove(deckStack.getChildren().size() - 1);
        }
    }

    /**
     * Shows only the labels of players currently in the game.
     */
    private void configureLabelVisibility() {
        if (labelPlayer != null) labelPlayer.setVisible(true);

        List<Label> botLabels = List.of(labelBot1, labelBot2, labelBot3);

        for (int i = 0; i < botLabels.size(); i++) {
            if (i < playerGPUList.size() && playerGPUList.get(i).getIsplaying()) {
                botLabels.get(i).setVisible(true);
            } else {
                botLabels.get(i).setVisible(false);
            }
        }
    }

    /**
     * Updates the UI label to indicate whose turn it currently is.
     */
    private void updateTurnLabel() {
        int currentTurn = turnManager.getActualTurn();

        if (currentTurn == playerHuman.getTurn()) {
            turnLabel.setText("Your turn :)");
        } else {
            int botNumber = currentTurn - 1;
            turnLabel.setText("Turn: Bot " + botNumber);
        }
    }

    /**
     * Notifies the UI that a bot turn has begun.
     * Updates its cards and the turn label.
     */
    public void notifyBotTurnChange() {
        Platform.runLater(() -> {
            updateTurnLabel();
            printCardsGPU();
        });
    }

    @FXML Button backButton, closeButton;

    /**
     * Returns to the player selection window.
     *
     * @throws IOException if the window cannot be reopened
     */
    @FXML
    void back() throws IOException{
        GameWindow.destroyInstance();
        SelectionPlayers.getInstance().show();
    }

    /**
     * Closes the entire game window.
     *
     * @throws IOException if the window fails to close
     */
    @FXML
    void close() throws IOException {
        GameWindow.destroyInstance();
    }
}