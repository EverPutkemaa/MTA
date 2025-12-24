module com.proto.marginalapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.controlsfx.controls;

    opens com.proto.marginalapplication to javafx.fxml;
    exports com.proto.marginalapplication;
}