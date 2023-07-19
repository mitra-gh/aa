package enumoration;

public enum GameTypes {
    SOLITUDE("solitude", new double[]{0, 72, 144, 216, 288}),
    DENSE("dense", new double[]{0, 36, 72, 108, 144, 180, 216, 252, 288,324}),
    MESSY("messy", new double[]{0, 10, 30, 60, 100, 150, 210, 280});
    private final String value;
    private final double[] degrees;

    GameTypes(String value, double[] degrees) {
        this.value = value;
        this.degrees = degrees;
    }

    public String getValue() {
        return value;
    }

    public double[] getDegrees() {
        return degrees;
    }
}
