package org.example._50zo;

import View.GameWindow;
import View.StartWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        StartWindow.getInstance().show();
    }
}
