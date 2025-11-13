package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The {@code SelectionPlayers} class represents the player selection window
 * of the <b>Cincuentazo</b> application.
 * <p>
 * This class extends {@link Stage} and handles the user interface for selecting
 * the number of players before the game begins. It follows the
 * <b>Singleton design pattern</b> to ensure that only one instance of the
 * selection window exists at any given time.
 * </p>
 *
 * <p><b>Usage example:</b></p>
 * <pre>{@code
 * SelectionPlayers selectionWindow = SelectionPlayers.getInstance();
 * selectionWindow.show();
 * }</pre>
 *
 * @author Juan-David-Brandon
 * @since 2025
 */
public class SelectionPlayers extends Stage {

    /**
     * Private constructor that initializes the player selection window.
     * <p>
     * Loads the corresponding FXML layout, sets the title, and prevents resizing.
     * </p>
     *
     * @throws IOException if the {@code SelectionPlayers.fxml} file cannot be loaded
     */
    private SelectionPlayers() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/SelectionPlayers.fxml")
        );
        Parent root = loader.load();
        Scene scene = new Scene(root);
        this.setScene(scene);
        this.setTitle("Selection Players");
        this.setResizable(false);
    }

    /**
     * Static inner class implementing the Singleton Holder pattern.
     * <p>
     * This ensures thread-safe lazy initialization of the {@link SelectionPlayers} instance.
     * </p>
     */
    private static class Holder {
        private static SelectionPlayers INSTANCE = null;
    }

    /**
     * Returns the single instance of {@link SelectionPlayers}.
     * <p>
     * If no instance exists, a new one is created and stored in memory.
     * </p>
     *
     * @return the singleton instance of {@code SelectionPlayers}
     * @throws IOException if the FXML layout cannot be loaded
     */
    public static SelectionPlayers getInstance() throws IOException {
        Holder.INSTANCE = (Holder.INSTANCE != null)
                ? Holder.INSTANCE
                : new SelectionPlayers();
        return Holder.INSTANCE;
    }

    /**
     * Closes the current instance of the {@link SelectionPlayers} window,
     * if it exists.
     * <p>
     * This method only hides the window; it does not destroy the instance.
     * </p>
     */
    public static void closeInstance() {
        if (Holder.INSTANCE != null) {
            Holder.INSTANCE.close();
        }
    }

    /**
     * Displays the {@link SelectionPlayers} window.
     * <p>
     * If no instance has been created yet, it initializes one before showing it.
     * </p>
     *
     * @throws IOException if the FXML layout cannot be loaded
     */
    public static void showInstance() throws IOException {
        getInstance().show();
    }
}
