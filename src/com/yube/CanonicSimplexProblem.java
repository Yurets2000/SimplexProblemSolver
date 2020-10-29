package com.yube;

import java.util.Arrays;

public class CanonicSimplexProblem {

    private int initialVariablesCount;
    private int variablesCount;
    private int equationsCount;
    private double[][] equationsCoefficients;
    private double[] bounds;
    private double[] function;
    private int[] basis;
    private SimplexProblemType type;

    public int getInitialVariablesCount() {
        return initialVariablesCount;
    }

    public void setInitialVariablesCount(int initialVariablesCount) {
        this.initialVariablesCount = initialVariablesCount;
    }

    public int getVariablesCount() {
        return variablesCount;
    }

    public void setVariablesCount(int variablesCount) {
        this.variablesCount = variablesCount;
    }

    public int getEquationsCount() {
        return equationsCount;
    }

    public void setEquationsCount(int equationsCount) {
        this.equationsCount = equationsCount;
    }

    public double[][] getEquationsCoefficients() {
        return equationsCoefficients;
    }

    public void setEquationsCoefficients(double[][] equationsCoefficients) {
        this.equationsCoefficients = equationsCoefficients;
    }

    public double[] getBounds() {
        return bounds;
    }

    public void setBounds(double[] bounds) {
        this.bounds = bounds;
    }

    public double[] getFunction() {
        return function;
    }

    public void setFunction(double[] function) {
        this.function = function;
    }

    public int[] getBasis() {
        return basis;
    }

    public void setBasis(int[] basis) {
        this.basis = basis;
    }

    public SimplexProblemType getType() {
        return type;
    }

    public void setType(SimplexProblemType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("Кількість змінних: %d\n", variablesCount));
        result.append(String.format("Кількість обмежень: %d\n", equationsCount));
        StringBuilder equationsSystemStringBuilder = new StringBuilder();
        for (int i = 0; i < equationsCount; i++) {
            equationsSystemStringBuilder.append(Utils.linearFunctionToString(equationsCoefficients[i]))
                    .append(" = ").append(bounds[i]).append("\n");
        }
        result.append(String.format("Система рівнянь: \n%s", equationsSystemStringBuilder.toString()));
        result.append(String.format("Базис: %s\n", Arrays.toString(basis)));
        result.append(String.format("Тип задачі: %s\n", type));
        return result.toString();
    }
}
