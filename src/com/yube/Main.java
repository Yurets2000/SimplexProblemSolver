/*
 * Copyright (c) 2020. Yurii Bezliudnyi.
 * Copying without author notice is prohibited.
 *
 */

package com.yube;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private final Scanner scanner = new Scanner(System.in);
    private final String symbolPatternString = "(<=|=|>=)";
    private final String doubleNumberPatternString = "-?\\d+(\\.\\d+)?";
    private final String vectorPatternString = "\\[((" + doubleNumberPatternString + ",\\s+)+" + doubleNumberPatternString + ")?\\]";
    private final String vectorEquationPatternString = vectorPatternString + "\\s*" + symbolPatternString + "\\s*" + doubleNumberPatternString;

    public static void main(String[] args) {
        Main main = new Main();
        main.process();
    }

    public void process() {
        SimplexProblem simplexProblem = readSimplexProblem();
        SimplexProblemSolver simplexProblemSolver = new SimplexProblemSolver();
        simplexProblemSolver.solve(simplexProblem);
    }

    private SimplexProblem readSimplexProblem() {
        while (true) {
            try {
                String typeString = read("Введіть тип задачі (min/max):", "(min|max)");
                SimplexProblemType type = parseSimplexProblemType(typeString);
                int m = Integer.parseInt(read("Введіть кількість рівнянь в системі:", "\\d{1,2}"));
                int n = Integer.parseInt(read("Введіть кількість змінних в рівнянні:", "\\d{1,2}"));
                if (n > m)
                    throw new IllegalArgumentException("Кількість змінних не повина бути більшою за кількість рівнянь");
                Equation[] equationSystem = readEquationSystem(m, n);
                String functionString = read("Введіть функцію, яку необхідно оптимізувати:", vectorPatternString);
                double[] function = parseVector(functionString, n);
                SimplexProblem simplexProblem = new SimplexProblem();
                simplexProblem.setEquationsCount(m);
                simplexProblem.setVariablesCount(n);
                simplexProblem.setEquationSystem(equationSystem);
                simplexProblem.setFunction(function);
                simplexProblem.setType(type);
                return simplexProblem;
            } catch (Exception e) {
                System.out.printf("Неправильно введена задача, причина: '%s'. Спробуйте задати задачу знову.\n", e.getMessage());
            }
        }
    }

    private Equation[] readEquationSystem(int m, int n) {
        System.out.printf("Введіть систему з %d рівнянь:\n", m);
        Equation[] equationsSystem = new Equation[m];
        for (int i = 0; i < m; i++) {
            equationsSystem[i] = readEquation(i, n);
        }
        return equationsSystem;
    }

    private Equation readEquation(int i, int n) {
        while (true) {
            try {
                String equationString = read(String.format("Введіть %d-е рівняння: ", i), vectorEquationPatternString);
                Matcher vectorMatcher = Pattern.compile(vectorPatternString).matcher(equationString);
                String vectorString;
                if (vectorMatcher.find()) {
                    vectorString = vectorMatcher.group();
                } else {
                    throw new IllegalArgumentException("Неможливо знайти вектор коефіцієнтів у рівнянні");
                }
                double[] coefficients = parseVector(vectorString, n);
                Matcher symbolMatcher = Pattern.compile(symbolPatternString).matcher(equationString);
                String symbolString;
                if (symbolMatcher.find()) {
                    symbolString = symbolMatcher.group();
                } else {
                    throw new IllegalArgumentException("Неможливо знайти знак у рівнянні");
                }
                Symbol symbol = parseSymbol(symbolString);
                Matcher numberMatcher = Pattern.compile(doubleNumberPatternString).matcher(equationString);
                String boundString = null;
                while (numberMatcher.find()) {
                    boundString = numberMatcher.group();
                }
                if (boundString == null) {
                    throw new IllegalArgumentException("Неможливо знайти обмеження у рівнянні");
                }
                double bound = Double.parseDouble(boundString);
                Equation equation = new Equation();
                equation.setCoefficients(coefficients);
                equation.setSymbol(symbol);
                equation.setBound(bound);
                return equation;
            } catch (Exception e) {
                System.out.printf("Неправильно введене рівняння, причина: '%s'. Спробуйте задати рівняння знову.\n", e.getMessage());
            }
        }
    }

    private SimplexProblemType parseSimplexProblemType(String s) {
        switch (s) {
            case "min":
                return SimplexProblemType.MIN;
            case "max":
                return SimplexProblemType.MAX;
        }
        throw new IllegalArgumentException(String.format("Неможливо виділити тип проблеми з рядка '%s'", s));
    }

    private Symbol parseSymbol(String s) {
        switch (s) {
            case "<=":
                return Symbol.LESS;
            case ">=":
                return Symbol.GREATER;
            case "=":
                return Symbol.EQUAL;
        }
        throw new IllegalArgumentException(String.format("Неможливо виділити тип знак з рядка '%s'", s));
    }

    private double[] parseVector(String s, int size) {
        Matcher numberMatcher = Pattern.compile(doubleNumberPatternString).matcher(s);
        double[] vector = new double[size];
        int i = 0;
        while (numberMatcher.find()) {
            if (i >= size)
                throw new IllegalArgumentException(String.format("Вектор має бути довжиною рівно в %d елементів", size));
            vector[i++] = Double.parseDouble(numberMatcher.group());
        }
        return vector;
    }

    private String read(String question, String pattern) {
        while (true) {
            System.out.println(question);
            String line = scanner.nextLine().trim();
            if (line.matches(pattern)) return line;
            System.out.println("Неправильно введене значення, спробуйте знову.");
        }
    }
}
