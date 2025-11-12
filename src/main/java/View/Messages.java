package View;

import Controller.FinalMessageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class Messages extends Stage {
    private String title;
    private FinalMessageController controller;
    public Messages(int option) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        switch (option) {
            case 1 -> {
                fxmlLoader.setLocation(getClass().getResource("/youWin.fxml"));
                title = "You Win";
            }
            case 2 -> {
                fxmlLoader.setLocation(getClass().getResource("/youLose.fxml"));
                title = "You Lose";
            }
            case 3 -> {
                fxmlLoader.setLocation(getClass().getResource("/message.fxml"));
                title = "Game";
            }
            default -> throw new IllegalArgumentException("Opción inválida: " + option);
        }

        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        controller.setMessages(this);
        Scene scene = new Scene(root);
        setScene(scene);
        setTitle(title);
        setResizable(false);
        initStyle(javafx.stage.StageStyle.UNDECORATED);
    }
}

