package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Controller.GameWindowController;
import java.io.IOException;

public class GameWindow extends Stage {

    private GameWindowController controller;

    private GameWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/GameWindow.fxml")
        );
        Parent root = loader.load();

        // Guardar referencia al controlador
        controller = loader.getController();

        Scene scene = new Scene(root);
        this.setScene(scene);
        this.setTitle("El Cincuentazo");
        this.setResizable(false);
    }

    private static class Holder {
        private static GameWindow INSTANCE = null;
    }

    public static GameWindow getInstance() throws IOException {
        Holder.INSTANCE = (Holder.INSTANCE != null)
                ? Holder.INSTANCE
                : new GameWindow();
        return Holder.INSTANCE;
    }

    public void setNumPlayers(int numPlayers) {
        if (controller != null) {
            controller.setNumPlayers(numPlayers);
        }
    }

    public static void closeInstance() {
        if (Holder.INSTANCE != null) {
            Holder.INSTANCE.close();
        }
    }
}