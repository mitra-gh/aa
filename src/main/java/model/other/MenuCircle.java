package model.other;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import model.game.Ball;
import view.animations.MenuCircleTransition;

import java.util.ArrayList;
import java.util.Random;

public class MenuCircle extends Pane {
    ArrayList<Ball> defaultBalls = new ArrayList<>();
    int radius;
    public double degree = 0;
    public Circle circle;

    public MenuCircle(int defaultBallCount,int radius) {
        circle = new Circle(500, 375, radius);
        this.radius = radius;
        makeCircleForMenu();
        makeDefaultBallsForMenu(defaultBallCount);
        this.getChildren().add(circle);
    }


    public void makeCircleForMenu() {
        circle = new Circle(500, 375, radius);
        circle.setFill(Color.LIGHTYELLOW);
        circle.setStrokeWidth(10);
        circle.setStrokeMiterLimit(10);
        circle.setStrokeType(StrokeType.CENTERED);
        circle.setStroke(Color.valueOf("0x333333"));
    }

    public MenuCircleTransition getTransition() {
        return new MenuCircleTransition(10, this);
    }

    public void makeDefaultBallsForMenu(int count) {
        for (int i = 0; i < count; i++) {
            Ball ball = makeBallForMenu();
            while (defaultBalls.contains(ball)) {
                ball = makeBallForMenu();
            }
            defaultBalls.add(ball);
            Line line = new Line(ball.getCenterX(), ball.getCenterY(), 500, 375);
            this.getChildren().addAll(ball, line);
        }
    }


    public Ball makeBallForMenu() {
        Random random = new Random();
        int radiusOfBall = radius + 70 + random.nextInt(50);
        int degree = random.nextInt(72) * 5;
        double x = Math.cos(degree * Math.PI / 180) * radiusOfBall + 500;
        double y = Math.sin(degree * Math.PI / 180) * radiusOfBall + 375;
        return new Ball(x, y, 10);
    }



}
