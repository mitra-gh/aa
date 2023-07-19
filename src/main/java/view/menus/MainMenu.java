package view.menus;

import controller.MainController;
import controller.UserController;
import enumoration.Translate;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Game;
import model.other.MenuCircle;
import view.animations.MenuCircleTransition;
import view.menus.profile.ProfileMenu;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

public class MainMenu extends Application {
    public static Stage stage;
    public BorderPane mainRoot;
    public Button startButton;

    public Rectangle setting;
    public Rectangle changeAvatar;
    public StackPane substructure;
    public Rectangle rank;
    public Circle littleCircle;
    public Rectangle exit;


    @Override
    public void start(Stage stage) throws Exception {
        MainMenu.stage = stage;
        makeScene();
    }


    @FXML
    private void initialize() {
        if (MainController.game.isBlackWhite()) {
            ColorAdjust c = new ColorAdjust();
            c.setSaturation(-1);
            mainRoot.setEffect(c);
        }
        MenuCircle menuCircle = new MenuCircle(12, 150);
        menuCircle.setId("menuCircle");
        MenuCircleTransition transition = menuCircle.getTransition();
        transition.play();
        int index = substructure.getChildren().indexOf(startButton);
        substructure.getChildren().add(index, menuCircle);
        startButton.setText(Translate.START.getString());
        String path = String.valueOf(new File(UserController.getCurrentUser().getPath()).toURI());
        changeAvatar.setFill(new ImagePattern(new Image(path)));
        setting.setFill(new ImagePattern(new Image(MainMenu.class.getResource("/images/icons/settings.png").toExternalForm())));
        rank.setFill(new ImagePattern(new Image(MainMenu.class.getResource("/images/icons/ranking.png").toExternalForm())));
        exit.setFill(new ImagePattern(new Image(MainMenu.class.getResource("/images/icons/exit.png").toExternalForm())));

        changeAvatar.setOnMouseEntered(this::scaleUp);
        setting.setOnMouseEntered(this::scaleUp);
        rank.setOnMouseEntered(this::scaleUp);
        exit.setOnMouseEntered(this::scaleUp);
        exit.setOnMouseExited(this::scaleDown);
        rank.setOnMouseExited(this::scaleDown);
        changeAvatar.setOnMouseExited(this::scaleDown);
        setting.setOnMouseExited(this::scaleDown);
    }

    public void makeScene() throws IOException {
        BorderPane pane = FXMLLoader.load(
                new URL(Objects.requireNonNull(MainMenu.class.getResource("/fxml/mainMenu.fxml")).toExternalForm()));

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }


    public void scaleDown(MouseEvent mouseEvent) {
        Node node = (Node) mouseEvent.getSource();
        ScaleTransition st = new ScaleTransition(Duration.millis(100), node);
        st.setFromX(1.1);
        st.setFromY(1.1);
        st.setToX(1);
        st.setToY(1);
        st.setAutoReverse(true);
        st.play();
    }

    public void scaleUp(MouseEvent mouseEvent) {
        Node node = (Node) mouseEvent.getSource();

        ScaleTransition st = new ScaleTransition(Duration.millis(100), node);
        st.setFromX(1);
        st.setFromY(1);
        st.setToX(1.1);
        st.setToY(1.1);
        st.setAutoReverse(true);
        st.play();

        Media media = new Media(getClass().getResource("/music/btn-hover.wav").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);

    }

    public void goProfileMenu(MouseEvent mouseEvent) throws Exception {
        new ProfileMenu().start(stage);
    }

    public void goSettingMenu(MouseEvent mouseEvent) throws Exception {
        new SettingMenu().start(stage);
    }


    public void goGameMenu(MouseEvent mouseEvent) {

        Game game = MainController.game;
        if (UserController.getCurrentUser().isGameSave()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(Translate.CONTINUE_GAME.getString());
            alert.setHeaderText(Translate.CONTINUE_GAME_QUESTION.getString());

            ButtonType yes = new ButtonType(Translate.YES.getString());
            ButtonType no = new ButtonType(Translate.NO.getString());
            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(yes, no);
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == yes) {
                MainController.game = UserController.getCurrentUser().getSavedGame();
                runTransitionToGame();
            } else if (option.get() == no) {
                game.resetGame();
                runTransitionToGame();
            }
        } else {
            runTransitionToGame();
        }


    }

    public void runTransitionToGame() {
        substructure.getChildren().remove(littleCircle);
        substructure.getChildren().add(littleCircle);
        ScaleTransition st = new ScaleTransition(Duration.millis(1000), littleCircle);
        st.setToX(100);
        st.setToY(100);
        st.setAutoReverse(false);
        st.play();
        st.setOnFinished(actionEvent -> {
            try {
                new GameMenu().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void goRankMenu(MouseEvent mouseEvent) throws Exception {
        new RankMenu().start(stage);
    }

    public void exit(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Translate.EXIT.getString());
        alert.setHeaderText(Translate.DO_YOU_WANT_TO_EXIT.getString());

        ButtonType yes = new ButtonType(Translate.YES.getString());
        ButtonType no = new ButtonType(Translate.NO.getString());
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(yes, no);
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == yes) {
            System.exit(0);
        }
    }
}
