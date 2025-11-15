package View;

import Controller.GameWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Eliminate extends Stage {

    private Eliminate() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/eliminate.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        this.setScene(scene);
        this.setTitle("50zo");
        this.setResizable(false);
    }
    private static class Holder {
        private static Eliminate INSTANCE = null;
    }
    public static Eliminate getInstance() throws IOException {
        if (Eliminate.Holder.INSTANCE == null) {
            Eliminate.Holder.INSTANCE = new Eliminate();
        }
        return Eliminate.Holder.INSTANCE;
    }
}
