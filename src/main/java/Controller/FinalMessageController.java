package Controller;

import View.GameWindow;
import View.Messages;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class FinalMessageController {
    private Messages messages;
    public FinalMessageController() {

    }
    public void setMessages(Messages messages) {
        this.messages = messages;
    }
    @FXML
    Button closeButton;
    void setTrue() throws IOException{
        GameWindow window = GameWindow.getInstance(3);
        window.getScene().getRoot().setMouseTransparent(true);
    }
    @FXML
    void closeWindow() throws IOException {
        setTrue();
        GameWindow window = GameWindow.getInstance(3);
        window.getScene().getRoot().setMouseTransparent(false);
        messages.close();

    }

}
