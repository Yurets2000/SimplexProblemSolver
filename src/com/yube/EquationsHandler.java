package com.yube;

public class EquationsHandler {

    public static Equation copyEquation(Equation equation) {
        Equation copy = new Equation();
        copy.setBound(equation.getBound());
        copy.setSymbol(equation.getSymbol());
        copy.setCoefficients(equation.getCoefficients());
        return copy;
    }

    public static Equation multiplyEquation(Equation equation, double x) {
        double[] resultCoefficients = Utils.multiplyArray(equation.getCoefficients(), x);
        Symbol resultSymbol = x >= 0 ? equation.getSymbol() : Symbol.invertSymbol(equation.getSymbol());
        double resultBound = x * equation.getBound();
        Equation result = new Equation();
        result.setCoefficients(resultCoefficients);
        result.setSymbol(resultSymbol);
        result.setBound(resultBound);
        return result;
    }
}
