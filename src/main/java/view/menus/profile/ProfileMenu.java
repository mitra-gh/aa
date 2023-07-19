package view.menus.profile;

import controller.DBController;
import controller.MainController;
import controller.UserController;
import enumoration.Translate;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Game;
import view.menus.LoginMenu;
import view.menus.MainMenu;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

public class ProfileMenu extends Application {

    public VBox layout;
    public static Stage stage;
    public VBox bar;
    public Rectangle profilePhoto;
    public Button changeUsername;
    public Button changePassword;
    public Button logout;
    public Button deleteAccount;
    public Label changeUsernameLabel;
    public BorderPane block;
    public TextField username;
    public Label usernameError;
    public Button submit;

    public boolean checkFormat = false;
    public Label savedAlert;
    public Button back;
    public BorderPane root;

    @Override
    public void start(Stage stage) throws Exception {
        ProfileMenu.stage = stage;
        makeScene();
    }

    @FXML
    public void initialize() {
        if (MainController.game.isBlackWhite()){
            ColorAdjust c = new ColorAdjust();
            c.setSaturation(-1);
            root.setEffect(c);
        }



        String path = String.valueOf(new File(UserController.getCurrentUser().getPath()).toURI());
        profilePhoto.setFill(new ImagePattern(new Image(path)));
        profilePhoto.setArcWidth(100);
        profilePhoto.setArcHeight(100);
        changeUsername.setText(Translate.CHANGE_USERNAME.getString());
        changeUsernameLabel.setText(Translate.CHANGE_USERNAME.getString());
        changePassword.setText(Translate.CHANGE_PASS.getString());
        back.setText(Translate.BACK.getString());
        logout.setText(Translate.LOGOUT.getString());
        deleteAccount.setText(Translate.DELETE_ACCOUNT.getString());
        username.setPromptText(Translate.USERNAME.getString() + "...");
        submit.setText(Translate.SAVE.getString());
        final BooleanProperty firstTime = new SimpleBooleanProperty(true);
        username.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                layout.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });

        username.setText(UserController.getCurrentUser().getUsername());
        if (UserController.getCurrentUser().isGuest()){
            username.setEditable(false);
        }
        username.textProperty().addListener((observabe, o, n) -> {
            savedAlert.setText("");
            if (Objects.equals(n, UserController.getCurrentUser().getUsername())) {
                submit.getStyleClass().remove("submit");
                submit.getStyleClass().add("inActiveButton");
            } else if (!submit.getStyleClass().contains("submit")) {
                submit.getStyleClass().remove("inActiveButton");
                submit.getStyleClass().add("submit");
            }
            if (checkFormat) {
                usernameError.setText(UserController.validateChangeUsername(n));
            }
        });


        if (MainController.game.isPersian()) {
            Node nodes = block.getLeft();
            block.setLeft(null);
            block.setRight(nodes);
        }

    }

    public void makeScene() throws IOException {
        BorderPane pane = FXMLLoader.load(
                new URL(Objects.requireNonNull(LoginMenu.class.getResource("/fxml/profileMenu.fxml")).toExternalForm()));

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }


    public void save(MouseEvent mouseEvent) {
        if (submit.getStyleClass().contains("submit")) {
            checkFormat = true;
            if (!UserController.validateChangeUsernameForm(username.getText())) {
                usernameError.setText(UserController.validateChangeUsername(username.getText()));
            } else {
                UserController.changeUsername(username.getText());
                savedAlert.setText("username changed successfully!");
                submit.getStyleClass().remove("submit");
                submit.getStyleClass().add("inActiveButton");
            }
        }
    }

    public void logout(MouseEvent mouseEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Translate.LOGOUT.getString());
        alert.setHeaderText(Translate.ARE_YOU_SURE_LOGOUT.getString());
        Optional<ButtonType> option = alert.showAndWait();

        if (option.isPresent() && option.get() == ButtonType.OK) {
            UserController.setCurrentUser(null);
            MainController.game = new Game();
            DBController.saveCurrentUser();
            new LoginMenu().start(stage);
        }




    }

    public void deleteAccount(MouseEvent mouseEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Translate.DELETE_ACCOUNT.getString());
        alert.setHeaderText(Translate.ARE_YOU_SURE_ِDELETE.getString());
        //alert.setContentText("C:/MyFile.txt");

        // option != null.
        Optional<ButtonType> option = alert.showAndWait();

        if (option.isPresent() && option.get() == ButtonType.OK) {
            UserController.getUsers().remove(UserController.getCurrentUser());
            UserController.setCurrentUser(null);
            MainController.game = new Game();
            DBController.saveCurrentUser();
            DBController.saveAllUsers();
            new LoginMenu().start(stage);
        }
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new MainMenu().start(stage);
    }
    public void goToChangePasswordMenu(MouseEvent mouseEvent) throws Exception {
        new ChangePassword().start(stage);
    }

    public void goToAvatarMenu(MouseEvent mouseEvent) throws Exception {
        new AvatarMenu().start(stage);
    }
}
