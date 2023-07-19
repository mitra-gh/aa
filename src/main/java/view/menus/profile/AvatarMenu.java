package view.menus.profile;

import controller.DBController;
import controller.MainController;
import controller.UserController;
import enumoration.Paths;
import enumoration.Translate;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Game;
import view.menus.LoginMenu;
import view.menus.MainMenu;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Optional;

public class AvatarMenu extends Application {

    public VBox layout;
    public static Stage stage;
    public BorderPane avatarRoot;
    public Label avatarLabel;
    public ScrollPane avatarBlock;
    public GridPane avatarsGrid;
    public Button uploadFile;
    public VBox bar;
    public Button changeUsername;
    public Rectangle profilePhoto;
    public Button changePassword;
    public Button logout;
    public Button deleteAccount;
    public Button back;


    @Override
    public void start(Stage stage) throws Exception {
        AvatarMenu.stage = stage;
        makeScene();
    }

    @FXML
    public void initialize() {
        avatarLabel.setText(Translate.AVATAR.getString());
        uploadFile.setText(Translate.UPLOAD.getString());
        String path = String.valueOf(new File(UserController.getCurrentUser().getPath()).toURI());
        profilePhoto.setFill(new ImagePattern(new Image(path)));
        profilePhoto.setArcWidth(100);
        profilePhoto.setArcHeight(100);
        changeUsername.setText(Translate.CHANGE_USERNAME.getString());
        changePassword.setText(Translate.CHANGE_PASS.getString());
        back.setText(Translate.BACK.getString());
        logout.setText(Translate.LOGOUT.getString());
        deleteAccount.setText(Translate.DELETE_ACCOUNT.getString());


        for (int i = 0; i < 16; i++) {
            Rectangle rectangle = new Rectangle(0, 0, 80, 80);
            path = String.valueOf(new File(Paths.USER_AVATARS.getPath() + (i + 1) + ".png").toURI());
            rectangle.setFill(new ImagePattern(new Image(path)));
            rectangle.setArcHeight(50);
            rectangle.setArcWidth(50);

            rectangle.setOnMouseEntered(mouseEvent -> {
                rectangle.setScaleX(1.1);
                rectangle.setScaleY(1.1);
            });

            rectangle.setOnMouseExited(mouseEvent -> {
                rectangle.setScaleX(1);
                rectangle.setScaleY(1);
            });

            int finalI = i;
            rectangle.setOnMouseClicked(mouseEvent -> {
                UserController.getCurrentUser().setPath(Paths.USER_AVATARS.getPath() + (finalI + 1) + ".png");
                DBController.saveCurrentUser();
                DBController.saveAllUsers();
                String path1 = String.valueOf(new File(UserController.getCurrentUser().getPath()).toURI());
                profilePhoto.setFill(new ImagePattern(new Image(path1)));
            });

            avatarsGrid.add(rectangle, i % 4, i / 4);
        }
    }

    public void makeScene() throws IOException {
        BorderPane pane = FXMLLoader.load(
                new URL(Objects.requireNonNull(LoginMenu.class.getResource("/fxml/avatarMenu.fxml")).toExternalForm()));

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    public void openFile(MouseEvent mouseEvent) throws URISyntaxException, IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            String path = Paths.USER_AVATARS.getPath() + UserController.getCurrentUser().getUsername();
            boolean check = new File(path).mkdirs();
            Files.copy(file.toPath(), (new File(path+ "/" + file.getName())).toPath());
            UserController.getCurrentUser().setPath(path+ "/" + file.getName());
            DBController.saveCurrentUser();
            DBController.saveAllUsers();
            profilePhoto.setFill(new ImagePattern(new Image(new File(path+ "/" + file.getName()).toURI().toString())));

        }
    }

    private static void configureFileChooser(FileChooser fileChooser) {
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
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

    public void goToChangePasswordMenu(MouseEvent mouseEvent) throws Exception {
        new ChangePassword().start(stage);
    }
}
