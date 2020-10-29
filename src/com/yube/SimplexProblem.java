package com.yube;

public class SimplexProblem {

    private int variablesCount;
    private int equationsCount;
    private Equation[] equationSystem;
    private double[] function;
    private SimplexProblemType type;

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

    public Equation[] getEquationSystem() {
        return equationSystem;
    }

    public void setEquationSystem(Equation[] equationSystem) {
        this.equationSystem = equationSystem;
    }

    public double[] getFunction() {
        return function;
    }

    public void setFunction(double[] function) {
        this.function = function;
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
            equationsSystemStringBuilder.append(equationSystem[i].toString()).append("\n");
        }
        result.append(String.format("Система рівнянь: \n%s", equationsSystemStringBuilder.toString()));
        result.append(String.format("Тип задачі: %s\n", type));
        return result.toString();
    }
}
