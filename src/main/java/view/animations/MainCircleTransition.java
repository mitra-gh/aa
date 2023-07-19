package view.animations;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import model.game.MainCircle;

public class MainCircleTransition extends Transition {

    MainCircle circle;
    Pane pane;

    public MainCircleTransition(MainCircle circle, Pane pane) {
        this.circle = circle;
        this.pane = pane;
        this.setCycleCount(-1);
        this.setCycleDuration(Duration.millis(10000));
        this.setInterpolator(Interpolator.LINEAR);
    }

    @Override
    protected void interpolate(double v) {
        double speed = (double) circle.speed / 20;
        pane.setRotate(pane.getRotate() + speed);
    }
}
