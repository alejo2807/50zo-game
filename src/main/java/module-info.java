module org.example._Ozo {
    requires javafx.controls;
    requires javafx.fxml;

    opens game50zo to javafx.fxml;
    opens game50zo.Controller to javafx.fxml;

    exports game50zo;
    exports game50zo.Controller;
    exports game50zo.Model;
    exports game50zo.View;
}
