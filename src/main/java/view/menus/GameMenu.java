package view.menus;

import controller.GameController;
import controller.MainController;
import controller.UserController;
import enumoration.Music;
import enumoration.Translate;
import javafx.animation.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Game;
import model.game.Ball;
import model.game.MainCircle;
import model.other.BallData;
import view.App;
import view.animations.MainCircleTransition;
import view.animations.ShootingAnimation;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class GameMenu extends Application {
    public static Stage stage;
    public static Scene scene;
    public StackPane mainRoot;
    public static StackPane substructure;
    public Rectangle back;
    public static Rectangle pause;
    public static Pane ballsPane;
    public static ArrayList<Ball> balls = new ArrayList<>();
    public static Label remindLabel;
    public static Label remindVal;
    public static ProgressBar iceTime;
    public static Label timer;
    public static Label scoreVal;
    public static Label scoreLabel;
    public static Label degreeLabel;
    public static Label degreeVal;
    public static Label phaseLabel;

    //=======pauseMenu
    public static BorderPane pauseMenu;
    public static BorderPane guideMenu;
    public static Slider volume;

    public static ComboBox<String> cmb;

    public static Button home;
    public static Button restart;
    public static Button resume;
    public static Button continueBtn;

    public VBox gameData;
    public static MainCircle mainCircle;
    Ball targetBall;
    Game game;
    public static ArrayList<Transition> transitions = new ArrayList<>();
    public static ArrayList<Timeline> timelines = new ArrayList<>();
    boolean canUseIce = false;
    int changedX = 0;
    public Rectangle pointer;

    public static Media media;
    public static MediaPlayer mediaPlayer;

    @Override
    public void start(Stage stage) throws Exception {
        GameMenu.stage = stage;
        GameController.gameMenu = this;
        game = MainController.game;
        if (!game.gameSave) {
            game.resetGame();
        }
        makeScene();
    }

    @FXML
    public void initialize() {
        if (MainController.game.isBlackWhite()) {
            ColorAdjust c = new ColorAdjust();
            c.setSaturation(-1);
            mainRoot.setEffect(c);
        }
        game = MainController.game;
        if (!game.gameSave) {
            game.resetGame();
        }

        pause = back;
        back.setFill(new ImagePattern(new Image(SettingMenu.class.getResource("/images/icons/pause.png").toExternalForm())));
        back.setOnMouseEntered(this::scaleUp);
        back.setOnMouseExited(this::scaleDown);

        iceTime = new ProgressBar(game.iceValue);
        if (game.iceValue > 0.9){
            canUseIce = true;
        }
        remindLabel = new Label(Translate.REMIND_BALL.getString() + ": ");
        remindVal = new Label(game.lastBallNum + "");
        remindVal.setTextFill(Color.RED);
        remindLabel.setTextFill(Color.RED);

        timer = new Label();
        HBox hBox = new HBox();
        hBox.getChildren().addAll(remindLabel, remindVal);

        scoreLabel = new Label(Translate.SCORE.getString() + ": ");
        scoreVal = new Label(game.score + "");
        HBox hBox1 = new HBox();
        hBox1.getChildren().addAll(scoreLabel, scoreVal);


        phaseLabel = new Label(Translate.PHASE.getString() + " " + game.phase);
        phaseLabel.setTextFill(Color.PALEVIOLETRED);


        degreeVal = new Label();
        degreeLabel = new Label();
        degreeLabel.setText(Translate.DEGREE.getString() + ": ");
        DecimalFormat df = new DecimalFormat("##.##");
        degreeVal.setText(String.valueOf(df.format(game.degree)));
        HBox hBox2 = new HBox();
        hBox2.getChildren().addAll(degreeLabel, degreeVal);


        VBox vBox = new VBox();
        vBox.getChildren().addAll(phaseLabel, timer, hBox, hBox1, hBox2, iceTime);
        gameData.getChildren().add(vBox);
        vBox.setPadding(new Insets(30));
        vBox.setMaxHeight(600);
        vBox.setMinWidth(200);
        vBox.setSpacing(20);
        vBox.getStyleClass().add("game-data");
        VBox.setMargin(vBox, new Insets(100, 0, 0, 40));
    }

    public void makeScene() throws IOException {
        StackPane pane = FXMLLoader.load(
                new URL(Objects.requireNonNull(MainMenu.class.getResource("/fxml/game.fxml")).toExternalForm()));
        mainRoot = pane;
        substructure = new StackPane();
        ballsPane = new Pane();
        pane.getChildren().add(substructure);
        substructure.getChildren().add(ballsPane);

        mainCircle = new MainCircle(50);
        mainCircle.setId("MainMenuCircle");
        if (game.gameSave) {
            loadMainCircle();
        }

        substructure.getChildren().addAll(mainCircle);

        initializeBalls();
        scene = new Scene(pane);


        stage.setScene(scene);
        stage.show();
        firstTransition();
    }

    public void firstTransition(){
        mainCircle.circle.setScaleX(100);
        mainCircle.circle.setScaleY(100);
        mainCircle.getChildren().remove(mainCircle.firstPane);
        mainCircle.getChildren().add(mainCircle.firstPane);
        ScaleTransition st = new ScaleTransition(Duration.millis(1000), mainCircle.circle);
        st.setFromX(100);
        st.setFromY(100);
        st.setToX(1);
        st.setToY(1);
        st.setAutoReverse(false);
        st.play();
        st.setOnFinished(actionEvent -> {
            mainRoot.getChildren().remove(substructure);
            mainRoot.getChildren().add(0,substructure);
            for (Node n : mainCircle.getChildren()) {
                if (n instanceof Pane p) {
                    MainCircleTransition transition = new MainCircleTransition(mainCircle, p);
                    transition.play();
                    transitions.add(transition);
                }
            }
            makeTimer();
            updatePhase();
            setOnKeyPressedOfScene();
        });
    }

    public void setOnKeyPressedOfScene(){
        scene.setOnKeyPressed(keyEvent -> {
            String keyName = keyEvent.getCode().getName();
            if (keyName.equals("Tab")) {
                if (canUseIce) {
                    iceScreen();
                    canUseIce = false;
                    iceTime.setProgress(0);
                }

            }
            if (keyName.equals("Space")) {
                media = new Media(getClass().getResource("/music/shooting.mp3").toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setAutoPlay(true);
                ShootingAnimation animation = new ShootingAnimation(mainCircle, targetBall, ballsPane, balls);
                if (game.phase == 4) {
                    animation.degree = game.degree;
                }
                animation.play();
                transitions.add(animation);
            }
            if (keyName.equals("Right")) {
                if (game.phase == 4) {
                    moveBallsRight();
                }
            }

            if (keyName.equals("Left")) {
                if (game.phase == 4) {
                    moveBallLeft();
                }
            }
            if (keyName.equals("Up")) {
                pause();
            }

            if (keyName.equals("Down")) {
                resume();
            }


        });
    }
    public void loseGame() {
        stop();
        mainRoot.setStyle("-fx-background-color: #ff0000");
        media = new Media(getClass().getResource("/music/lose.mp3").toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        showAll();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> showEnd(false)));
        clearStaticMethod();
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void initializeBalls() {
        Ball ball = new Ball(500, 615, 10);
        Ball ball1 = new Ball(500, 645, 10);
        Ball ball2 = new Ball(500, 675, 10);
        Ball ball3 = new Ball(500, 705, 10);
        Ball ball4 = new Ball(500, 735, 10);

        pointer = new Rectangle(492.5, 555, 15, 45);
        pointer.setFill(new ImagePattern(new Image(GameMenu.class.getResource("/images/laser.png").toExternalForm())));
        pointer.setOpacity(0);
        ball.setNum(game.lastBallNum);
        ball1.setNum(game.lastBallNum - 1);
        ball2.setNum(game.lastBallNum - 2);
        ball3.setNum(game.lastBallNum - 3);
        ball4.setNum(game.lastBallNum - 4);
        balls.add(ball);
        balls.add(ball1);
        balls.add(ball2);
        balls.add(ball3);
        balls.add(ball4);
        for (int i = 0; i < Math.min(5, game.lastBallNum); i++) {
            ballsPane.getChildren().addAll(balls.get(i), balls.get(i).text);
        }
        ballsPane.getChildren().add(pointer);
        targetBall = ball;
    }

    public void updateBall() {

        game.lastBallNum--;
        checkPhases();
        updateData();

        if (game.lastBallNum == 0) {
            winGame();
            return;
        }
        targetBall = balls.get(0);

        for (Ball ball : balls) {
            ball.setCenterY(ball.getCenterY() - 30);
            ball.text.setY(ball.text.getY() - 30);
        }

        if (game.lastBallNum >= 5) {
            Ball ball = new Ball(500 + changedX, 735, 10);
            ball.setNum(game.lastBallNum - 4);
            ballsPane.getChildren().addAll(ball, ball.text);
            balls.add(ball);
        }
    }

    public void updateData() {
        game.score += game.getHardshipLevel();
        scoreVal.setText(game.score + "");
        remindVal.setText(String.valueOf(game.lastBallNum));

        double val = iceTime.getProgress();
        if (val + 0.2 < 1) {
            iceTime.setProgress(iceTime.getProgress() + 0.2);
        } else {
            canUseIce = true;
            iceTime.setProgress(1);
            iceTransition();
        }
    }

    public void moveBallsRight() {
        if (500 + changedX + 10 >= 990) {
            return;
        }
        changedX += 10;
        for (Ball ball : balls) {
            ball.setCenterX(500 + changedX);
            ball.text.setX(500 + changedX - 5);
        }
        pointer.setX(492.5 + changedX);
    }

    public void moveBallLeft() {
        if (500 + changedX - 10 <= 10) {
            return;
        }
        changedX -= 10;
        for (Ball ball : balls) {
            ball.setCenterX(500 + changedX);
            ball.text.setX(500 + changedX - 5);
        }
        pointer.setX(492.5 + changedX);
    }

    public void checkPhases() {
        int phase2 = (int) (Math.round((double) game.getCountOfBall() / 4) * 3);
        int phase3 = game.getCountOfBall() / 2;
        int phase4 = game.getCountOfBall() / 4;
        if (game.lastBallNum > phase2) {
            return;
        }
        if (game.lastBallNum == phase2 && game.phase != 2) {
            game.phase = 2;
            remindVal.setTextFill(Color.ORANGE);
            remindLabel.setTextFill(Color.ORANGE);
            phaseLabel.setText(Translate.PHASE.getString() + " " + game.phase);
            phaseLabel.setTextFill(Color.ORANGERED);
            changeDirection();
            changeSize();
        }
        if (game.lastBallNum == phase3 && game.phase != 3) {
            game.phase = 3;
            remindVal.setTextFill(Color.GOLD);
            remindLabel.setTextFill(Color.GOLD);
            phaseLabel.setText(Translate.PHASE.getString() + " " + game.phase);
            phaseLabel.setTextFill(Color.BLUEVIOLET);
            changeOpacity();
        }
        if (game.lastBallNum == phase4 && game.phase != 4) {
            game.phase = 4;
            remindVal.setTextFill(Color.GREEN);
            remindLabel.setTextFill(Color.GREEN);
            phaseLabel.setText(Translate.PHASE.getString() + " " + game.phase);
            phaseLabel.setTextFill(Color.INDIANRED);
            game.degree = 15;
            pointer.setRotate(game.degree);
            pointer.setOpacity(1);
            changeDegree();
            DecimalFormat df = new DecimalFormat("##.##");
            degreeVal.setText(String.valueOf(df.format(game.degree)));
        }
    }

    public void makeTimer() {
        makeTimerLabel(game.remindTime / 60, game.remindTime % 60);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
            game.remindTime--;
            makeTimerLabel(game.remindTime / 60, game.remindTime % 60);
            if (game.remindTime == 0) {
                this.stop();
                GameController.showLose();
            }
        }));
        timelines.add(timeline);
        timeline.setCycleCount(90);
        timeline.play();
    }

    public void makeTimerLabel(int min, int second) {
        if (second < 10) {
            timer.setText("0" + min + " : 0" + second);
        } else {
            timer.setText("0" + min + " : " + second);
        }
    }

    public void winGame() {
        stop();
        mainRoot.setStyle("-fx-background-color: #00ff0d");
        media = new Media(getClass().getResource("/music/win.mp3").toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> showEnd(true)));
        showAll();
        clearStaticMethod();
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void changeDirection() {
        Random random = new Random();
        int time = random.nextInt(3) + 4;
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(time * 1000), actionEvent -> {
            mainCircle.speed *= (-1);
            changeDirection();
        }));
        timelines.add(timeline);
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void changeSize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
            if (mainCircle.getRadiusOfBalls() == 100) {
                mainCircle.setRadiusOfBalls(110);
            } else {
                mainCircle.setRadiusOfBalls(100);
            }

            int radiusOfBall = mainCircle.radius + mainCircle.getRadiusOfBalls();


            for (Ball ball : mainCircle.getNewBalls()) {
                double degree = Math.atan((ball.getCenterY() - 375) / (ball.getCenterX() - 500));
                double x = Math.cos(degree) * radiusOfBall;
                double y = Math.sin(degree) * radiusOfBall;

                if (Math.signum(Math.cos(degree)) != Math.signum(ball.getCenterX() - 500)) {
                    x *= (-1);
                }
                if (Math.signum(ball.getCenterY() - 375) != Math.signum(Math.sin(degree))) {
                    y *= (-1);
                }
                x += 500;
                y += 375;
                ball.setCenterX(x);
                ball.setCenterY(y);
                ball.text.setX(x - 5);
                ball.text.setY(y + 5);
                ball.line.setStartX(x);
                ball.line.setStartY(y);
            }

            for (Ball ball : mainCircle.getDefaultBalls()) {
                double degree = Math.atan((ball.getCenterY() - 375) / (ball.getCenterX() - 500));
                double x = Math.cos(degree) * radiusOfBall;
                double y = Math.sin(degree) * radiusOfBall;

                if (Math.signum(Math.cos(degree)) != Math.signum(ball.getCenterX() - 500)) {
                    x *= (-1);
                }
                if (Math.signum(ball.getCenterY() - 375) != Math.signum(Math.sin(degree))) {
                    y *= (-1);
                }
                x += 500;
                y += 375;
                ball.setCenterX(x);
                ball.setCenterY(y);
                ball.line.setStartX(x);
                ball.line.setStartY(y);
            }
        }));
        timelines.add(timeline);
        timeline.setCycleCount(-1);
        timeline.play();
    }

    public void changeOpacity() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
            for (Ball ball : mainCircle.getNewBalls()) {
                if (ball.getOpacity() == 0) {
                    ball.setOpacity(1);
                    ball.text.setOpacity(1);
                    ball.line.setOpacity(1);
                } else {
                    ball.setOpacity(0);
                    ball.text.setOpacity(0);
                    ball.line.setOpacity(0);
                }
            }

            for (Ball ball : mainCircle.getDefaultBalls()) {
                if (ball.getOpacity() == 0) {
                    ball.setOpacity(1);
                    ball.line.setOpacity(1);
                } else {
                    ball.setOpacity(0);
                    ball.line.setOpacity(0);
                }
            }
        }));
        timelines.add(timeline);
        timeline.setCycleCount(-1);
        timeline.play();
    }

    public void iceScreen() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), actionEvent -> {
            int sign = (int) Math.signum(mainCircle.speed);
            int speed = (Math.abs(mainCircle.speed) - 1) * sign;
            mainCircle.speed = speed;
            if (speed == 0) {
                media = new Media(getClass().getResource("/music/freeze.mp3").toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setAutoPlay(true);
                mainCircle.getChildren().remove(mainCircle.firstPane);
                mainCircle.getChildren().add(mainCircle.firstPane);
                try {
                    deleteIceState();
                    timelines.remove(this);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }));
        timelines.add(timeline);
        timeline.setCycleCount(Math.abs(mainCircle.speed));
        timeline.play();
    }

    public void deleteIceState() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(game.getIceTime() * 1000), actionEvent -> {
            mainCircle.speed = game.getSpeed();
        }));
        timelines.add(timeline);
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void pause() {
        for (Transition transition : transitions) {
            transition.pause();
        }
        for (Timeline timeline : timelines) {
            timeline.pause();
        }
    }

    public void resume() {
        for (Transition transition : transitions) {
            transition.play();
        }
        for (Timeline timeline : timelines) {
            timeline.play();
        }
    }

    public void stop() {
        for (Transition transition : transitions) {
            transition.stop();
        }
        for (Timeline timeline : timelines) {
            timeline.stop();
        }
        transitions.clear();
        timelines.clear();
        scene.setOnKeyPressed(null);
        pause.setOnMouseClicked(null);
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

    public void showAll() {
        for (Ball ball : mainCircle.getNewBalls()) {
            ball.setOpacity(1);
            ball.text.setOpacity(1);
            ball.line.setOpacity(1);
        }

        for (Ball ball : mainCircle.getDefaultBalls()) {
            ball.setOpacity(1);
            ball.line.setOpacity(1);
        }
    }

    public void showPauseMenu(MouseEvent mouseEvent) {
        pause();
        pauseMenu = new BorderPane();
        VBox root = new VBox();
        root.setId("layout");
        root.setMaxWidth(460);
        root.setMaxHeight(552);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(40);


        Rectangle rectangle = new Rectangle(20, 20, 30, 30);
        rectangle.setFill(new ImagePattern(new Image(GameMenu.class.getResource("/images/icons/info.png").toExternalForm())));
        rectangle.setOnMouseEntered(this::scaleUp);
        rectangle.setOnMouseExited(this::scaleDown);
        rectangle.setOnMouseClicked(this::showGuide);
        HBox row1 = new HBox(rectangle);
        HBox.setMargin(rectangle, new Insets(0, 0, 0, 30));


        Label label = new Label(Translate.SETTING.getString());
        label.setId("settingLabel");
        label.getStyleClass().add("headLabel");
        VBox.setMargin(label, new Insets(0, 0, 20, 0));
        HBox row2 = new HBox();
        row2.setSpacing(20);
        Label label2 = new Label(Translate.AUDIO_SOUND.getString() + ":");
        volume = new Slider(0, 100, App.bgMusicPlayer.getVolume() * 100);
        row2.getChildren().addAll(label2, volume);
        row2.setPadding(new Insets(0, 0, 0, 40));
        label2.setPadding(new Insets(0, 20, 0, 0));
        volume.valueProperty().addListener((observable) -> updateMusicVolume());


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
        row7.setPadding(new Insets(0, 0, 0, 40));
        label7.setPadding(new Insets(0, 20, 0, 0));
        cmb.valueProperty().addListener(observable -> updateMusic());

        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER);
        home = new Button(Translate.HOME.getString());
        resume = new Button(Translate.RESUME.getString());
        restart = new Button(Translate.RESTART.getString());
        home.getStyleClass().add("submit");
        restart.getStyleClass().add("submit");
        resume.getStyleClass().add("submit");
        home.setMaxWidth(150);
        restart.setMaxWidth(150);
        resume.setMaxWidth(150);
        hBox.getChildren().addAll(home, restart, resume);

        home.setOnMouseEntered(this::scaleUp);
        restart.setOnMouseEntered(this::scaleUp);
        resume.setOnMouseEntered(this::scaleUp);
        home.setOnMouseExited(this::scaleDown);
        restart.setOnMouseExited(this::scaleDown);
        resume.setOnMouseExited(this::scaleDown);

        home.setOnMouseClicked(mouseEvent12 -> {
            try {
                goHome();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        resume.setOnMouseClicked(mouseEvent1 -> {
            mainRoot.getChildren().remove(pauseMenu);
            resume();
        });

        restart.setOnMouseClicked(mouseEvent1 -> {
            clearStaticMethod();
            transitions.clear();
            timelines.clear();
            game.resetGame();
            try {
                new GameMenu().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        label2.getStyleClass().add("settingLabel");
        label7.getStyleClass().add("settingLabel");


        pauseMenu.setCenter(root);
        pauseMenu.setId("pauseMenu");
        root.getChildren().addAll(row1, label, row2, row7, hBox);
        mainRoot.getChildren().add(pauseMenu);

    }

    private void showGuide(MouseEvent mouseEvent) {
        guideMenu = new BorderPane();
        VBox root = new VBox();
        root.setId("layout");
        root.setMaxWidth(400);
        root.setMaxHeight(400);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(30);

        Label label = new Label(Translate.GUIDE.getString());
        label.getStyleClass().add("headLabel");


        Label label1 = new Label(Translate.SHOOTING.getString() + ": ");
        Label label2 = new Label("space");
        HBox row1 = new HBox(label1, label2);

        Label label3 = new Label(Translate.ICE_STATE.getString() + ": ");
        Label label4 = new Label("tab");
        HBox row2 = new HBox(label3, label4);

        Label label5 = new Label(Translate.MOVING_RIGHT.getString() + ": ");
        Label label6 = new Label("right");
        HBox row3 = new HBox(label5, label6);

        Label label7 = new Label(Translate.MOVING_LEFT.getString() + ": ");
        Label label8 = new Label("left");
        HBox row4 = new HBox(label7, label8);


        label1.getStyleClass().add("settingLabel");
        label3.getStyleClass().add("settingLabel");
        label5.getStyleClass().add("settingLabel");
        label7.getStyleClass().add("settingLabel");

        row1.setAlignment(Pos.CENTER);
        row2.setAlignment(Pos.CENTER);
        row3.setAlignment(Pos.CENTER);
        row4.setAlignment(Pos.CENTER);


        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER);
        continueBtn = new Button(Translate.BACK.getString());
        continueBtn.getStyleClass().add("submit");
        continueBtn.setMaxWidth(150);
        hBox.getChildren().addAll(continueBtn);
        continueBtn.setOnMouseEntered(this::scaleUp);
        continueBtn.setOnMouseExited(this::scaleDown);

        continueBtn.setOnMouseClicked(mouseEvent12 -> {
            mainRoot.getChildren().remove(guideMenu);
        });

        guideMenu.setCenter(root);
        guideMenu.setId("guideMenu");
        root.getChildren().addAll(label, row1, row2, row3, row4, continueBtn);
        mainRoot.getChildren().add(guideMenu);
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

    public void goHome() throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Translate.SAVE.getString());
        alert.setHeaderText(Translate.SAVE_DIALOG.getString());

        ButtonType yes = new ButtonType(Translate.YES.getString());
        ButtonType no = new ButtonType(Translate.NO.getString());
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(yes, no);
        Optional<ButtonType> option = alert.showAndWait();


        if (option.get() == yes) {
            saveGame();
            game.resetGame();
            clearStaticMethod();
            new MainMenu().start(stage);
        } else if (option.get() == no) {
            game.resetGame();
            clearStaticMethod();
            new MainMenu().start(stage);
        }

    }


    public void loadMainCircle() {
        GameController.loadMainCircle(mainCircle, game);
    }

    public void saveGame() {
        game.gameSave = true;
        UserController.getCurrentUser().setGameSave(true);
        game.iceValue = iceTime.getProgress();
        game.radiusOfBall = mainCircle.getRadiusOfBalls();
        game.defaultBalls.clear();
        game.connectedBalls.clear();
        for (Ball ball : mainCircle.getDefaultBalls()) {
            BallData ballData = new BallData(ball.getCenterX(), ball.getCenterY());
            ballData.rotate = ball.pane.getRotate();
            game.defaultBalls.add(ballData);
        }
        for (Ball ball : mainCircle.getNewBalls()) {
            BallData ballData = new BallData(ball.getNum(), ball.getCenterX(), ball.getCenterY());
            ballData.rotate = ball.pane.getRotate();
            game.connectedBalls.add(ballData);
        }
        UserController.getCurrentUser().setSavedGame(new Game(game));
        MainController.game = UserController.getCurrentUser().getLastGame();

    }

    public void clearStaticMethod() {
        remindLabel = null;
        mainCircle = null;
        remindVal = null;
        scoreVal = null;
        scoreLabel = null;
        iceTime = null;
        timer = null;
        degreeLabel = null;
        degreeVal = null;
        phaseLabel = null;
        ballsPane = null;
        pause = null;
        balls = new ArrayList<>();
        clearPauseMenu();
    }

    public void clearPauseMenu() {
        pauseMenu = null;
        home = null;
        restart = null;
        resume = null;
        volume = null;
        cmb = null;
    }

    public void updatePhase() {
        int phase2 = (int) (Math.round((double) game.getCountOfBall() / 4) * 3);
        int phase3 = game.getCountOfBall() / 2;
        int phase4 = game.getCountOfBall() / 4;
        if (game.lastBallNum <= phase4) {
            game.phase = 4;
            remindVal.setTextFill(Color.GREEN);
            remindLabel.setTextFill(Color.GREEN);
            phaseLabel.setText(Translate.PHASE.getString() + " " + game.phase);
            phaseLabel.setTextFill(Color.INDIANRED);
            changeDirection();
            changeSize();
            changeOpacity();
            changeDegree();
            game.degree = 15;
            pointer.setRotate(game.degree);
            pointer.setOpacity(1);
            DecimalFormat df = new DecimalFormat("##.##");
            degreeVal.setText(String.valueOf(df.format(game.degree)));
            return;
        }
        if (game.lastBallNum <= phase3) {
            game.phase = 3;
            remindVal.setTextFill(Color.GOLD);
            remindLabel.setTextFill(Color.GOLD);
            phaseLabel.setText(Translate.PHASE.getString() + " " + game.phase);
            phaseLabel.setTextFill(Color.BLUEVIOLET);
            changeOpacity();
            changeDirection();
            changeSize();
            return;
        }
        if (game.lastBallNum <= phase2) {
            game.phase = 2;
            remindVal.setTextFill(Color.ORANGE);
            remindLabel.setTextFill(Color.ORANGE);
            phaseLabel.setText(Translate.PHASE.getString() + " " + game.phase);
            phaseLabel.setTextFill(Color.ORANGERED);
            changeDirection();
            changeSize();
        }


    }

    public void changeDegree() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(5000), actionEvent -> {
            game.degree -= 4 * game.getWindSpeed();
            if (game.degree <= -15) {
                game.degree = 15;
            }
            pointer.setRotate(game.degree);
            DecimalFormat df = new DecimalFormat("##.##");
            degreeVal.setText(String.valueOf(df.format(game.degree)));
        }));
        timelines.add(timeline);
        timeline.setCycleCount(-1);
        timeline.play();
    }

    public void showEnd(boolean win) {
        int newScore = game.score;
        if (win) {
            newScore += game.remindTime;
        }
        if (newScore > UserController.getCurrentUser().getScore()) {
            UserController.getCurrentUser().setLevel(game.getHardshipLevel());
            UserController.getCurrentUser().setScore(newScore);
            UserController.getCurrentUser().setTime(game.remindTime);
        }
        if (game.gameSave){
            UserController.getCurrentUser().setGameSave(false);
            UserController.getCurrentUser().setSavedGame(null);
            MainController.game = UserController.getCurrentUser().getLastGame();

        }
        game.resetGame();
        pauseMenu = new BorderPane();
        VBox root = new VBox();
        root.setId("layout");
        root.setMaxWidth(400);
        root.setMaxHeight(400);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(40);


        Label label = new Label();
        label.getStyleClass().add("headScore");
        if (win) {
            label.setTextFill(Color.GREEN);
            label.setText(Translate.WIN.getString());
        } else {
            label.setTextFill(Color.RED);
            label.setText(Translate.LOSE.getString());
        }

        Label label2 = new Label(newScore + "");

        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER);
        continueBtn = new Button(Translate.CONTINUE.getString());
        continueBtn.getStyleClass().add("submit");
        continueBtn.setMaxWidth(150);
        hBox.getChildren().addAll(continueBtn);

        continueBtn.setOnMouseEntered(this::scaleUp);
        continueBtn.setOnMouseExited(this::scaleDown);

        continueBtn.setOnMouseClicked(mouseEvent12 -> {
            try {
                new RankMenu().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        label2.getStyleClass().add("score");

        pauseMenu.setCenter(root);
        pauseMenu.setId("pauseMenu");
        root.getChildren().addAll(label, label2, hBox);
        mainRoot.getChildren().add(pauseMenu);
    }

    public void iceTransition(){
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setNode(iceTime);
        fadeTransition.setDuration(Duration.millis(200));
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setCycleCount(6);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
        fadeTransition.setOnFinished(actionEvent -> iceTime.setOpacity(1));
        transitions.add(fadeTransition);
    }
}
