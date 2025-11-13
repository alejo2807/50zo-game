package Controller;

import View.SelectionPlayers;
import View.StartWindow;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

/**
 * Controller class for the main start window of the application.
 * This controller manages the initial menu interface, handling navigation
 * to the player selection screen and application exit functionality.
 * It serves as the entry point for user interaction with the game.
 *
 * @author Juan-David-Brandon
 * @version 1.0
 * @since 2025
 */
public class StartWindowController {

    /**
     * Button that exits the application when clicked.
     */
    @FXML
    private Button mainMenuExitButton;

    /**
     * Button that navigates to the player selection screen when clicked.
     */
    @FXML
    private Button mainMenuPlayButton;

    /**
     * Handles the exit button click event in the main menu.
     * Closes the JavaFX application and terminates the JVM process.
     * This ensures a complete shutdown of the application.
     *
     * @param event the action event triggered by clicking the exit button
     */
    @FXML
    void onMainMenuExitClick(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Handles the play button click event in the main menu.
     * Closes the start window and opens the player selection window,
     * allowing the user to choose the number of players for the game.
     * If an error occurs while loading the selection window, it is logged
     * to the standard error output.
     *
     * @param event the action event triggered by clicking the play button
     */
    @FXML
    void onMainMenuPlayClick(ActionEvent event) {

        try {
            // Close the initial menu
            StartWindow.closeInstance();

            // Open the player selection window
            SelectionPlayers.getInstance().show();

        } catch (IOException e) {
            System.err.println("Error al cargar la selección de jugadores: " + e.getMessage());
            e.printStackTrace();
        }
    }
}