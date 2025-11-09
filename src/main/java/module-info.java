module org.example._50zo {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.example._50zo;
    requires java.desktop;

    // Abres los paquetes que contienen controladores a JavaFX
    opens Controller to javafx.fxml;
    opens View to javafx.fxml;

    // Exportas los paquetes principales
    exports Controller;
    exports Model;
    exports View;
    exports Model.Cards;
}
