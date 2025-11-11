package Controller;

import View.GameWindow;
import View.SelectionPlayers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;

public class SelectionPlayersController {

    @FXML
    private ToggleGroup selectionPlayers;

    @FXML
    private RadioButton twoPlayersButton;

    @FXML
    private RadioButton threePlayersButton;

    @FXML
    private RadioButton fourPlayersButton;

    @FXML
    public void initialize() {
        // Guardar el número de jugadores en cada RadioButton
        twoPlayersButton.setUserData(2);
        threePlayersButton.setUserData(3);
        fourPlayersButton.setUserData(4);

        // Seleccionar 3 jugadores por defecto
        threePlayersButton.setSelected(true);
    }

    @FXML
    private Button playButton;

    @FXML
    private void onPlayClick() {
        // Obtener la cantidad de jugadores seleccionada
        int numPlayers = (int) selectionPlayers.getSelectedToggle().getUserData();

        System.out.println("Iniciando juego con " + numPlayers + " jugadores");

        // Aquí va tu lógica para iniciar el juego
        startGame(numPlayers);
    }

    private void startGame(int numPlayers) {
        try {
            // Cerrar la ventana de selección DESDE EL BOTÓN
            playButton.getScene().getWindow().hide();

            GameWindow.getInstance(numPlayers-1).show();

        } catch (IOException e) {
            System.err.println("Error al cargar la ventana del juego: " + e.getMessage());
            e.printStackTrace();
        }
    }

}



