package view.animations;

import controller.GameController;
import javafx.animation.Transition;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import model.game.Ball;
import model.game.MainCircle;

import java.util.ArrayList;


public class ShootingAnimation extends Transition {
    MainCircle circle;
    ArrayList<Ball> balls;
    Pane ballPane;
    Ball ball;

    public double degree;

    public ShootingAnimation(MainCircle circle, Ball ball, Pane ballPane, ArrayList<Ball> balls) {
        this.circle = circle;
        this.ball = ball;
        this.ballPane = ballPane;
        this.balls = balls;
        this.setCycleCount(-1);
        this.setCycleDuration(Duration.millis(800));
    }

    @Override
    protected void interpolate(double v) {
        if (degree == 0) {
            changeWhenDegreeZero();
        } else {
            changeWithDegree();
        }
    }

    public void changeWhenDegreeZero() {
        double y = ball.getCenterY() - 5;
        if (Math.abs(y - 375) <= circle.getRadiusOfBalls() + 50) {
            this.stop();
            GameController.removeTransition(this);
            int opacity = 1;

            for (Ball b : circle.getDefaultBalls()) {
                opacity = (int) b.getOpacity();
                if (isCollide(b, ball)) {
                    GameController.showLose();
                    return;
                }
            }
            for (Ball b : circle.getNewBalls()) {
                if (isCollide(b, ball)) {
                    GameController.showLose();
                    return;
                }
            }

            ball.setRotate(circle.getRotate());
            Line line = new Line(ball.getCenterX(), ball.getCenterY(), circle.circle.getCenterX(), circle.circle.getCenterY());
            ballPane.getChildren().remove(ball);
            balls.remove(ball);
            ball.line = line;
            ball.setOpacity(opacity);
            ball.line.setOpacity(opacity);
            ball.text.setOpacity(opacity);
            Pane newPane = new Pane();
            newPane.getChildren().addAll(line, ball, ball.text);
            ball.pane = newPane;
            MainCircleTransition transition = new MainCircleTransition(circle, newPane);
            transition.play();
            GameController.addTransition(transition);
            circle.getChildren().add(newPane);
            circle.getNewBalls().add(ball);
            GameController.gameMenu.updateBall();






            return;

        }
        ball.setCenterY(y);
        ball.text.setY(y + 5);
    }

    public void changeWithDegree() {
        double rad = degree * Math.PI / 180;
        double dy = Math.cos(rad) * 5;
        double dx = Math.sin(rad) * 5;
        double y = ball.getCenterY() - dy;
        double x = ball.getCenterX() + dx;
        double dis = Math.sqrt(Math.pow(y - 375,2) + Math.pow( x - 500,2));



        if (dis <= circle.getRadiusOfBalls() + circle.radius) {
            int radiusOfBall = circle.radius + circle.getRadiusOfBalls();
            if (dis < circle.getRadiusOfBalls() + circle.radius){
                double degree = Math.atan((ball.getCenterY() - 375) / (ball.getCenterX() - 500));
                x = Math.cos(degree) * radiusOfBall;
                y = Math.sin(degree) * radiusOfBall;
                if (Math.signum(Math.cos(degree)) != Math.signum(ball.getCenterX() - 500)) {
                    x *= (-1);
                }
                if (Math.signum(ball.getCenterY() - 375) != Math.signum(Math.sin(degree))) {
                    y *= (-1);
                }
                x += 500;
                y += 375;
                ball.setCenterY(y);
                ball.text.setY(y + 5);
                ball.setCenterX(x);
                ball.text.setX(x - 5);
            }
            this.stop();
            GameController.removeTransition(this);
            int opacity = 1;

            for (Ball b : circle.getDefaultBalls()) {
                opacity = (int) b.getOpacity();
                if (isCollide(b, ball)) {
                    GameController.showLose();
                    return;
                }
            }
            for (Ball b : circle.getNewBalls()) {
                if (isCollide(b, ball)) {
                    GameController.showLose();
                    return;
                }
            }


            circle.getNewBalls().add(ball);
            ball.setRotate(circle.getRotate());
            Line line = new Line(ball.getCenterX(), ball.getCenterY(), circle.circle.getCenterX(), circle.circle.getCenterY());
            ballPane.getChildren().remove(ball);
            balls.remove(ball);
            ball.line = line;
            ball.setOpacity(opacity);
            ball.line.setOpacity(opacity);
            ball.text.setOpacity(opacity);
            Pane newPane = new Pane();
            newPane.getChildren().addAll(line, ball, ball.text);
            ball.pane = newPane;
            MainCircleTransition transition = new MainCircleTransition(circle, newPane);
            transition.play();
            GameController.addTransition(transition);
            circle.getChildren().add(newPane);
            GameController.gameMenu.updateBall();
            return;

        }
        ball.setCenterY(y);
        ball.text.setY(y + 5);
        ball.setCenterX(x);
        ball.text.setX(x - 5);
        if (x <=10 || y <= 10 || y >= 740 || x >= 900 ){
            this.stop();
            GameController.removeTransition(this);
            GameController.showLose();
        }
    }

    public static boolean isCollide(Node x, Node y) {
        Bounds RectA = x.localToScene(x.getBoundsInLocal());
        Bounds RectB = y.localToScene(y.getBoundsInLocal());

        return RectB.intersects(RectA);
    }
}
