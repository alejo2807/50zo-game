package Controller;

import View.GameWindow;
import View.Messages;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

/**
 * Controller class responsible for managing the final message dialog and its interactions.
 * This controller handles the closing behavior of messages and manages the game window state
 * based on different message options.
 *
 * @author Juan-David-Brandon
 * @version 1.0
 * @since 2025
 */
public class FinalMessageController {

    /**
     * Reference to the Messages view component.
     */
    private Messages messages;

    /**
     * Default constructor for FinalMessageController.
     * Initializes a new instance of the controller.
     */
    public FinalMessageController() {

    }

    /**
     * Sets the Messages component that this controller will manage.
     *
     * @param messages the Messages view component to be controlled
     */
    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    /**
     * FXML injected button component used to close the message window.
     */
    @FXML
    Button closeButton;

    /**
     * Enables mouse interaction for the game window by setting mouse transparency to true.
     * This method retrieves the game window instance and modifies its root node properties.
     *
     * @throws IOException if an I/O error occurs while accessing the game window
     */
    void setTrue() throws IOException {
        GameWindow window = GameWindow.getInstance(3);
        window.getScene().getRoot().setMouseTransparent(true);
    }

    /**
     * Handles the close window action based on the message option.
     * If the message option is 3, it calls turnMessage(); otherwise, it calls closeMessage().
     * This method is triggered by FXML button events.
     *
     * @throws IOException if an I/O error occurs during the close operation
     */
    @FXML
    void closeWindow() throws IOException {
        if(messages.getOption() == 3) {
            turnMessage();
        }
        else {
            closeMessage();
        }
    }

    /**
     * Handles the turn message scenario by re-enabling mouse interaction
     * on the game window and closing the message dialog.
     * This method is called when the message option equals 3.
     *
     * @throws IOException if an I/O error occurs while manipulating the game window or closing the message
     */
    void turnMessage() throws IOException {
        setTrue();
        GameWindow window = GameWindow.getInstance(3);
        window.getScene().getRoot().setMouseTransparent(false);
        messages.close();
    }

    /**
     * Closes both the game window and the message dialog.
     * This method is called for message options other than 3.
     *
     * @throws IOException if an I/O error occurs while closing the windows
     */
    void closeMessage() throws IOException {
        GameWindow.getInstance(3).close();
        messages.close();
    }
}