package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import javafx.stage.Stage;

/**
 * Represents the main menu window of the 'Cincuentazo' application.
 * <p>
 * This class extends {@link Stage} and manages the main menu interface defined in
 * the {@code MainMenu.fxml} file. It uses the Singleton design pattern to ensure
 * only one instance of the main menu exists throughout the application's lifecycle.
 * </p>
 */

public class StartWindow extends Stage {

    private StartWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/MainMenu.fxml")
        );
        Parent root = loader.load();
        Scene scene = new Scene(root);
        this.setScene(scene);
        this.setTitle("Main Menu");
        this.setResizable(false);
    }

    /**
     * Static inner class implementing the Singleton holder pattern.
     * <p>
     * Ensures lazy and thread-safe initialization of the {@link StartWindow} instance
     * without requiring explicit synchronization.
     * </p>
     */
    private static class Holder {
        /** Singleton instance of the {@link StartWindow}. */
        private static StartWindow INSTANCE = null;
    }

    /**
     * Returns the single instance of {@link StartWindow}, creating it if necessary.
     *
     * @return the singleton {@link StartWindow} instance
     * @throws IOException if the FXML file cannot be loaded during initialization
     */
    public static StartWindow getInstance() throws IOException {
        Holder.INSTANCE = (Holder.INSTANCE != null)
                ? Holder.INSTANCE
                : new StartWindow();
        return Holder.INSTANCE;
    }

    /**
     * Closes the current instance of the {@link StartWindow}, if it exists.
     * <p>
     * This does not destroy the instance — it only hides the window from view.
     * </p>
     */
    public static void closeInstance() {
        Holder.INSTANCE.close();
    }

    /**
     * Displays the current instance of the {@link StartWindow}.
     * <p>
     * If the window has not been initialized yet, it must first be created
     * via {@link #getInstance()} before calling this method.
     * </p>
     *
     * @throws IOException if the instance has not been properly initialized
     */
    public static void showInstance() throws IOException {
        Holder.INSTANCE.show();
    }
}


