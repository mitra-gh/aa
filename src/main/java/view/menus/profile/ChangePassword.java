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

public class ChangePassword extends Application {

    public static Stage stage;
    public BorderPane block;
    public VBox layout;
    public Label changePassLabel;
    public PasswordField password;
    public PasswordField repeatPassword;
    public Label repeatPassError;
    public Label passError;
    public Button submit;
    public Label savedAlert;
    public VBox bar;
    public Button changeUsername;
    public Button changePassword;
    public Button deleteAccount;
    public Button logout;
    public Button back;
    public Rectangle profilePhoto;
    public boolean checkFormat = false;
    public PasswordField oldPassword;
    public Label oldPassError;
    public BorderPane root;

    @Override
    public void start(Stage stage) throws Exception {
        ChangePassword.stage = stage;
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
        changePassLabel.setText(Translate.CHANGE_PASS.getString());
        changePassword.setText(Translate.CHANGE_PASS.getString());
        back.setText(Translate.BACK.getString());
        logout.setText(Translate.LOGOUT.getString());
        deleteAccount.setText(Translate.DELETE_ACCOUNT.getString());
        password.setPromptText(Translate.PASSWORD.getString() + "...");
        oldPassword.setPromptText(Translate.CURRENT_PASSWORD.getString() + "...");
        repeatPassword.setPromptText(Translate.REPEAT_PASSWORD.getString() + "...");
        submit.setText(Translate.SAVE.getString());


        final BooleanProperty firstTime = new SimpleBooleanProperty(true);
        oldPassword.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                layout.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });


        if (UserController.getCurrentUser().isGuest()){
            oldPassword.setEditable(false);
            password.setEditable(false);
            repeatPassword.setEditable(false);
        }

        oldPassword.textProperty().addListener((observable, o, n) -> {
            savedAlert.setText("");
            if (checkNullFields()) {
                if (!submit.getStyleClass().contains("inActiveButton")){
                    submit.getStyleClass().remove("submit");
                    submit.getStyleClass().add("inActiveButton");
                }
            } else if (!submit.getStyleClass().contains("submit")) {
                submit.getStyleClass().remove("inActiveButton");
                submit.getStyleClass().add("submit");
            }

            if (checkFormat) {
                oldPassError.setText(UserController.validateChangePassOldPassword(n));
            }
        });


        password.textProperty().addListener((observable, o, n) -> {
            savedAlert.setText("");
            if (checkNullFields()) {
                if (!submit.getStyleClass().contains("inActiveButton")){
                    submit.getStyleClass().remove("submit");
                    submit.getStyleClass().add("inActiveButton");
                }
            } else if (!submit.getStyleClass().contains("submit")) {
                submit.getStyleClass().remove("inActiveButton");
                submit.getStyleClass().add("submit");
            }

            if (checkFormat) {
                passError.setText(UserController.validateChangePassPassword(n));
                repeatPassError.setText(UserController.validateRepeatPassword(n,repeatPassword.getText()));
            }
        });


        repeatPassword.textProperty().addListener((observable, o, n) -> {
            if (checkNullFields()) {
                if (!submit.getStyleClass().contains("inActiveButton")){
                    submit.getStyleClass().remove("submit");
                    submit.getStyleClass().add("inActiveButton");
                }
            } else if (!submit.getStyleClass().contains("submit")) {
                submit.getStyleClass().remove("inActiveButton");
                submit.getStyleClass().add("submit");
            }
            savedAlert.setText("");
            if (checkFormat) {
                repeatPassError.setText(UserController.validateRepeatPassword(password.getText(),n));
            }
        });


        if (MainController.game.isPersian()) {
            Node nodes = block.getLeft();
            block.setLeft(null);
            block.setRight(nodes);
        }

    }


    public boolean checkNullFields(){
        if (password.getText() == null || password.getText().length() == 0){
            return true;
        }
        if (oldPassword.getText() == null || oldPassword.getText().length() == 0){
            return true;
        }
        if ( repeatPassword.getText() == null || repeatPassword.getText().length() == 0){
            return true;
        }
        return false;
    }
    public void makeScene() throws IOException {
        BorderPane pane = FXMLLoader.load(
                new URL(Objects.requireNonNull(LoginMenu.class.getResource("/fxml/changePassword.fxml")).toExternalForm()));

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    public void logout(MouseEvent mouseEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Translate.LOGOUT.getString());
        alert.setHeaderText(Translate.ARE_YOU_SURE_LOGOUT.getString());
        //alert.setContentText("C:/MyFile.txt");

        // option != null.
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
    public void goToProfileMenu(MouseEvent mouseEvent) throws Exception {
        new ProfileMenu().start(stage);
    }

    public void goToAvatarMenu(MouseEvent mouseEvent) throws Exception {
        new AvatarMenu().start(stage);
    }
    public void save(MouseEvent mouseEvent) {
        if (submit.getStyleClass().contains("submit")) {
            checkFormat = true;
            if (!UserController.validateChangePassForm(oldPassword.getText(),password.getText(),repeatPassword.getText())) {
                passError.setText(UserController.validateChangePassPassword(password.getText()));
                repeatPassError.setText(UserController.validateRepeatPassword(password.getText(),repeatPassword.getText()));
                oldPassError.setText(UserController.validateChangePassOldPasswordAfterSubmit(oldPassword.getText()));
            } else {
                UserController.changePassword(password.getText(),oldPassword.getText());
                checkFormat = false;
                password.setText("");
                repeatPassword.setText("");
                oldPassword.setText("");
                submit.getStyleClass().remove("submit");
                submit.getStyleClass().add("inActiveButton");
                savedAlert.setText("password changed successfully!");
            }
        }
    }
}
