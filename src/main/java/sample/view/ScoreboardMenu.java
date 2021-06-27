package sample.view;

import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import sample.model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;

public class ScoreboardMenu extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        Button button = new Button("Exit");
        pane.getChildren().add(button);
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    new MainMenu().start(MainMenu.stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        button.setTranslateX(700);
        button.setTranslateY(20);
        ListView<String> scoreTable = new ListView<>();
        Observable scores = FXCollections.observableArrayList(sortScores());
        scoreTable.setItems((ObservableList<String>) scores);
        BackgroundImage backgroundimage = new BackgroundImage(new Image(getClass().getResource("/sample/ScoreboardBackground.jpeg").toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        scoreTable.setPrefWidth(300);
        scoreTable.setPrefHeight(400);
        scoreTable.setTranslateX(230);
        scoreTable.setTranslateY(350);
        pane.getChildren().add(scoreTable);
        pane.setBackground(new Background(backgroundimage));
        stage.setScene(new Scene(pane, 750, 1000));
        stage.setTitle("Scoreboard Menu");
    }

    public ArrayList<String> sortScores() throws FileNotFoundException {
        File folder = new File("resources/users");
        File[] files = folder.listFiles();
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            if (!files[i].getName().equals(".DS_Store")) {
                FileReader fileReader = new FileReader(files[i]);
                GsonBuilder gsonBuilder = new GsonBuilder();
                User user = gsonBuilder.create().fromJson(fileReader, User.class);
                users.add(user);
            }
        }
        users.sort(new SortByScore());
        ArrayList<String> sortedScores = new ArrayList<>();
        int currentRank = 1, lastScore = users.get(0).getScore();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getScore() == lastScore)
                sortedScores.add(currentRank + "- " + users.get(i).getUsername() + ": " + users.get(i).getScore());
            else {
                currentRank = i + 1;
                sortedScores.add(currentRank + "- " + users.get(i).getUsername() + ": " + users.get(i).getScore());
            }
            lastScore = users.get(i).getScore();
        }
        return sortedScores;
    }
}

class SortByScore implements Comparator<User> {
    public int compare(User a, User b) {
        if (a.getScore() != b.getScore())
            return b.getScore() - a.getScore();
        return b.getLastScoreChange().isBefore(a.getLastScoreChange()) ? 1 : -1;
    }
}
