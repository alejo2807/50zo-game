package View;

import Controller.FinalMessageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The {@code Messages} class represents a modal pop-up window
 * used to display game status messages such as victory, defeat, or general notifications.
 * <p>
 * This class extends {@link Stage} and dynamically loads different FXML views
 * depending on the provided option. It serves as a lightweight user interface
 * for feedback at the end of the game or during specific in-game events.
 * </p>
 *
 * <p><b>Usage example:</b></p>
 * <pre>{@code
 * Messages winMessage = new Messages(1);
 * winMessage.show();
 * }</pre>
 *
 * <p>Available options:</p>
 * <ul>
 *   <li>{@code 1} → Displays the “You Win” message window.</li>
 *   <li>{@code 2} → Displays the “You Lose” message window.</li>
 *   <li>{@code 3} → Displays a general “Game” message window.</li>
 * </ul>
 *
 * @author Juan-David-Brandon
 * @since 2025
 */
public class Messages extends Stage {

    /** The title of the message window. */
    private String title;

    /** Controller associated with the final message FXML. */
    private FinalMessageController controller;

    /** Indicates which message type should be displayed. */
    private int option;

    /**
     * Constructs a new {@code Messages} window based on the specified option.
     * <p>
     * The constructor dynamically loads an FXML file depending on the option:
     * <ul>
     *   <li>{@code 1 → youWin.fxml}</li>
     *   <li>{@code 2 → youLose.fxml}</li>
     *   <li>{@code 3 → message.fxml}</li>
     * </ul>
     * </p>
     *
     * @param option the type of message to display (1 = win, 2 = lose, 3 = general)
     * @throws IOException if the corresponding FXML file cannot be loaded
     * @throws IllegalArgumentException if the option provided is invalid
     */
    public Messages(int option) throws IOException {
        this.option = option;
        FXMLLoader fxmlLoader = new FXMLLoader();

        switch (option) {
            case 1 -> {
                fxmlLoader.setLocation(getClass().getResource("/youWin.fxml"));
                title = "You Win";
            }
            case 2 -> {
                fxmlLoader.setLocation(getClass().getResource("/youLose.fxml"));
                title = "You Lose";
            }
            case 3 -> {
                fxmlLoader.setLocation(getClass().getResource("/message.fxml"));
                title = "Game";
            }
            default -> throw new IllegalArgumentException("Invalid option: " + option);
        }

        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        controller.setMessages(this);

        Scene scene = new Scene(root);
        setScene(scene);
        setTitle(title);
        setResizable(false);
        initStyle(javafx.stage.StageStyle.UNDECORATED);
    }

    /**
     * Returns the numeric code representing the message type.
     *
     * @return the message type option (1, 2, or 3)
     */
    public int getOption() {
        return option;
    }
}
