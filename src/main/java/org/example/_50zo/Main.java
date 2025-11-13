package org.example._50zo;

import View.StartWindow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Entry point of the 'Cincuentazo' JavaFX application.
 * <p>
 * This class serves as the launcher for the graphical user interface (GUI).
 * It initializes the JavaFX runtime environment and displays the application's
 * main menu window through the {@link StartWindow} singleton instance.
 * </p>
 *
 * <p>
 * The lifecycle of a JavaFX application is managed through three main methods:
 * <ul>
 *     <li>{@link #init()} — Called once before the UI starts. Used for preloading data or resources.</li>
 *     <li>{@link #start(Stage)} — Called after initialization. Responsible for creating and showing the UI.</li>
 *     <li>{@link #stop()} — Called when the application terminates. Used to release resources or save data.</li>
 * </ul>
 * </p>
 *
 * <p>
 * This class does not directly manipulate the stage passed by the JavaFX runtime.
 * Instead, it delegates the window management to {@link StartWindow}, which follows
 * the Singleton design pattern to ensure that only one instance of the main menu exists.
 * </p>
 *
 * @author
 * Brandon (a.k.a. el pingoso)
 * @version 1.0
 * @since 2025
 */
public class Main extends Application {

    /**
     * Launches the JavaFX application.
     * <p>
     * This method is the standard entry point for JavaFX applications.
     * It triggers the JavaFX runtime and eventually calls {@link #start(Stage)}.
     * </p>
     *
     * @param args command-line arguments passed to the application (if any)
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes and displays the main menu window of the application.
     * <p>
     * This method is automatically invoked by the JavaFX runtime after
     * successful initialization. It loads and shows the {@link StartWindow}
     * singleton instance, which represents the main menu interface.
     * </p>
     *
     * @param primaryStage the primary stage provided by the JavaFX runtime
     * @throws Exception if an error occurs while loading the main menu FXML
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        StartWindow.getInstance().show();
    }

    /**
     * Called automatically when the application is about to stop.
     * <p>
     * This method can be used to release resources, close connections,
     * or perform any cleanup tasks before the application exits.
     * </p>
     */
    @Override
    public void stop() {
        System.out.println("Aplicación cerrada correctamente.");
    }
}
