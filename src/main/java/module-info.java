module com.example.moorhuhn {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.moorhuhn to javafx.fxml;
    exports com.example.moorhuhn;
}