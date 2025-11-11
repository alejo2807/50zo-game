package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Represents the player selection window of the 'Cincuentazo' application.
 * <p>
 * This class extends {@link Stage} and manages the player selection interface.
 * It uses the Singleton design pattern to ensure only one instance exists.
 * </p>
 */
public class SelectionPlayers extends Stage {

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
     * Static inner class implementing the Singleton holder pattern.
     */
    private static class Holder {
        private static SelectionPlayers INSTANCE = null;
    }

    /**
     * Returns the single instance of {@link SelectionPlayers()}.
     *
     * @return the singleton instance
     * @throws IOException if the FXML file cannot be loaded
     */
    public static SelectionPlayers getInstance() throws IOException {
        Holder.INSTANCE = (Holder.INSTANCE != null)
                ? Holder.INSTANCE
                : new SelectionPlayers();
        return Holder.INSTANCE;
    }

    /**
     * Closes the current instance of the window.
     */
    public static void closeInstance() {
        if (Holder.INSTANCE != null) {
            Holder.INSTANCE.close();
        }
    }

    /**
     * Displays the current instance of the window.
     *
     * @throws IOException if the instance has not been properly initialized
     */
    public static void showInstance() throws IOException {
        getInstance().show();
    }
}