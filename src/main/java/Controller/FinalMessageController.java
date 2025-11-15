package Controller;

import Model.Players.PlayerGPU;
import Model.Players.PlayerHuman;
import Model.Players.TurnManager;
import View.GameWindow;
import View.Messages;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.List;

/**
 * Controller class responsible for managing the final message dialog and its interactions.
 * This controller handles the closing behavior of messages and manages the game window state
 * based on different message options.
 *
 * @author Juan-David-Brandon
 * @version 1.0
 * @since 2025
 */
public class FinalMessageController {

    /**
     * Reference to the Messages view component.
     */
    private Messages messages;
    private TurnManager turnManager;
    private PlayerHuman humanPlayer;
    private List<PlayerGPU> playerGPUList;
    @FXML
   private Label winer;

    @FXML
   private  Button closeButton;

    /**
     * Default constructor for FinalMessageController.
     * Initializes a new instance of the controller.
     */
    public FinalMessageController() {
        // Constructor vacío
    }

    /**
     * IMPORTANTE: NO llames a setWiner() aquí porque los datos aún no están disponibles
     */
    @FXML
    public void initialize() {
        // Déjalo vacío o inicializa solo componentes visuales básicos
        System.out.println("FinalMessageController initialized");
    }

    public void setTurnManager(TurnManager turnManager) {
        this.turnManager = turnManager;
    }

    public void setHumanPlayer(PlayerHuman humanPlayer) {
        this.humanPlayer = humanPlayer;
    }

    public void setPlayerGPUList(List<PlayerGPU> playerGPUList) {
        this.playerGPUList = playerGPUList;
    }

    /**
     * Sets the Messages component that this controller will manage.
     *
     * @param messages the Messages view component to be controlled
     */
    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    /**
     * Configura el texto del ganador.
     * DEBE llamarse DESPUÉS de setear todos los datos (turnManager, humanPlayer, playerGPUList)
     */
    public void setWiner() {

        int winnerTurn = turnManager.getTotalTurns().get(0);


        String winText;
        if (winnerTurn == humanPlayer.getTurn()) {
            winText = humanPlayer.getPlayerType() + " WIN!";
            System.out.println("✅ Humano ganó: " + winText);
        } else {
            String gpuName = "Unknown";
            for (PlayerGPU playerGPU : playerGPUList) {
                System.out.println("   Revisando GPU turno: " + playerGPU.getTurn());
                if (winnerTurn == playerGPU.getTurn()) {
                    gpuName = playerGPU.getPlayerType();
                    break;
                }
            }
            winText = gpuName + " WIN!";
            System.out.println("✅ GPU ganó: " + winText);
        }

        winer.setText(winText);

        // Forzar actualización visual
        winer.setVisible(true);
    }
    /**
     * Handles the close window action based on the message option.
     */
    @FXML
    void closeWindow() throws IOException {
        if (messages.getOption() == 3) {
            turnMessage();
        } else {
            closeMessage();
        }
    }

    /**
     * Handles the turn message scenario by re-enabling mouse interaction
     */
    void turnMessage() throws IOException {
        GameWindow window = GameWindow.getInstance(3);
        window.getScene().getRoot().setMouseTransparent(false);
        messages.close();
    }

    /**
     * Closes both the game window and the message dialog.
     */
    void closeMessage() throws IOException {
        GameWindow.getInstance(3).close();
        messages.close();
    }
}