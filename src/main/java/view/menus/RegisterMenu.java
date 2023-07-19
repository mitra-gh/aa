package view.menus;

import controller.DBController;
import controller.MainController;
import controller.UserController;
import javafx.animation.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class RegisterMenu extends Application {
    public static Stage stage;
    public Label registerPassError;
    public Button registerSubmit;
    public PasswordField registerPassword;
    public Label registerUsernameError;
    public TextField registerUsername;
    public VBox registerLayout;
    public Label registerLabel;

    public Button changeToLoginButton;
    public Label registerRepeatPassError;
    public PasswordField registerRepeatPassword;
    public VBox thirdHide;

    public boolean checkFormat;
    public BorderPane registerRoot;
    public Label skip;

    @Override
    public void start(Stage stage) throws Exception {
        RegisterMenu.stage = stage;
        makeRegisterScene();
    }


    @FXML
    public void initialize() {
        firstAnimation();
        registerUsername.textProperty().addListener((observable, oldText, newText) -> {
            if (checkFormat) {
                registerUsernameError.setText(UserController.validateRegisterUsername(newText));
            }
        });

        registerPassword.textProperty().addListener((observable, oldText, newText) -> {
            if (checkFormat) {
                registerPassError.setText(UserController.validateRegisterPassword(newText));
                registerRepeatPassError.setText(UserController.validateRepeatPassword(newText, registerRepeatPassword.getText()));
            }
        });

        registerRepeatPassword.textProperty().addListener((observable, oldText, newText) -> {
            if (checkFormat) {
                registerRepeatPassError.setText(UserController.validateRepeatPassword(registerPassword.getText(), newText));
            }
        });
    }


    public void makeRegisterScene() throws IOException {
        BorderPane pane = FXMLLoader.load(
                new URL(Objects.requireNonNull(LoginMenu.class.getResource("/fxml/registerMenu.fxml")).toExternalForm()));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    public void firstAnimation() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setNode(registerLabel);
        fadeTransition.setDuration(Duration.millis(200));
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

        FadeTransition fadeTransition2 = new FadeTransition();
        fadeTransition2.setNode(thirdHide);
        fadeTransition2.setDuration(Duration.millis(200));
        fadeTransition2.setFromValue(0);
        fadeTransition2.setToValue(1);

        ParallelTransition parTransition = new ParallelTransition();
        parTransition.setNode(changeToLoginButton);
        parTransition.getChildren().addAll(fadeTransition, fadeTransition2);
        parTransition.setCycleCount(1);
        parTransition.play();
    }

    public void register(MouseEvent mouseEvent) throws Exception {
        checkFormat = true;
        if (!UserController.validateRegisterForm(registerUsername.getText(), registerPassword.getText(), registerRepeatPassword.getText())) {
            registerUsernameError.setText(UserController.validateRegisterUsername(registerUsername.getText()));
            registerPassError.setText(UserController.validateRegisterPassword(registerPassword.getText()));
            registerRepeatPassError.setText(UserController.validateRepeatPassword(registerPassword.getText(),
                    registerRepeatPassword.getText()));
        } else {

            UserController.register(registerUsername.getText(), registerPassword.getText());
            MainController.game = UserController.getCurrentUser().getLastGame();
            new MainMenu().start(stage);
        }
    }

    public void changeToLogin(MouseEvent mouseEvent) {
        RotateTransition transition = new RotateTransition();
        transition.setNode(changeToLoginButton);
        transition.setDuration(Duration.millis(600));
        transition.setFromAngle(0);
        transition.setToAngle(360);


        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setNode(registerLabel);
        fadeTransition.setDuration(Duration.millis(200));
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);

        FadeTransition fadeTransition2 = new FadeTransition();
        fadeTransition2.setNode(thirdHide);
        fadeTransition2.setDuration(Duration.millis(200));
        fadeTransition2.setFromValue(1);
        fadeTransition2.setToValue(0);

        SequentialTransition sequTransition = new SequentialTransition();
        sequTransition.setNode(changeToLoginButton);
        sequTransition.getChildren().addAll(fadeTransition, fadeTransition2);
        sequTransition.setCycleCount(1);


        ParallelTransition parTransition = new ParallelTransition();
        parTransition.setNode(changeToLoginButton);
        parTransition.getChildren().addAll(sequTransition, transition);
        parTransition.setCycleCount(1);
        parTransition.play();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(800),
                actionEvent -> {
                    try {
                        new LoginMenu().start(stage);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void skip(MouseEvent mouseEvent) throws Exception {
        User user = new User(UserController.generateUsername(),"");
        user.setGuest(true);
        UserController.getUsers().add(user);
        UserController.setCurrentUser(user);
        DBController.saveAllUsers();
        DBController.saveCurrentUser();
        new MainMenu().start(stage);
    }
}
