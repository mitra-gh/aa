package model.game;

import controller.MainController;
import enumoration.GameTypes;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import model.Game;
import view.animations.MainCircleTransition;

import java.util.ArrayList;
import java.util.Random;

public class MainCircle extends StackPane {
    private ArrayList<Ball> defaultBalls = new ArrayList<>();
    private ArrayList<Ball> newBalls = new ArrayList<>();

    Game game;
    public int radius;
    private int radiusOfBalls = 100;
    public Circle circle;

    public int speed;
    public Pane firstPane = new Pane();




    public MainCircle(int radius) {
        circle = new Circle(500, 375, radius);
        this.radius = radius;
        makeBalls();
        firstPane.getChildren().add(circle);
        this.getChildren().add(firstPane);
        game = MainController.game;
        speed = game.getSpeed();
    }


    public void makeCircleForMenu() {
        circle = new Circle(500, 375, radius);
        circle.setFill(Color.LIGHTYELLOW);
        circle.setStrokeWidth(10);
        circle.setStrokeMiterLimit(10);
        circle.setStrokeType(StrokeType.CENTERED);
        circle.setStroke(Color.valueOf("0x333333"));
    }


    public void makeBalls() {
        Game game = MainController.game;
        String type = game.getType();
        double[] degrees;
        degrees = switch (type) {
            case "solitude" -> GameTypes.SOLITUDE.getDegrees();
            case "dense" -> GameTypes.DENSE.getDegrees();
            default -> GameTypes.MESSY.getDegrees();
        };
        makeBalls(degrees);
    }

    public int getRadiusOfBalls() {
        return radiusOfBalls;
    }

    public void setRadiusOfBalls(int radiusOfBalls) {
        this.radiusOfBalls = radiusOfBalls;
    }

    public void makeBalls(double[] degrees) {
        for (double v : degrees) {
            int radiusOfBall = radius + radiusOfBalls;
            double x = Math.cos(v * Math.PI / 180) * radiusOfBall + 500;
            double y = Math.sin(v * Math.PI / 180) * radiusOfBall + 375;
            Ball ball = new Ball(x, y, 10);
            Line line = new Line(ball.getCenterX(), ball.getCenterY(), 500, 375);
            ball.line = line;
            ball.pane = firstPane;
            defaultBalls.add(ball);
            firstPane.getChildren().addAll(ball, line);
        }
    }

    public ArrayList<Ball> getDefaultBalls() {
        return defaultBalls;
    }

    public void setDefaultBalls(ArrayList<Ball> defaultBalls) {
        this.defaultBalls = defaultBalls;
    }

    public ArrayList<Ball> getNewBalls() {
        return newBalls;
    }

    public void setNewBalls(ArrayList<Ball> newBalls) {
        this.newBalls = newBalls;
    }
}
