package Model;

import View.StartWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        StartWindow mainMenu = StartWindow.getInstance();
        mainMenu.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
