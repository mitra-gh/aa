package view.animations;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.util.Duration;
import model.other.MenuCircle;

public class MenuCircleTransition extends Transition {
    int speed;
    MenuCircle circle;

    public MenuCircleTransition(int speed, MenuCircle circle) {
        this.speed = speed;
        this.circle = circle;
        this.setCycleCount(-1);
        this.setCycleDuration(Duration.millis(10000));
        this.setInterpolator(Interpolator.LINEAR);
    }

    @Override
    protected void interpolate(double v) {
        circle.degree += 0.02;
        circle.setRotate(circle.degree);
    }
}
