package View;

import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

/**
 * Represents the Rules window of the 50zo game.
 * <p>
 * This class implements the <strong>Singleton</strong> pattern using the
 * Initialization-on-demand holder idiom. Only one RulesWindow instance
 * can exist at any time.
 * </p>
 *
 * <p>
 * The window loads its layout from the {@code info.fxml} file and
 * is initialized as a standard JavaFX {@link Stage}.
 * </p>
 */
public class RulesWindow extends Stage {

    /**
     * Private constructor to prevent direct instantiation.
     * <p>
     * Loads the FXML file that defines the Rules window layout.
     * The controller is expected to be declared inside the FXML
     * using {@code fx:controller="Controller.RulesController"}.
     * </p>
     *
     * @throws IOException if the FXML file cannot be loaded.
     */
    private RulesWindow() throws IOException {
        // Load the UI from the FXML file located in the resources root
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/info.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);

        // Configure the JavaFX Stage
        this.setScene(scene);
        this.setTitle("50zo Rules");
        this.setResizable(false);
    }

    /**
     * Holder class that lazily stores the Singleton instance.
     * <p>
     * This uses the "Initialization-on-demand holder" idiom,
     * which ensures thread-safe lazy initialization without explicit synchronization.
     * </p>
     */
    private static class Holder {
        private static RulesWindow INSTANCE = null;
    }

    /**
     * Returns the unique instance of {@link RulesWindow}, creating it if necessary.
     *
     * @return the Singleton instance of {@code RulesWindow}.
     * @throws IOException if the window cannot be created due to an FXML loading error.
     */
    public static RulesWindow getInstance() throws IOException {
        if (Holder.INSTANCE == null) {
            Holder.INSTANCE = new RulesWindow();
        }
        return Holder.INSTANCE;
    }

    /**
     * Displays the Rules window.
     * <p>
     * If the window has not been created yet, it will be initialized first.
     * </p>
     *
     * @throws IOException if window initialization fails.
     */
    public static void showInstance() throws IOException {
        getInstance().show();
    }

    /**
     * Hides the Rules window if it has already been created.
     * <p>
     * Does nothing if the instance has not been initialized.
     * </p>
     */
    public static void closeInstance() {
        if (Holder.INSTANCE != null) {
            Holder.INSTANCE.close();
        }
    }
}
