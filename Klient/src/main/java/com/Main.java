package com;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/entrance.fxml"));
        AnchorPane root = loader.load();

        Scene scene = new Scene(root, 845, 541);

        primaryStage.setScene(scene);
        primaryStage.setTitle("BESTschoolEVER");

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
