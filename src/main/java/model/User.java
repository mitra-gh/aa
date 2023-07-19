package model;

import controller.UserController;
import enumoration.Paths;

import java.util.Random;

public class User {
    private String username;
    private String password;

    private String path;

    private Game lastGame;
    private Game savedGame;
    private int score;
    private int time;
    private int level;

    private boolean gameSave = false;

    private boolean guest = false;

    public User(String username, String password) {
        this.username = username;
        this.password = UserController.convertPasswordToHash(password);
        Random random = new Random();
        path = "files/img/avatars/"+(random.nextInt(16) + 1) + ".png";
        lastGame = new Game();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isPasswordCorrect(String pass) {
        pass = UserController.convertPasswordToHash(pass);
        return pass.hashCode() == password.hashCode();
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Game getLastGame() {
        return lastGame;
    }

    public void setLastGame(Game lastGame) {
        this.lastGame = lastGame;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPassword() {
        return password;
    }

    public boolean isGuest() {
        return guest;
    }

    public void setGuest(boolean guest) {
        this.guest = guest;
    }

    public void addScore(int value){
        score += value;
    }
    public void setPassword(String password) {
        this.password = UserController.convertPasswordToHash(password);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isGameSave() {
        return gameSave;
    }

    public void setGameSave(boolean gameSave) {
        this.gameSave = gameSave;
    }

    public Game getSavedGame() {
        return savedGame;
    }

    public void setSavedGame(Game savedGame) {
        this.savedGame = savedGame;
    }
}
