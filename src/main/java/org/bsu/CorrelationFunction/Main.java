package org.bsu.CorrelationFunction;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getClassLoader().getResource("window.fxml")));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStage(stage);
        stage.setTitle("Корреляционная функция");
        stage.setScene(new Scene(root, 1280, 720));
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
