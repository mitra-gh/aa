package model.game;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class Ball extends Circle {
    private final double degree;
    public int num = -1;
    public Text text = new Text();

    public Pane pane;
    public Line line;

    public Ball(double v, double v1, double v2) {
        super(v, v1, v2);
        this.degree = Math.atan((v1 - 375)/(v - 500)) * (180 / Math.PI);
    }


    public void setNum(int num) {
        this.num = num;

        text.setText(num + "");
        if (num < 10) {
            text.setText("0" + num);
        }
        text.setX(this.getCenterX() - 7);
        text.setY(this.getCenterY() + 5);
        text.setFill(Color.WHITE);
    }

    public int getNum() {
        return num;
    }

    public Text getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Ball ball)) {
            return false;
        }
        return Math.abs(ball.degree - this.degree) < 8;
    }
}
