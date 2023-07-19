package view.menus;

import controller.UserController;
import enumoration.Translate;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class RankMenu extends Application {
    public static Stage stage;
    public BorderPane settingRoot;

    public static VBox layout;

    public Rectangle back;

    public int levelVal;

    @Override
    public void start(Stage stage) throws Exception {
        RankMenu.stage = stage;
        makeScene();
    }


    @FXML
    public void initialize() {
        layout = new VBox();
        layout.setId("layout");
        layout.setMaxWidth(460);
        layout.setMaxHeight(552);
        layout.setAlignment(Pos.CENTER);
        StackPane stackPane = new StackPane();
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(layout);
        stackPane.getChildren().add(borderPane);
        makeTable();
        borderPane = new BorderPane();
        stackPane.getChildren().add(borderPane);
        HBox hBox = new HBox();
        back = new Rectangle(30, 30);
        HBox.setMargin(back, new Insets(20, 0, 0, 20));
        hBox.getChildren().add(back);
        borderPane.setTop(hBox);
        back.setFill(new ImagePattern(new Image(SettingMenu.class.getResource("/images/icons/home.png").toExternalForm())));
        back.setOnMouseClicked(mouseEvent -> {
            try {
                goHome();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        back.setOnMouseEntered(this::scaleUp);
        back.setOnMouseExited(this::scaleDown);
        settingRoot.setCenter(stackPane);
    }

    private void makeTable() {

        layout.getChildren().clear();
        ArrayList<User> users = UserController.getSortedListOfUsers(levelVal);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setMaxWidth(400);
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        Pane pane = new Pane();
        Pane pane1 = new Pane();
        Pane pane2 = new Pane();
        Pane pane3 = new Pane();
        Pane pane4 = new Pane();
        Pane pane5 = new Pane();
        pane.getStyleClass().add("rankCell");
        pane1.getStyleClass().add("rankCell");
        pane2.getStyleClass().add("rankCell");
        pane3.getStyleClass().add("rankCell");
        pane4.getStyleClass().add("rankCell");
        pane5.getStyleClass().add("rankCell");

        Label rank = new Label(Translate.RANKING.getString());
        Label username = new Label(Translate.USERNAME.getString());
        Label score = new Label(Translate.SCORE.getString());
        Label time = new Label(Translate.TIME.getString());
        Label level = new Label(Translate.LEVEL.getString());
        pane.getChildren().add(rank);
        pane1.getChildren().add(username);
        pane2.getChildren().add(score);
        pane3.getChildren().add(time);
        pane4.getChildren().addAll(level);

        username.getStyleClass().add("settingLabel");
        score.getStyleClass().add("settingLabel");
        rank.getStyleClass().add("settingLabel");
        time.getStyleClass().add("settingLabel");
        level.getStyleClass().add("settingLabel");
        gridPane.add(pane5, 0, 0);
        gridPane.add(pane, 1, 0);
        gridPane.add(pane1, 2, 0);
        gridPane.add(pane2, 3, 0);
        gridPane.add(pane3, 4, 0);
        gridPane.add(pane4, 5, 0);
        vBox.getChildren().add(gridPane);
        layout.getChildren().add(vBox);
        for (int i = 1; i <= Math.min(users.size(), 10); i++) {
            User user = users.get(i - 1);
            username = new Label(user.getUsername());
            score = new Label(user.getScore() + "");
            time = new Label(user.getTime() + "");
            level = new Label(user.getLevel() + "");
            rank = new Label(i + "");
            pane = new Pane();
            pane1 = new Pane();
            pane2 = new Pane();
            pane3 = new Pane();
            pane4 = new Pane();
            pane5 = new Pane();
            pane.getStyleClass().add("rankCell");
            pane1.getStyleClass().add("rankCell");
            pane2.getStyleClass().add("rankCell");
            pane3.getStyleClass().add("rankCell");
            pane4.getStyleClass().add("rankCell");
            pane5.getStyleClass().add("rankCell");
            pane.getChildren().add(rank);
            pane1.getChildren().add(username);
            pane2.getChildren().add(score);
            pane3.getChildren().add(time);
            pane4.getChildren().add(level);
            if (i == 1) {
                username.setTextFill(Color.GOLD);
            } else if (i == 2) {
                username.setTextFill(Color.SILVER);
            } else if (i == 3) {
                username.setTextFill(Color.ORANGE);
            }
            gridPane.add(pane5, 0, i);
            gridPane.add(pane, 1, i);
            gridPane.add(pane1, 2, i);
            gridPane.add(pane2, 3, i);
            gridPane.add(pane3, 4, i);
            gridPane.add(pane4, 5, i);
        }
        if (users.size() == 0) {
            Label label = new Label(Translate.NO_USER.getString());
            label.getStyleClass().add("no-user");
            layout.getChildren().add(label);
        }
    }

    public void makeScene() throws IOException {
        BorderPane pane = FXMLLoader.load(
                new URL(Objects.requireNonNull(RankMenu.class.getResource("/fxml/rankTable.fxml")).toExternalForm()));
        Scene scene = new Scene(pane);
        scene.setOnKeyPressed(keyEvent -> {
            levelVal = (levelVal + 1) % 4;
            makeTable();
        });
        stage.setScene(scene);
        stage.show();
    }

    public void goHome() throws Exception {
        new MainMenu().start(stage);
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
}

