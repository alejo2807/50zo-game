package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The {@code StartWindow} class represents the main menu window
 * of the <b>Cincuentazo</b> application.
 * <p>
 * This class extends {@link Stage} and manages the application's main menu
 * interface defined in the {@code MainMenu.fxml} file. It follows the
 * <b>Singleton design pattern</b> to ensure that only one instance of the
 * main menu window exists during the application's lifecycle.
 * </p>
 *
 * <p><b>Usage example:</b></p>
 * <pre>{@code
 * StartWindow mainMenu = StartWindow.getInstance();
 * mainMenu.show();
 * }</pre>
 *
 * @author Juan-David-Brandon
 * @since 2025
 */
public class StartWindow extends Stage {

    /**
     * Private constructor that initializes the main menu window.
     * <p>
     * Loads the FXML layout, sets the title, and configures the window
     * to be non-resizable.
     * </p>
     *
     * @throws IOException if the {@code MainMenu.fxml} file cannot be loaded
     */
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
     * Static inner class implementing the Singleton Holder pattern.
     * <p>
     * Ensures thread-safe, lazy initialization of the {@link StartWindow} instance
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
     * @return the singleton instance of {@code StartWindow}
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
     * This method hides the window but does not destroy the singleton instance.
     * </p>
     */
    public static void closeInstance() {
        if (Holder.INSTANCE != null) {
            Holder.INSTANCE.close();
        }
    }

    /**
     * Displays the {@link StartWindow}.
     * <p>
     * If the window has not been created yet, it initializes a new instance before showing it.
     * </p>
     *
     * @throws IOException if the instance has not been properly initialized
     */
    public static void showInstance() throws IOException {
        getInstance().show();
    }
}
