package View;

import Controller.GameWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The {@code GameWindow} class represents the main game interface window.
 *
 * This class extends {@link Stage} and implements the Singleton design pattern
 * to ensure that only one instance of the game window can exist at any given time.
 * It loads the corresponding FXML layout, initializes the {@link GameWindowController},
 * and manages the display lifecycle of the game window.
 *
 *
 * Usage example:
 * <pre>{@code
 * GameWindow window = GameWindow.getInstance(3);
 * window.show();
 * }</pre>
 *
 * @author Juan-David-Brandon
 * @since 2025
 */
public class GameWindow extends Stage {

    /**
     * Private constructor that initializes the game window with the specified number of GPU players.
     *
     * The constructor loads the FXML layout, assigns the controller, and configures the window’s
     * properties (title, style, and size behavior).
     *
     *
     * @param playersGPU the number of GPU (computer-controlled) players
     * @throws IOException if the FXML file cannot be loaded
     */
    private GameWindow(int playersGPU) throws IOException {
        GameWindowController gameWindowController = new GameWindowController();
        gameWindowController.getPlayersGPU(playersGPU);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GameWindow.fxml"));
        fxmlLoader.setController(gameWindowController);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        this.setScene(scene);
        this.setTitle("50zo");
        this.setResizable(false);
        this.initStyle(javafx.stage.StageStyle.UNDECORATED);
    }

    /**
     * Static inner holder class that manages the single instance of {@link GameWindow}.
     *
     * This approach ensures lazy initialization and thread-safety without explicit synchronization.
     *
     */
    private static class Holder {
        private static GameWindow INSTANCE = null;
    }

    /**
     * Returns the single instance of the {@link GameWindow}, creating it if it does not already exist.
     *
     * @param playersGPU the number of GPU (computer-controlled) players
     * @return the singleton {@code GameWindow} instance
     * @throws IOException if the FXML resource cannot be loaded
     */
    public static GameWindow getInstance(int playersGPU) throws IOException {
        if (Holder.INSTANCE == null) {
            Holder.INSTANCE = new GameWindow(playersGPU);
        }
        return Holder.INSTANCE;
    }

    /**
     * Displays the existing {@link GameWindow} instance.
     *
     * @throws IOException if the instance cannot be shown (e.g., due to loading issues)
     */
    public static void showInstance() throws IOException {
        Holder.INSTANCE.show();
    }

    /**
     * Closes the current instance of the {@link GameWindow}, if it exists.
     *
     * This method does not destroy the instance — it simply hides the window,
     * allowing it to be reopened later via {@link #showInstance()}.
     *
     */
    public static void closeInstance() {
        Holder.INSTANCE.close();
    }

    /**
     * Hides the window AND removes the stored instance, forcing a new one
     * to be created on the next call to getInstance().
     */
    public static void destroyInstance() {
        if (Holder.INSTANCE != null) {
            Holder.INSTANCE.close(); // Oculta/Cierra la ventana de JavaFX
            Holder.INSTANCE = null;  // <--- ¡Esto es lo crucial!
        }
    }


}
