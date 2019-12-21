package ru.ssau.tk.sergunin.lab.ui;

import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.nio.file.Paths;

public class AboutController implements Openable {
    private Stage stage;
    private Openable parentController;
    public MediaPlayer mediaPlayer;

    @FXML
    MediaView mediaView;

    public void play() {
        stage.show();
        stage.setOnCloseRequest(windowEvent -> mediaPlayer.stop());
        Media media = new Media(Paths.get("1234.mp4").toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(() -> stage.close());
        mediaView.setMediaPlayer(mediaPlayer);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setFactory(TabulatedFunctionFactory factory) {
    }

    @Override
    public void setParentController(Openable controller) {
        this.parentController = controller;
    }

}
