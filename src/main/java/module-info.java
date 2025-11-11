module org.example._50zo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    exports org.example._50zo to javafx.graphics;
    // Abres los paquetes que contienen controladores a JavaFX
    opens Controller to javafx.fxml;
    opens View to javafx.fxml;

    // Exportas los paquetes principales
    exports Controller;
    exports Model.Exceptions;
    exports Model.Players;
    exports View;
    exports Model.Cards;

}
