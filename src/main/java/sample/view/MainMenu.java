package sample.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import sample.controller.RegisterController;

public class MainMenu extends Application {
    public static Stage stage;
    public static MediaPlayer mediaPlayer;

    @Override
    public void start(Stage stage) throws Exception {
        MainMenu.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/sample/MainMenu.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
        Media introSoundEffect = new Media(getClass().getResource("/sample/IntroSound.mp3").toExternalForm());
        mediaPlayer = new MediaPlayer(introSoundEffect);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();
        mediaPlayer.autoPlayProperty().setValue(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setOnEndOfMedia(mediaPlayer::play);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void startRegisterPage() throws Exception {
        new RegisterMenu().start(stage);
    }

    public void startProfilePage() throws Exception {
        if (RegisterController.currentUser == null)
            return;
        new ProfileMenu().start(stage);
    }

    public void startLoginPage() throws Exception {
        new LoginMenu().start(stage);
    }

    public void startGamePage() throws Exception {
        GameMenu.getInstance().start(stage);
    }

    public void startScoreboardPage() throws Exception {
        new ScoreboardMenu().start(stage);
    }

    public void exit() {
        System.exit(0);
    }
}
