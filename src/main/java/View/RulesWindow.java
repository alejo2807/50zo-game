package View;

import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

/**
 * Clase Singleton para la ventana de Reglas del juego 50zo.
 */
public class RulesWindow extends Stage {

    private RulesWindow() throws IOException {
        // Asume que el FXML está en la carpeta raíz de recursos
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/info.fxml"));
        // El controlador se define dentro del FXML (fx:controller="Controller.RulesController")

        Parent root = loader.load();
        Scene scene = new Scene(root);

        this.setScene(scene);
        this.setTitle("Reglas de 50zo");
        this.setResizable(false);
    }

    private static class Holder {
        private static RulesWindow INSTANCE = null;
    }

    /**
     * Retorna la única instancia de RulesWindow, creándola si es necesario.
     * @return la instancia Singleton de RulesWindow
     * @throws IOException si el FXML o el CSS no pueden ser cargados
     */
    public static RulesWindow getInstance() throws IOException {
        if (Holder.INSTANCE == null) {
            Holder.INSTANCE = new RulesWindow();
        }
        return Holder.INSTANCE;
    }

    /**
     * Muestra la ventana de Reglas.
     * @throws IOException si hay un error de inicialización
     */
    public static void showInstance() throws IOException {
        getInstance().show();
    }

    /**
     * Oculta la ventana de Reglas.
     */
    public static void closeInstance() {
        if (Holder.INSTANCE != null) {
            Holder.INSTANCE.close();
        }
    }
}