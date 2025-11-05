package Model;

import Model.Cards.Card;
import Model.Cards.Deck;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.InputStream;

public class Prueba extends Application {

    @Override
    public void start(Stage stage) {
        // Crear el mazo
        Deck deck = new Deck();

        // Sacar una carta
        Card card = null;
        try {
            var getCardMethod = Deck.class.getDeclaredMethod("getCard");
            getCardMethod.setAccessible(true); // Permite acceder al método privado temporalmente
            card = (Card) getCardMethod.invoke(deck);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (card == null) {
            System.out.println("Error: no se pudo obtener una carta del mazo.");
            return;
        }

        System.out.println("Carta seleccionada: " + card.getSymbol() + " (" + card.getUrl() + ")");

        // Cargar imagen desde resources
        InputStream imageStream = getClass().getResourceAsStream(card.getUrl());
        if (imageStream == null) {
            System.out.println("No se encontró la imagen: " + card.getUrl());
            return;
        }

        Image image = new Image(imageStream);
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);

        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root, 400, 400);

        stage.setTitle("Carta mostrada: " + card.getSymbol());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
