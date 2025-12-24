package com.proto.marginalapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MarginalApp extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        URL fxmlResource = getClass().getResource("/MarginalApp.fxml");

        if (fxmlResource == null) {
            throw new IllegalStateException(
                    "Cannot find MarginalApp.fxml! " +
                            "Please ensure the file is in: src/main/resources/com/proto/marginalapplication/MarginalApp.fxml"
            );
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        Scene scene = new Scene(fxmlLoader.load(), 750, 950);

        primaryStage.setTitle("Marginal Trading Application");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setWidth(750);
        primaryStage.setHeight(950);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}