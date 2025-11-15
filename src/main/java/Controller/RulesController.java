package Controller;

import View.RulesWindow;
import View.StartWindow;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import java.io.IOException;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Controller responsible for handling interactions within the Rules window.
 * This class manages navigation actions such as returning to the player selection
 * or start window, ensuring smooth UI transitions.
 *
 * <p>The controller is linked to the RulesWindow FXML layout and responds to
 * user actions via JavaFX event handling.</p>
 *
 * @author Juan-David-Brandon
 * @version 1.0
 * @since 2025
 */
public class RulesController {

    /**
     * Button used to return to the previous window.
     */
    @FXML
    private Button backButton;
    /**
     * Handles the action executed when the user presses the
     * "Back" button inside the Rules window.
     *
     * <p>This method closes the RulesWindow instance and opens the
     * StartWindow. Both windows are managed as singletons to avoid
     * creating duplicate UI instances.</p>
     *
     * @throws RuntimeException if the transition to the StartWindow fails
     */
    @FXML
    void backToStartWindow() {
        try {
            // Close the rules window instance (Singleton)
            RulesWindow.closeInstance();

            // Show the Start Window (Singleton)
            StartWindow.getInstance().show();

        } catch (IOException e) {
            // Re-throw as unchecked to avoid console output and maintain clean UI
            throw new RuntimeException("Unable to return to the selection window.", e);
        }
    }

    /**
     * Initializes UI components after the FXML file has been loaded.
     * <p>No special initialization is required for this controller,
     * but the method is kept for future extension if needed.</p>
     */
    @FXML
    public void initialize() {
        javafx.application.Platform.runLater(() -> {
            backButton.getScene().setOnKeyPressed(this::handleKeyPress);
        });

    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
            backToMenu();
        }
    }
    private void backToMenu() {
        try {
            RulesWindow.closeInstance(); // Cerrar con Singleton
            StartWindow.getInstance().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
