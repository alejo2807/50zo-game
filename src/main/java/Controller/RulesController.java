package Controller;

import View.SelectionPlayers; // Importa la clase de la ventana a la que quieres volver
import View.RulesWindow;       // Importa la propia ventana de reglas para poder cerrarla
import View.StartWindow;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

public class RulesController {

    @FXML
    private Button backButton;

    /**
     * Acción ejecutada al presionar el botón "Volver a Selección de Jugadores".
     */
    @FXML
    void backToSelection() {
        try {
            // 1. Oculta la ventana de Reglas
            // Usamos el método estático si la ventana de reglas es Singleton,
            // o simplemente backButton.getScene().getWindow().hide();
            RulesWindow.closeInstance();

            // 2. Muestra la ventana de Selección de Jugadores
            // Usamos getInstance().show() para asegurar que la instancia existe
            StartWindow.getInstance().show();

        } catch (IOException e) {
            System.err.println("❌ Error al volver a la selección de jugadores: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método initialize opcional, si necesitaras configurar algo al cargar el FXML
    @FXML
    public void initialize() {
        // Por ejemplo, podrías deshabilitar el botón si fuera necesario,
        // pero para esta funcionalidad, no se requiere nada especial.
    }
}