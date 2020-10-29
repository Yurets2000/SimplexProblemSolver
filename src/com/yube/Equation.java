package com.yube;

public class Equation {

    private double[] coefficients;
    private Symbol symbol;
    private double bound;

    public double[] getCoefficients() {
        return coefficients;
    }

    public void setCoefficients(double[] coefficients) {
        this.coefficients = coefficients;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public double getBound() {
        return bound;
    }

    public void setBound(double bound) {
        this.bound = bound;
    }

    @Override
    public String toString() {
        return Utils.linearFunctionToString(coefficients) +
                " " + symbol.getValue() + " " + bound;
    }
}
