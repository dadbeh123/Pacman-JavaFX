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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.controller.RegisterController;

import java.io.IOException;

public class ProfileMenu extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane anchorPane = setAnchorpaneChildren();
        BackgroundImage backgroundimage = new BackgroundImage(new Image(getClass().getResource("/sample/ProfileBackground.png").toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        anchorPane.setBackground(new Background(backgroundimage));
        stage.setScene(new Scene(anchorPane, 466, 750));
        stage.setTitle("Profile Menu");
    }

    public AnchorPane setAnchorpaneChildren() {
        AnchorPane anchorPane = new AnchorPane();
        Button changePassButton = new Button(), exitButton = new Button(), deleteButton = new Button();
        deleteButton.setText("Delete this account");deleteButton.setStyle("-fx-background-color: yellow;");
        changePassButton.setText("Change password");changePassButton.setStyle("-fx-background-color: yellow;");
        exitButton.setText("Exit");exitButton.setPrefWidth(110);exitButton.setStyle("-fx-background-color: yellow;");
        TextField passwordField = new TextField();
        passwordField.setText("New password:");
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(passwordField);
        vBox.getChildren().add(changePassButton);
        vBox.getChildren().add(deleteButton);
        vBox.getChildren().add(exitButton);
        vBox.setTranslateX(150);
        vBox.setTranslateY(200);
        vBox.setSpacing(15);
        setButtonsEvents(changePassButton, deleteButton, exitButton, vBox, passwordField);
        anchorPane.getChildren().add(vBox);
        return anchorPane;
    }

    public void setButtonsEvents(Button changePassButton, Button deleteButton,
                                 Button exitButton, VBox vBox, TextField passwordField) {
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
        changePassButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                vBox.getChildren().removeIf(n -> n instanceof Label);
                Label label = new Label();label.setTextFill(Color.color(1,1,1));
                try {
                    label.setText(RegisterController.getInstance().changePassword(passwordField.getText()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                vBox.getChildren().add(label);
            }
        });
        deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                final Stage dialog = new Stage();
                HBox hBox = new HBox(20);
                Button button1 = new Button("Yes"), button2 = new Button("No");
                hBox.getChildren().add(new Label("Are you sure"));
                hBox.getChildren().add(button1);
                hBox.getChildren().add(button2);
                button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        RegisterController.getInstance().deleteUser();
                        dialog.close();
                        try {
                            new MainMenu().start(MainMenu.stage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                button2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        dialog.close();
                    }
                });
                Scene dialogScene = new Scene(hBox, 200, 60);
                dialog.setScene(dialogScene);
                dialog.show();
            }
        });
    }


}
