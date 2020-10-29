package com.yube;

import java.util.Arrays;

public class SimplexTable {

    private int[] xBasis;
    private double[] c;
    private double[][] a;
    private double[] beta;
    private double[] theta;
    private double[] delta;

    public int[] getxBasis() {
        return xBasis;
    }

    public void setxBasis(int[] xBasis) {
        this.xBasis = xBasis;
    }

    public double[] getC() {
        return c;
    }

    public void setC(double[] c) {
        this.c = c;
    }

    public double[][] getA() {
        return a;
    }

    public void setA(double[][] a) {
        this.a = a;
    }

    public double[] getBeta() {
        return beta;
    }

    public void setBeta(double[] beta) {
        this.beta = beta;
    }

    public double[] getTheta() {
        return theta;
    }

    public void setTheta(double[] theta) {
        this.theta = theta;
    }

    public double[] getDelta() {
        return delta;
    }

    public void setDelta(double[] delta) {
        this.delta = delta;
    }

    @Override
    public String toString() {
        return String.format("Матриця коефіцієнтів в системі обмежень: \n%s\n", Utils.matrixToString(a)) +
                String.format("x: %s\n", Arrays.toString(xBasis)) +
                String.format("c: %s\n", Utils.arrayToString(c)) +
                String.format("β: %s\n", Utils.arrayToString(beta)) +
                String.format("Θ: %s\n", Utils.arrayToString(theta)) +
                String.format("Δ: %s\n", Utils.arrayToString(delta));
    }
}
