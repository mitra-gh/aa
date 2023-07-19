package model.other;

public class BallData {
    public int num = -1;
    public double x;
    public double y;

    public double rotate;

    public BallData(int num, double x, double y) {
        this.num = num;
        this.x = x;
        this.y = y;
    }

    public BallData(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
