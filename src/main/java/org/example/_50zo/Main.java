package org.example._50zo;

import View.StartWindow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Entry point of the 'Cincuentazo' JavaFX application.
 * 
 * This class serves as the launcher for the graphical user interface (GUI).
 * It initializes the JavaFX runtime environment and displays the application's
 * main menu window through the {@link StartWindow} singleton instance.
 * 
 *
 * 
 * The lifecycle of a JavaFX application is managed through three main methods:
 * 
 *     {@link #init()} — Called once before the UI starts. Used for preloading data or resources.
 *     {@link #start(Stage)} — Called after initialization. Responsible for creating and showing the UI.
 *     {@link #stop()} — Called when the application terminates. Used to release resources or save data.
 * 
 * 
 *
 * 
 * This class does not directly manipulate the stage passed by the JavaFX runtime.
 * Instead, it delegates the window management to {@link StartWindow}, which follows
 * the Singleton design pattern to ensure that only one instance of the main menu exists.
 * 
 *
 * @author
 * Brandon (a.k.a. el pingoso)
 * @version 1.0
 * @since 2025
 */
public class Main extends Application {

    /**
     * Launches the JavaFX application.
     * 
     * This method is the standard entry point for JavaFX applications.
     * It triggers the JavaFX runtime and eventually calls {@link #start(Stage)}.
     * 
     *
     * @param args command-line arguments passed to the application (if any)
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes and displays the main menu window of the application.
     * 
     * This method is automatically invoked by the JavaFX runtime after
     * successful initialization. It loads and shows the {@link StartWindow}
     * singleton instance, which represents the main menu interface.
     * 
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
     * 
     * This method can be used to release resources, close connections,
     * or perform any cleanup tasks before the application exits.
     * 
     */
    @Override
    public void stop() {
        System.out.println("Aplicación cerrada correctamente.");
    }
}
