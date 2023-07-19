package view;

import controller.DBController;
import controller.MainController;
import controller.UserController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.menus.LoginMenu;
import view.menus.MainMenu;
import view.menus.RegisterMenu;

public class App extends Application {

    public static Media bgMusic;
    public static MediaPlayer bgMusicPlayer;

    public static void setMusic(){
        if (bgMusicPlayer != null){
            bgMusicPlayer.stop();
        }
        Media media = new Media(App.class.getResource(MainController.game.getMusic().getPath()).toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        mediaPlayer.setVolume(MainController.game.getMusicVolume());
        bgMusic = media;
        bgMusicPlayer = mediaPlayer;

    }
    @Override
    public void start(Stage stage) throws Exception {
        MainController.loadGame();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DBController.saveAllUsers();
            DBController.saveCurrentUser();
        }));
        setMusic();
        stage.getIcons().add(new Image(getClass().getResource("/images/logo.png").toExternalForm()));
        if (UserController.getCurrentUser() != null) {
            new MainMenu().start(stage);
        } else if (UserController.getUsers().size() == 0) {
            new RegisterMenu().start(stage);
        } else {
            new LoginMenu().start(stage);
        }
    }
}
