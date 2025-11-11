package View;

import Controller.GameWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameWindow extends Stage {


        private GameWindow(int playersGPU) throws IOException{
            GameWindowController gameWindowController = new GameWindowController();
            gameWindowController.getPlayersGPU(playersGPU);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GameWindow.fxml"));
            fxmlLoader.setController(gameWindowController);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            this.setScene(scene);
            this.setTitle("50zo");
            this.setResizable(false);
            this.initStyle(javafx.stage.StageStyle.UNDECORATED);



        }
        private static class Holder{
            private static GameWindow INSTANCE = null;



        }
    public static GameWindow getInstance(int playersGPU) throws IOException{
        if(Holder.INSTANCE == null){
            Holder.INSTANCE = new GameWindow(playersGPU);
        }
        return Holder.INSTANCE;


    }
    public static void showInstance() throws IOException {
        Holder.INSTANCE.show();
    }

    /**
     * Closes the current instance of the {@link GameWindow}, if it exists.
     * <p>
     * This method does not destroy the instance — it simply hides the window.
     * </p>
     */
    public static void closeInstance() {
        Holder.INSTANCE.close();
    }




}
