package Controller;

import View.RulesWindow;
import View.SelectionPlayers;
import View.StartWindow;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class StartWindowController {

    @FXML
    private Button mainMenuExitButton;

    @FXML
    private Button mainMenuPlayButton;


    @FXML
    void onMainMenuExitClick(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void onMainMenuPlayClick(ActionEvent event) {

        try {
            // Cerrar el menú inicial
            StartWindow.closeInstance();

            // Abrir la ventana de selección de jugadores
            SelectionPlayers.getInstance().show();

        } catch (IOException e) {
            System.err.println("Error al cargar la selección de jugadores: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML Button rulesButton;
    @FXML
    void rules() throws IOException {
        RulesWindow.getInstance().show();
        StartWindow.getInstance().close();
    }

}
