package view.menus;

import controller.MainController;
import enumoration.GameTypes;
import enumoration.Music;
import enumoration.Translate;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Game;
import org.controlsfx.control.ToggleSwitch;
import view.App;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class SettingMenu extends Application {

    public static Stage stage;
    public VBox settingContent;

    public BorderPane block;
    public VBox layout;
    public ScrollPane settingBlock;
    public RadioButton hard;
    public RadioButton average;
    public RadioButton easy;

    public RadioButton solitude;
    public RadioButton dense;
    public RadioButton messy;
    public ComboBox<String> cmb;
    public Slider volume;
    public Slider countOfBall;
    public ToggleSwitch persian;
    public ToggleSwitch withAndBlack;

    public Label settingLabel;
    public BorderPane settingRoot;
    public Rectangle back;

    @Override
    public void start(Stage stage) throws Exception {
        SettingMenu.stage = stage;
        makeScene();
    }

    @FXML
    public void initialize() {
        if (MainController.game.isBlackWhite()) {
            ColorAdjust c = new ColorAdjust();
            c.setSaturation(-1);
            settingRoot.setEffect(c);
        }

        settingLabel.setText(Translate.SETTING.getString());
        back.setFill(new ImagePattern(new Image(SettingMenu.class.getResource("/images/icons/home.png").toExternalForm())));
        HBox row1 = new HBox();
        row1.setSpacing(20);
        ToggleGroup toggleGroup = new ToggleGroup();
        Label label1 = new Label(Translate.HARDSHIP_LEVEL.getString() + ":");

        hard = new RadioButton(Translate.HARD.getString());
        average = new RadioButton(Translate.AVERAGE.getString());
        easy = new RadioButton(Translate.EASY.getString());
        hard.setToggleGroup(toggleGroup);
        average.setToggleGroup(toggleGroup);
        easy.setToggleGroup(toggleGroup);
        row1.getChildren().addAll(label1, hard, average, easy);
        row1.setPadding(new Insets(20, 0, 0, 40));
        label1.setPadding(new Insets(0, 20, 0, 0));

        updateHardshipLevel();
        toggleGroup.selectedToggleProperty().addListener(observable -> updateHardshipLevel());


        HBox row2 = new HBox();
        row2.setSpacing(20);
        Label label2 = new Label(Translate.AUDIO_SOUND.getString() + ":");
        volume = new Slider(0, 100, App.bgMusicPlayer.getVolume() * 100);
        row2.getChildren().addAll(label2, volume);
        row2.setPadding(new Insets(0, 0, 0, 40));
        label2.setPadding(new Insets(0, 20, 0, 0));
        volume.valueProperty().addListener((observable) -> updateMusicVolume());

        HBox row3 = new HBox();
        row3.setSpacing(20);
        Label label3 = new Label(Translate.COUNT_OF_BALL.getString() + ":");
        countOfBall = new Slider(5, 20, MainController.game.getCountOfBall());
        row3.getChildren().addAll(label3, countOfBall);
        row3.setPadding(new Insets(0, 0, 0, 40));
        label3.setPadding(new Insets(0, 20, 0, 0));
        countOfBall.valueProperty().addListener(observable -> updateCountOfBall());
        countOfBall.setMajorTickUnit(1);


        HBox row4 = new HBox();
        row4.setSpacing(20);
        Label label4 = new Label(Translate.BLACK_WHITE.getString() + ":");
        withAndBlack = new ToggleSwitch();
        row4.getChildren().addAll(label4, withAndBlack);
        row4.setPadding(new Insets(0, 0, 0, 40));
        label4.setPadding(new Insets(0, 20, 0, 0));
        withAndBlack.setSelected(MainController.game.isBlackWhite());
        withAndBlack.selectedProperty().addListener(observable -> {
            try {
                updateSwitchBlackWhite();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        HBox row5 = new HBox();
        row5.setSpacing(20);
        Label label5 = new Label(Translate.PERSIAN.getString() + ":");
        persian = new ToggleSwitch();
        row5.getChildren().addAll(label5, persian);
        row5.setPadding(new Insets(0, 0, 0, 40));
        label5.setPadding(new Insets(0, 20, 0, 0));
        persian.setSelected(MainController.game.isPersian());
        persian.selectedProperty().addListener(observable -> {
            try {
                updateSwitchPersian();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        HBox row6 = new HBox();
        row6.setSpacing(20);
        ToggleGroup toggleGroup1 = new ToggleGroup();
        Label label6 = new Label(Translate.PLAN.getString() + ":");

        solitude = new RadioButton(Translate.SOLITUDE.getString());
        dense = new RadioButton(Translate.DENSE.getString());
        messy = new RadioButton(Translate.MESSY.getString());
        solitude.setToggleGroup(toggleGroup1);
        dense.setToggleGroup(toggleGroup1);
        messy.setToggleGroup(toggleGroup1);
        row6.getChildren().addAll(label6, solitude, dense, messy);
        row6.setPadding(new Insets(0, 0, 0, 40));
        label6.setPadding(new Insets(0, 20, 0, 0));
        updatePlan();
        toggleGroup1.selectedToggleProperty().addListener(observable -> updatePlan());


        HBox row7 = new HBox();
        row7.setSpacing(20);
        Label label7 = new Label(Translate.SELECT_MUSIC.getString() + ":");
        cmb = new ComboBox<>();
        cmb.getItems().addAll(
                Music.LOBBY_TIME.getName(),
                Music.WALLPAPER.getName(),
                Music.CLOWN.getName(),
                Music.SNEAKY_SNITCH.getName()
        );
        cmb.setValue(MainController.game.getMusic().getName());
        row7.getChildren().addAll(label7, cmb);
        row7.setPadding(new Insets(0, 0, 40, 40));
        label7.setPadding(new Insets(0, 20, 0, 0));
        cmb.valueProperty().addListener(observable -> updateMusic());


        back.setOnMouseEntered(this::scaleUp);
        back.setOnMouseExited(this::scaleDown);

        label1.getStyleClass().add("settingLabel");
        label2.getStyleClass().add("settingLabel");
        label3.getStyleClass().add("settingLabel");
        label4.getStyleClass().add("settingLabel");
        label5.getStyleClass().add("settingLabel");
        label6.getStyleClass().add("settingLabel");
        label7.getStyleClass().add("settingLabel");

        settingContent.getChildren().addAll(row1, row2, row3, row4, row5, row6, row7);
    }

    public void updateHardshipLevel() {
        Game game = MainController.game;
        if (hard.isSelected()) {
            MainController.game.setHardshipLevel(3);
        } else if (average.isSelected()) {
            MainController.game.setHardshipLevel(2);
        } else if (easy.isSelected()) {
            MainController.game.setHardshipLevel(1);
        } else {
            hard.setSelected(game.getHardshipLevel() == 3);
            average.setSelected(game.getHardshipLevel() == 2);
            easy.setSelected(game.getHardshipLevel() == 1);
            return;
        }
    }

    public void updatePlan() {
        Game game = MainController.game;
        if (solitude.isSelected()) {
            MainController.game.setType(GameTypes.SOLITUDE.getValue());
        } else if (dense.isSelected()) {
            MainController.game.setType(GameTypes.DENSE.getValue());
        } else if (messy.isSelected()) {
            MainController.game.setType(GameTypes.MESSY.getValue());
        } else {
            solitude.setSelected(Objects.equals(game.getType(), GameTypes.SOLITUDE.getValue()));
            dense.setSelected(Objects.equals(game.getType(), GameTypes.DENSE.getValue()));
            messy.setSelected(Objects.equals(game.getType(), GameTypes.MESSY.getValue()));
        }
    }

    public void updateMusicVolume() {
        double val = volume.getValue();
        App.bgMusicPlayer.setVolume(val / 100);
        MainController.game.setMusicVolume(val / 100);
    }

    public void updateMusic() {
        if (cmb.getValue().equals(Music.LOBBY_TIME.getName())) {
            MainController.game.setMusic(Music.LOBBY_TIME);
        } else if (cmb.getValue().equals(Music.WALLPAPER.getName())) {
            MainController.game.setMusic(Music.WALLPAPER);
        } else if (cmb.getValue().equals(Music.CLOWN.getName())) {
            MainController.game.setMusic(Music.CLOWN);
        } else if (cmb.getValue().equals(Music.SNEAKY_SNITCH.getName())) {
            MainController.game.setMusic(Music.SNEAKY_SNITCH);
        }
        App.setMusic();
    }

    public void updateCountOfBall() {
        double val = countOfBall.getValue();
        MainController.game.setCountOfBall((int) val);
    }

    public void updateSwitchPersian() throws Exception {
        MainController.game.setPersian(persian.isSelected());
        new SettingMenu().start(stage);
    }

    public void updateSwitchBlackWhite() throws Exception {
        MainController.game.setBlackWhite(withAndBlack.isSelected());
        new SettingMenu().start(stage);
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

    public void makeScene() throws IOException {
        BorderPane pane = FXMLLoader.load(
                new URL(Objects.requireNonNull(LoginMenu.class.getResource("/fxml/settingMenu.fxml")).toExternalForm()));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    public void goMainMenu(MouseEvent mouseEvent) throws Exception {
        new MainMenu().start(stage);
    }
}
