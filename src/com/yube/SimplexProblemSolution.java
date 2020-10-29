package com.yube;

public class SimplexProblemSolution {

    private double[] x;
    private double value;
    private int iterations;
    private SimplexProblemType type;

    public double[] getX() {
        return x;
    }

    public void setX(double[] x) {
        this.x = x;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public SimplexProblemType getType() {
        return type;
    }

    public void setType(SimplexProblemType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        String result = String.format("Кількість зроблених ітерацій: %d\n", iterations);
        result += type == SimplexProblemType.MIN ? "F_min = " : "F_max = ";
        result += "F" + Utils.arrayToString(x) + " = " + String.format("%.3f", value);
        return result;
    }
}
