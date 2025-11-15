package View;

import Controller.FinalMessageController;
import Model.Players.PlayerGPU;
import Model.Players.PlayerHuman;
import Model.Players.TurnManager;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * The {@code Messages} class represents a modal pop-up window used to display
 * end-of-game results or other important notifications.
 *
 * <p>This class extends {@link Stage} and dynamically loads the corresponding
 * FXML layout depending on the message type. It also injects game-related data
 * into the associated controller ({@link FinalMessageController}).</p>
 *
 * <h3>Usage example:</h3>
 * <pre>{@code
 * Messages msg = new Messages(1, humanPlayer, gpuPlayers, turnManager);
 * msg.show();
 * }</pre>
 *
 * <h3>Available options:</h3>
 * <ul>
 *   <li>{@code 1} → Displays the "You Win" window.</li>
 *   <li>{@code 2} and {@code 3} may be added in the future for "Lose" or general messages.</li>
 * </ul>
 *
 * <p>This window is borderless ({@code StageStyle.UNDECORATED}) and non-resizable.</p>
 *
 * @author
 * @since 2025
 */
public class ShowWiner extends Stage {

    /** The title displayed at the top of the message window. */
    private String title;

    /** Controller for the final message FXML file. */
    private FinalMessageController controller;

    /** Numeric code representing the type of message window to display. */
    private int option;

    /**
     * Creates a new {@code Messages} window for the given option.
     *
     * <p>Depending on the value of {@code option}, a specific FXML file is loaded.
     * Currently supported:</p>
     * <ul>
     *   <li>{@code 1 → youWin.fxml}</li>
     * </ul>
     *
     * <p>After loading the FXML, the controller is retrieved and populated with:</p>
     * <ul>
     *   <li>The human player instance</li>
     *   <li>A list of GPU players</li>
     *   <li>The game's turn manager</li>
     * </ul>
     *
     * <p>Finally, {@code controller.setWiner()} is executed on the JavaFX thread using
     * {@link Platform#runLater(Runnable)}.</p>
     *
     * @param option the type of message to display (currently only {@code 1} is valid)
     * @param humanPlayer the human player instance used for the final screen
     * @param playerGPUList the GPU-controlled players involved in the game
     * @param turnManager the turn manager, used to compute results and statistics
     *
     * @throws IOException if the FXML file fails to load
     * @throws IllegalArgumentException if an invalid option is provided
     */
    public ShowWiner(int option,
                     PlayerHuman humanPlayer,
                     List<PlayerGPU> playerGPUList,
                     TurnManager turnManager) throws IOException {

        this.option = option;
        FXMLLoader fxmlLoader = new FXMLLoader();

        // Select the FXML based on the provided option
        switch (option) {
            case 1 -> {
                fxmlLoader.setLocation(getClass().getResource("/youWin.fxml"));
                title = "You Win";
                System.out.println("📂 Loading youWin.fxml");
            }
            default -> throw new IllegalArgumentException("Invalid option: " + option);
        }

        // Load the UI layout
        Parent root = fxmlLoader.load();

        // Retrieve and configure the controller
        controller = fxmlLoader.getController();
        System.out.println("📋 Controller obtained: " + controller);

        controller.setMessages(this);
        controller.setHumanPlayer(humanPlayer);
        controller.setPlayerGPUList(playerGPUList);
        controller.setTurnManager(turnManager);

        System.out.println("📋 Controller data set");
        System.out.println("📋 HumanPlayer: " + humanPlayer);
        System.out.println("📋 Total turns: " + turnManager.getTotalTurns());

        // Execute the final winner calculation on the JavaFX thread
        Platform.runLater(() -> {
            System.out.println("🎯 Executing setWiner() on JavaFX thread...");
            controller.setWiner();
            System.out.println("🎯 setWiner() completed");
        });

        // Configure the window
        Scene scene = new Scene(root);
        setScene(scene);
        setTitle(title);
        setResizable(false);
        initStyle(javafx.stage.StageStyle.UNDECORATED);
    }

    /**
     * Returns the type of message represented by this window.
     *
     * @return the numeric option ({@code 1} for now)
     */
    public int getOption() {
        return option;
    }
}
