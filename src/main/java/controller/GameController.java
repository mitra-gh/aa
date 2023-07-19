package controller;

import javafx.animation.Transition;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import model.Game;
import model.game.Ball;
import model.game.MainCircle;
import model.other.BallData;
import view.menus.GameMenu;

public class GameController {
    public static boolean lose = false;
    public static boolean win = false;
    public static GameMenu gameMenu;

    public static void showLose() {
        gameMenu.loseGame();
    }

    public static void updateBall() {
        gameMenu.updateBall();
    }

    public static void addTransition(Transition transition) {
        GameMenu.transitions.add(transition);
    }

    public static void removeTransition(Transition transition) {
        GameMenu.transitions.remove(transition);
    }


    public static void loadMainCircle(MainCircle mainCircle,Game game){
        mainCircle.speed = game.mainCircleSpeed;
        mainCircle.setRadiusOfBalls(game.radiusOfBall);
        mainCircle.firstPane.getChildren().clear();
        mainCircle.firstPane.getChildren().add(mainCircle.circle);
        for (BallData ballData : game.defaultBalls) {
            Ball ball = new Ball(ballData.x, ballData.y, 10);
            ball.line = new Line(ballData.x, ballData.y, 500, 375);
            ball.pane = mainCircle.firstPane;
            mainCircle.firstPane.setRotate(ballData.rotate);
            mainCircle.firstPane.getChildren().addAll(ball.line, ball);
            mainCircle.getDefaultBalls().add(ball);
        }
        for (BallData ballData : game.connectedBalls) {
            Ball ball = new Ball(ballData.x, ballData.y, 10);
            Line line = new Line(ballData.x, ballData.y, 500, 375);
            ball.setNum(ballData.num);
            ball.line = line;

            Pane newPane = new Pane();
            newPane.getChildren().addAll(line, ball, ball.text);
            ball.pane = newPane;
            newPane.setRotate(ballData.rotate);
            mainCircle.getChildren().addAll(newPane);
            mainCircle.getNewBalls().add(ball);
        }
    }

}
