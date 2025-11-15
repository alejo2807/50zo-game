package Controller;

import Model.Players.PlayerGPU;
import Model.Players.PlayerHuman;
import Model.Players.TurnManager;
import View.GameWindow;
import View.ShowWiner;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.List;

/**
 * Controller class responsible for managing the final winner dialog displayed
 * at the end of a match. This controller handles UI updates, winner message
 * configuration, and interaction logic such as closing or resuming the game.
 *
 * <p>The controller receives references to the players, turn manager, and
 * supporting UI elements. Once all required data is set, the method
 * {@link #setWiner()} must be called to properly display the winner text.</p>
 *
 * <p>This class is used together with the {@link ShowWiner} UI window.</p>
 *
 * @author Juan-David-Brandon
 * @version 1.0
 * @since 2025
 */
public class FinalMessageController {

    /**
     * Reference to the winner message window.
     */
    private ShowWiner showWiner;

    /**
     * Reference to the TurnManager managing turn order and remaining players.
     */
    private TurnManager turnManager;

    /**
     * Reference to the human player.
     */
    private PlayerHuman humanPlayer;

    /**
     * List containing all GPU-controlled players.
     */
    private List<PlayerGPU> playerGPUList;

    /**
     * Label used to display the winner message.
     */
    @FXML
    private Label winer;

    /**
     * Button used to close the message window.
     */
    @FXML
    private Button closeButton;

    /**
     * Default constructor.
     * Initializes a new instance of the controller.
     */
    public FinalMessageController() {}

    /**
     * Initializes the controller after its FXML components are loaded.
     * <p>No winner data is set here because required game information is not
     * yet available at this point. All winner-related logic must be invoked
     * manually once dependencies are injected.</p>
     */
    @FXML
    public void initialize() {
        // UI-only initialization. Logic must be called after external data is set.
    }

    /**
     * Sets the turn manager used to determine the winner.
     *
     * @param turnManager the TurnManager instance
     */
    public void setTurnManager(TurnManager turnManager) {
        this.turnManager = turnManager;
    }

    /**
     * Sets the human player reference.
     *
     * @param humanPlayer the PlayerHuman instance
     */
    public void setHumanPlayer(PlayerHuman humanPlayer) {
        this.humanPlayer = humanPlayer;
    }

    /**
     * Sets the list of GPU players used for identifying the winner.
     *
     * @param playerGPUList the list of PlayerGPU instances
     */
    public void setPlayerGPUList(List<PlayerGPU> playerGPUList) {
        this.playerGPUList = playerGPUList;
    }

    /**
     * Sets the {@link ShowWiner} window managed by this controller.
     *
     * @param showWiner the window instance displaying the winner
     */
    public void setMessages(ShowWiner showWiner) {
        this.showWiner = showWiner;
    }

    /**
     * Configures and displays the text that indicates which player won the game.
     * <p>This method MUST be called after all required game data
     * (TurnManager, humanPlayer, GPU list) have been assigned.</p>
     */
    public void setWiner() {
        int winnerTurn = turnManager.getTotalTurns().get(0);
        String winText;

        if (winnerTurn == humanPlayer.getTurn()) {
            winText = humanPlayer.getPlayerType() + " WIN!";
        } else {
            String gpuName = "Unknown";
            for (PlayerGPU playerGPU : playerGPUList) {
                if (winnerTurn == playerGPU.getTurn()) {
                    gpuName = playerGPU.getPlayerType();
                    break;
                }
            }
            winText = gpuName + " WIN!";
        }

        winer.setText(winText);
        winer.setVisible(true);
    }

    /**
     * Handles the action performed when the close button is pressed.
     * The behavior depends on the option selected in the winner window.
     *
     * @throws IOException if the game window cannot be accessed or modified
     */
    @FXML
    void closeWindow() throws IOException {
        if (showWiner.getOption() == 3) {
            turnMessage();
        } else {
            closeMessage();
        }
    }

    /**
     * Handles the scenario when the winner message should allow the game to continue.
     * Re-enables mouse interaction on the main game window.
     *
     * @throws IOException if the game window retrieval fails
     */
    void turnMessage() throws IOException {
        GameWindow window = GameWindow.getInstance(3);
        window.getScene().getRoot().setMouseTransparent(false);
        showWiner.close();
    }

    /**
     * Fully closes both the game window and the winner dialog.
     *
     * @throws IOException if the game window cannot be closed
     */
    void closeMessage() throws IOException {
        GameWindow.getInstance(3).close();
        showWiner.close();
    }
}
