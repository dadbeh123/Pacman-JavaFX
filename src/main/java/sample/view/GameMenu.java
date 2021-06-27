package sample.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Text;
import sample.controller.GameController;

import java.io.IOException;
import java.util.HashMap;

public class GameMenu extends Application {
    private static GameMenu instance = null;
    private Stage stage;
    private GameController gameController = new GameController();
    private Timeline timeline = new Timeline();
    public GridPane gridPane = new GridPane();
    private boolean isSoundMuted = false;
    private Image pacmanRightImage;
    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image blueGhostImage;
    private Image redGhostImage;
    private Image yellowGhostImage;
    private Image pinkGhostImage;
    private Image escapeGhostImage;
    private Image smallDotImage;

    public static GameMenu getInstance() {
        if (instance == null)
            instance = new GameMenu();
        return instance;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        loadImages();
        gridPane.setStyle("-fx-background-color: black;");
        renderImages(gridPane);
        Scene scene = new Scene(gridPane, 400, 320);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().getCode() == 38)
                    gameController.handleKey('u');
                else if (keyEvent.getCode().getCode() == 40)
                    gameController.handleKey('d');
                else if (keyEvent.getCode().getCode() == 39)
                    gameController.handleKey('r');
                else if (keyEvent.getCode().getCode() == 37)
                    gameController.handleKey('l');
                else if (keyEvent.getCode().getCode() == 77) {
                    if (!isSoundMuted)
                        MainMenu.mediaPlayer.stop();
                    else
                        MainMenu.mediaPlayer.play();
                    isSoundMuted = !isSoundMuted;
                } else if (keyEvent.getCode().getCode() == 80)
                    timeline.stop();
                else
                    timeline.playFromStart();
            }
        });
        setTimeLineKeyFrames();
        timeline.playFromStart();
        stage.setScene(scene);
        stage.setTitle("Pac Man");
    }

    public void renderImages(GridPane gridPane) {
        char[][] board = gameController.getBoard().getBoard();
        gridPane.getChildren().clear();
        HashMap<String, Integer[]> ghostMap = gameController.getBoard().getGhostsIndexes();
        for (int i = 0; i < 31; i++) {
            for (int j = 0; j < 31; j++) {
                gridPane.add(pickImage(board[i][j]), j + 1, i);
            }
        }
        boolean isScoreBallConsumed = gameController.isScoreBallConsumed();
        gridPane.add(isScoreBallConsumed ? pickImage('g') : pickImage('r'), ghostMap.get("red")[0] + 1, ghostMap.get("red")[1]);
        gridPane.add(isScoreBallConsumed ? pickImage('g') : pickImage('b'), ghostMap.get("blue")[0] + 1, ghostMap.get("blue")[1]);
        gridPane.add(isScoreBallConsumed ? pickImage('g') : pickImage('y'), ghostMap.get("yellow")[0] + 1, ghostMap.get("yellow")[1]);
        gridPane.add(isScoreBallConsumed ? pickImage('g') : pickImage('p'), ghostMap.get("pink")[0] + 1, ghostMap.get("pink")[1]);
        setLabelsOnGridPane();
    }

    public Shape pickImage(char element) {
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(10);
        rectangle.setWidth(10);
        ImagePattern imagePattern;
        if (element == 'r') {
            imagePattern = new ImagePattern(redGhostImage);
            rectangle.setFill(imagePattern);
        } else if (element == 'b') {
            imagePattern = new ImagePattern(blueGhostImage);
            rectangle.setFill(imagePattern);
        } else if (element == 'y') {
            imagePattern = new ImagePattern(yellowGhostImage);
            rectangle.setFill(imagePattern);
        } else if (element == 'p') {
            imagePattern = new ImagePattern(pinkGhostImage);
            rectangle.setFill(imagePattern);
        } else if (element == 'e') {
            imagePattern = new ImagePattern(pacmanRightImage);
            rectangle.setFill(imagePattern);
        } else if (element == 'w') {
            imagePattern = new ImagePattern(pacmanLeftImage);
            rectangle.setFill(imagePattern);
        } else if (element == 'n') {
            imagePattern = new ImagePattern(pacmanUpImage);
            rectangle.setFill(imagePattern);
        } else if (element == 's') {
            imagePattern = new ImagePattern(pacmanDownImage);
            rectangle.setFill(imagePattern);
        } else if (element == 'g') {
            imagePattern = new ImagePattern(escapeGhostImage);
            rectangle.setFill(imagePattern);
        } else if (element == '1') {
            rectangle.setFill(Color.color(0, 0, 1));
        } else if (element == '*') {
            imagePattern = new ImagePattern(smallDotImage);
            rectangle.setFill(imagePattern);
        } else if (element == '#') {
            return new Circle(4, Color.color(1, 1, 1));
        }
        return rectangle;
    }

    public void loadImages() {
        this.pacmanDownImage = new Image(getClass().getResourceAsStream("/sample/PMDown.gif"));
        this.pacmanUpImage = new Image(getClass().getResourceAsStream("/sample/PMUp.gif"));
        this.pacmanLeftImage = new Image(getClass().getResourceAsStream("/sample/PMLeft.gif"));
        this.pacmanRightImage = new Image(getClass().getResourceAsStream("/sample/PMRight.gif"));
        this.yellowGhostImage = new Image(getClass().getResourceAsStream("/sample/YellowGhost.gif"));
        this.pinkGhostImage = new Image(getClass().getResourceAsStream("/sample/PinkGhost.gif"));
        this.redGhostImage = new Image(getClass().getResourceAsStream("/sample/RedGhost.gif"));
        this.blueGhostImage = new Image(getClass().getResourceAsStream("/sample/BlueGhost.gif"));
        this.escapeGhostImage = new Image(getClass().getResourceAsStream("/sample/EscapeGhost.gif"));
        this.smallDotImage = new Image(getClass().getResourceAsStream("/sample/SmallDot.png"));
    }

    public void setAfterGameScene() {
        Stage tempStage = new Stage();
        Pane pane = new Pane();
        Button mainMenuButton = new Button("Main Menu");
        Button newGameButton = new Button("New Game");
        mainMenuButton.setTranslateX(20);
        newGameButton.setTranslateX(100);
        mainMenuButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    new MainMenu().start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        newGameButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    tempStage.close();
                    instance = null;
                    GameMenu.getInstance().start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        timeline.stop();
        pane.getChildren().addAll(mainMenuButton, newGameButton);
        tempStage.setScene(new Scene(pane, 150,60));
        tempStage.show();
    }

    public void setTimeLineKeyFrames() {
        KeyFrame ghostFrame = new KeyFrame(Duration.seconds(0.3), event -> {
            try {
                gameController.moveGhosts();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        timeline.getKeyFrames().add(ghostFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void setLabelsOnGridPane() {
        Text lifePointLabel = new Text("Life points : " + gameController.getLifePoint());
        Text scoreLabel = new Text(String.format("Score : %d", gameController.getScore()));
        lifePointLabel.setFill(Color.color(1,1,1));
        scoreLabel.setFill(Color.color(1,1,1));
        gridPane.add(lifePointLabel, 0, 15);
        gridPane.add(scoreLabel, 0, 16);
    }

    public void handleEscapingGhosts() {
        Timeline tempTimeLine = new Timeline();
        gameController.setScoreBallConsumed(true);
        KeyFrame escapeFrame = new KeyFrame(Duration.seconds(10), event -> {});
        tempTimeLine.setCycleCount(1);
        tempTimeLine.getKeyFrames().add(escapeFrame);
        tempTimeLine.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gameController.setScoreBallConsumed(false);
            }
        });
        tempTimeLine.play();
    }

    public void playSound(String soundName) {
        if (!isSoundMuted) {
            Timeline tempTimeLine = new Timeline();
            Media introSoundEffect = new Media(getClass().getResource("/sample/" + soundName + ".mp3").toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(introSoundEffect);
            mediaPlayer.setVolume(0.2);
            KeyFrame soundFrame = new KeyFrame(Duration.seconds(1), event -> {
                mediaPlayer.play();
            });
            tempTimeLine.getKeyFrames().add(soundFrame);
            tempTimeLine.setCycleCount(1);
            tempTimeLine.play();
        }
    }
}
