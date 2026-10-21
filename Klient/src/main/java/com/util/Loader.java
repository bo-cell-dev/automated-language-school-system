package com.util;

import com.enums.StagePath;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Loader {
    public static void loadSceneWithThrowException(Stage stage, StagePath stagePath) throws IOException {
        FXMLLoader loader = new FXMLLoader(Loader.class.getResource(stagePath.getPathToFxml()));
        Parent root = loader.load();

        stage.setResizable(false);

        stage.setScene(new Scene(root));
    }

    public static void loadScene(Stage stage, StagePath stagePath) {
        try {
            loadSceneWithThrowException(stage, stagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

