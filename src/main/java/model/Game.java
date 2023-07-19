package model;

import enumoration.Music;
import model.other.BallData;

import java.util.ArrayList;

public class Game {

    private String type = "solitude";
    private int hardshipLevel = 2;
    private int speed = 10;
    private double windSpeed = 1.5;
    private int iceTime = 5;
    private boolean muteSound = false;
    private boolean isBlackWhite = false;
    private boolean isPersian = false;
    private Music music = Music.LOBBY_TIME;
    private double musicVolume = 0.3;
    private int countOfBall = 10;
    //----------------game properties---------------------

    public ArrayList<BallData> defaultBalls = new ArrayList<>();
    public ArrayList<BallData> connectedBalls = new ArrayList<>();
    public int lastBallNum;
    public int phase = 1;
    public double iceValue = 0;

    public int mainCircleSpeed;
    public int radiusOfBall = 100;

    public int score = 0;
    public double degree = 0;

    public int remindTime = 90;

    public boolean gameSave = false;

    public Game(Game game) {
        this.type = game.type;
        this.hardshipLevel = game.hardshipLevel;
        this.speed = game.speed;
        this.windSpeed = game.windSpeed;
        this.iceTime = game.iceTime;
        this.muteSound = game.muteSound;
        this.isBlackWhite = game.isBlackWhite;
        this.isPersian = game.isPersian;
        this.music = game.music;
        this.musicVolume = game.musicVolume;
        this.countOfBall = game.countOfBall;
        this.defaultBalls = new ArrayList<>(game.defaultBalls);
        this.connectedBalls = new ArrayList<>(game.connectedBalls);
        this.lastBallNum = game.lastBallNum;
        this.phase = game.phase;
        this.iceValue = game.iceValue;
        this.mainCircleSpeed = game.mainCircleSpeed;
        this.radiusOfBall = game.radiusOfBall;
        this.score = game.score;
        this.degree = game.degree;
        this.remindTime = game.remindTime;
        this.gameSave = game.gameSave;
    }

    public Game() {}




    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHardshipLevel() {
        return hardshipLevel;
    }

    public void setHardshipLevel(int hardshipLevel) {
        if (hardshipLevel == 1) {
            speed = 5;
            windSpeed = 1.2;
            iceTime = 7;
        }
        if (hardshipLevel == 2) {
            speed = 10;
            windSpeed = 1.5;
            iceTime = 5;
        }
        if (hardshipLevel == 3) {
            speed = 15;
            windSpeed = 1.8;
            iceTime = 3;
        }
        this.hardshipLevel = hardshipLevel;
    }

    public boolean isMuteSound() {
        return muteSound;
    }

    public void setMuteSound(boolean muteSound) {
        this.muteSound = muteSound;
    }

    public boolean isBlackWhite() {
        return isBlackWhite;
    }

    public void setBlackWhite(boolean whiteBlack) {
        isBlackWhite = whiteBlack;
    }

    public boolean isPersian() {
        return isPersian;
    }

    public void setPersian(boolean persian) {
        isPersian = persian;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(double musicVolume) {
        this.musicVolume = musicVolume;
    }

    public int getCountOfBall() {
        return countOfBall;
    }

    public void setCountOfBall(int countOfBall) {
        this.countOfBall = countOfBall;
    }

    public int getSpeed() {
        return speed;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getIceTime() {
        return iceTime;
    }

    public void resetGame() {
        gameSave = false;
        lastBallNum = countOfBall;
        phase = 1;
        defaultBalls.clear();
        connectedBalls.clear();
        iceValue = 0;
        mainCircleSpeed = speed;
        radiusOfBall = 100;
        degree = 0;
        score = 0;
        remindTime = 90;
    }


}
