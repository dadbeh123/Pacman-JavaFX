package sample.view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import sample.controller.RegisterController;

import java.io.IOException;

public class LoginMenu extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane anchorPane = setAnchorpaneChildren();
        BackgroundImage backgroundimage = new BackgroundImage(new Image(getClass().getResource("/sample/LoginBackground.png").toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        anchorPane.setBackground(new Background(backgroundimage));
        stage.setScene(new Scene(anchorPane, 900, 600));
        stage.setTitle("Login Menu");
    }

    public AnchorPane setAnchorpaneChildren() {
        AnchorPane anchorPane = new AnchorPane();
        Button button = new Button(), exitButton = new Button();
        button.setText("Login");button.setStyle("-fx-background-color: yellow;");
        exitButton.setText("Exit");exitButton.setPrefWidth(60);exitButton.setStyle("-fx-background-color: yellow;");
        TextField usernameField = new TextField();
        usernameField.setText("Username");
        TextField passwordField = new TextField();
        passwordField.setText("Password");
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(usernameField);
        vBox.getChildren().add(passwordField);
        vBox.getChildren().add(button);
        vBox.getChildren().add(exitButton);
        vBox.setTranslateX(700);
        vBox.setTranslateY(200);
        vBox.setSpacing(15);
        exitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    new MainMenu().start(MainMenu.stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                vBox.getChildren().removeIf(n -> n instanceof Label);
                Label label = new Label();
                try {
                    label.setText(RegisterController.getInstance().loginUser(usernameField.getText(), passwordField.getText()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                vBox.getChildren().add(label);
            }
        });
        anchorPane.getChildren().add(vBox);
        return anchorPane;
    }
}
