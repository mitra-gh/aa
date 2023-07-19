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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class LoginMenu extends Application {
    public static Stage stage;
    public Label label;
    public TextField username;
    public VBox loginLayout;
    public Label loginLabel;
    public TextField loginUsername;
    public PasswordField loginPassword;
    public Button loginSubmit;
    public Pane loginRoot;
    public Label loginUsernameError;
    public Label loginPassError;
    public Button changeToRegisterButton;
    public VBox hideThird;

    public boolean checkFormat = false;
    public Label skip;


    @Override
    public void start(Stage stage) throws IOException {
        LoginMenu.stage = stage;
        makeLoginScene();
    }

    @FXML
    public void initialize() {
        firstAnimation();
        loginUsername.textProperty().addListener((observable, oldText, newText) -> {
            if (checkFormat) {
                loginUsernameError.setText(UserController.validateLoginUsername(newText));
            }
        });

        loginPassword.textProperty().addListener((observable, oldText, newText) -> {
            if (checkFormat) {
                loginPassError.setText(UserController.validateLoginPassword(newText));
            }
        });
    }

    public void makeLoginScene() throws IOException {
        BorderPane pane = FXMLLoader.load(
                new URL(Objects.requireNonNull(LoginMenu.class.getResource("/fxml/loginMenu.fxml")).toExternalForm()));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    public void firstAnimation() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setNode(loginLabel);
        fadeTransition.setDuration(Duration.millis(200));
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

        FadeTransition fadeTransition2 = new FadeTransition();
        fadeTransition2.setNode(hideThird);
        fadeTransition2.setDuration(Duration.millis(200));
        fadeTransition2.setFromValue(0);
        fadeTransition2.setToValue(1);

        ParallelTransition parTransition = new ParallelTransition();
        parTransition.setNode(changeToRegisterButton);
        parTransition.getChildren().addAll(fadeTransition, fadeTransition2);
        parTransition.setCycleCount(1);
        parTransition.play();
    }


    public void login(MouseEvent mouseEvent) throws Exception {
        checkFormat = true;
        if (!UserController.validateLoginForm(loginUsername.getText(), loginPassword.getText())) {
            loginUsernameError.setText(UserController.validateLoginUsernameAfterSubmit(loginUsername.getText()));
            loginPassError.setText(UserController.validateLoginPasswordAfterSubmit(loginUsername.getText(), loginPassword.getText()));
        } else {
            UserController.login(loginUsername.getText(), loginPassword.getText());
            MainController.game = UserController.getCurrentUser().getLastGame();
            new MainMenu().start(stage);
        }

    }


    public void changeToRegister(MouseEvent mouseEvent) throws Exception {
        RotateTransition transition = new RotateTransition();
        transition.setNode(changeToRegisterButton);
        transition.setDuration(Duration.millis(600));
        transition.setFromAngle(0);
        transition.setToAngle(405);


        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setNode(loginLabel);
        fadeTransition.setDuration(Duration.millis(200));
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);

        FadeTransition fadeTransition2 = new FadeTransition();
        fadeTransition2.setNode(hideThird);
        fadeTransition2.setDuration(Duration.millis(200));
        fadeTransition2.setFromValue(1);
        fadeTransition2.setToValue(0);

        SequentialTransition sequTransition = new SequentialTransition();
        sequTransition.setNode(changeToRegisterButton);
        sequTransition.getChildren().addAll(fadeTransition, fadeTransition2);
        sequTransition.setCycleCount(1);


        ParallelTransition parTransition = new ParallelTransition();
        parTransition.setNode(changeToRegisterButton);
        parTransition.getChildren().addAll(sequTransition, transition);
        parTransition.setCycleCount(1);
        parTransition.play();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(800),
                actionEvent -> {
                    try {
                        new RegisterMenu().start(stage);
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
