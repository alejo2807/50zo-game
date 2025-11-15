package View;

import Controller.GameWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * The {@code Eliminate} class represents a singleton JavaFX {@link Stage} used
 * to display the "eliminate" window within the application.
 * <p>
 * This class loads its layout from the {@code eliminate.fxml} file and ensures
 * that only one instance of this window exists at any given time using the
 * Initialization-on-demand holder idiom.
 * </p>
 *
 * <p><b>Usage:</b></p>
 * <pre>{@code
 * Eliminate window = Eliminate.getInstance();
 * window.show();
 * }</pre>
 *
 * @see javafx.stage.Stage
 */
public class Eliminate extends Stage {

    /**
     * Creates a new {@code Eliminate} window by loading the corresponding FXML file
     * and configuring the stage's properties.
     *
     * @throws IOException if the FXML file cannot be loaded.
     */
    private Eliminate() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/eliminate.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        this.setScene(scene);
        this.setTitle("50zo");
        this.setResizable(false);
    }

    /**
     * Holder class for the singleton instance.
     * <p>
     * This leverages the classloader's thread-safe initialization to ensure
     * the instance is created only when first requested.
     * </p>
     */
    private static class Holder {
        private static Eliminate INSTANCE = null;
    }

    /**
     * Returns the singleton instance of the {@code Eliminate} window.
     * If the instance has not yet been created, it is initialized.
     *
     * @return the unique {@code Eliminate} instance.
     * @throws IOException if the FXML file cannot be loaded during initialization.
     */
    public static Eliminate getInstance() throws IOException {
        if (Eliminate.Holder.INSTANCE == null) {
            Eliminate.Holder.INSTANCE = new Eliminate();
        }
        return Eliminate.Holder.INSTANCE;
    }
}

