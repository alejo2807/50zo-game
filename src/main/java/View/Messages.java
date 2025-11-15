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
 * The {@code Messages} class represents a modal pop-up window
 * used to display game status messages such as victory, defeat, or general notifications.
 * 
 * This class extends {@link Stage} and dynamically loads different FXML views
 * depending on the provided option. It serves as a lightweight user interface
 * for feedback at the end of the game or during specific in-game events.
 * 
 *
 * Usage example:
 * <pre>{@code
 * Messages winMessage = new Messages(1);
 * winMessage.show();
 * }</pre>
 *
 * Available options:
 * 
 *   {@code 1} -> Displays the "You Win" message window.
 *   {@code 2} -> Displays the "You Lose" message window.
 *   {@code 3} -> Displays a general "Game" message window.
 *
 * 
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
     * 
     * The constructor dynamically loads an FXML file depending on the option:
     * 
     *   {@code 1 → youWin.fxml}
     *   {@code 2 → youLose.fxml}
     *   {@code 3 → message.fxml}
     * 
     * 
     *
     * @param option the type of message to display (1 = win, 2 = lose, 3 = general)
     * @throws IOException if the corresponding FXML file cannot be loaded
     * @throws IllegalArgumentException if the option provided is invalid
     */
    public Messages(int option, PlayerHuman humanPlayer, List<PlayerGPU> playerGPUList, TurnManager turnManager) throws IOException {
        this.option = option;
        FXMLLoader fxmlLoader = new FXMLLoader();

        switch (option) {
            case 1 -> {
                fxmlLoader.setLocation(getClass().getResource("/youWin.fxml"));
                title = "You Win";
                System.out.println("📂 Cargando youWin.fxml");
            }
            default -> throw new IllegalArgumentException("Invalid option: " + option);
        }

        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();

        System.out.println("📋 Controller obtenido: " + controller);

        controller.setMessages(this);
        controller.setHumanPlayer(humanPlayer);
        controller.setPlayerGPUList(playerGPUList);
        controller.setTurnManager(turnManager);

        System.out.println("📋 Datos configurados");
        System.out.println("📋 HumanPlayer: " + humanPlayer);
        System.out.println("📋 TurnManager total turns: " + turnManager.getTotalTurns());

        // EJECUTAR setWiner() EN EL HILO DE JavaFX CON UN PEQUEÑO DELAY
        Platform.runLater(() -> {
            System.out.println("🎯 Ejecutando setWiner() en el hilo de JavaFX...");
            controller.setWiner();
            System.out.println("🎯 setWiner() completado");
        });

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
