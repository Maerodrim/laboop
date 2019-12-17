package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.Paths;

public class Init extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(Paths.get("src/main/java/ru/ssau/tk/sergunin/lab/alt_ui/view.fxml").toUri().toURL());
        Parent root = loader.load();
        TableController controller = loader.getController();
        controller.setStage(primaryStage);

        primaryStage.setTitle("Itenion");
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(450);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}