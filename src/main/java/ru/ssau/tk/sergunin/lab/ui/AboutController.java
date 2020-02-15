package ru.ssau.tk.sergunin.lab.ui;

import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.io.BufferedInputStream;
import java.nio.file.Paths;
import java.util.Objects;

@ConnectableItem(name = "Смешарики", type = Item.CONTROLLER, pathFXML = "about.fxml")
public class AboutController implements Openable {
    private MediaPlayer mediaPlayer;
    @FXML
    MediaView mediaView;
    private Stage stage;

    void play() {
        stage.show();
        stage.setOnCloseRequest(windowEvent -> mediaPlayer.stop());
        Media media = new Media(getClass().getResource("/videos/video.mp4").toString());
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
    public void setParentController(Openable controller) { }

}
