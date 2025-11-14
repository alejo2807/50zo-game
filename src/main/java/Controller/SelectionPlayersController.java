package Controller;

import View.GameWindow;
import View.SelectionPlayers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;

/**
 * Controller class for the player selection screen.
 * This controller manages the user interface for selecting the number of players
 * (including GPU opponents) before starting a game. It handles radio button
 * selections and initiates the game window with the chosen configuration.
 *
 * @author Juan-David-Brandon
 * @version 1.0
 * @since 2025
 */
public class SelectionPlayersController {

    /**
     * Toggle group that manages the mutual exclusivity of player count radio buttons.
     */
    @FXML
    private ToggleGroup selectionPlayers;

    /**
     * Radio button for selecting a 2-player game (1 human + 1 GPU).
     */
    @FXML
    private RadioButton twoPlayersButton;

    /**
     * Radio button for selecting a 3-player game (1 human + 2 GPU).
     */
    @FXML
    private RadioButton threePlayersButton;

    /**
     * Radio button for selecting a 4-player game (1 human + 3 GPU).
     */
    @FXML
    private RadioButton fourPlayersButton;

    /**
     * Button that triggers the game start when clicked.
     */
    @FXML
    private Button playButton;

    /**
     * Initializes the controller after the FXML elements have been loaded.
     * Sets up user data for each radio button representing the total number
     * of players (human + GPU), and selects the 3-player option as default.
     */
    @FXML
    public void initialize() {
        // Store the number of players in each RadioButton
        twoPlayersButton.setUserData(2);
        threePlayersButton.setUserData(3);
        fourPlayersButton.setUserData(4);

        // Select 3 players by default
        threePlayersButton.setSelected(true);
    }

    /**
     * Handles the play button click event.
     * Retrieves the selected number of players from the toggle group
     * and initiates the game with the chosen configuration.
     */
    @FXML
    private void onPlayClick() {
        // Get the selected number of players
        int numPlayers = (int) selectionPlayers.getSelectedToggle().getUserData();

        System.out.println("Iniciando juego con " + numPlayers + " jugadores");

        // Start the game with selected players
        startGame(numPlayers);
    }

    /**
     * Starts the game with the specified number of total players.
     * Closes the player selection window and opens the game window.
     * The number of GPU players is calculated as (numPlayers - 1) since
     * one player is always the human player.
     *
     * @param numPlayers the total number of players including the human player
     */
    private void startGame(int numPlayers) {
        try {
            // Close the selection window from the button
            playButton.getScene().getWindow().hide();

            GameWindow.getInstance(numPlayers-1).show();

        } catch (IOException e) {
            System.err.println("Error al cargar la ventana del juego: " + e.getMessage());
            e.printStackTrace();
        }
    }
}