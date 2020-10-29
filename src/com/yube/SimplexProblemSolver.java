package com.yube;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimplexProblemSolver {

    private final static Double M = 10000.0;
    private final static int maxIterations = 100;

    public SimplexProblemSolution solve(SimplexProblem simplexProblem) {
        System.out.println(Utils.getEnvelope("Задача до розв'язання симплекс-методом", simplexProblem.toString()));
        CanonicSimplexProblem canonicSimplexProblem = getSimplexProblemCanonicForm(simplexProblem);
        System.out.println(Utils.getEnvelope("Канонічна задача до розв'язання симплекс-методом", canonicSimplexProblem.toString()));
        SimplexProblemSolution solution = solveCanonic(canonicSimplexProblem);
        System.out.println(Utils.getEnvelope("Розв'язок задачі лінійного програмування", solution.toString()));
        return solution;
    }

    private SimplexProblemSolution solveCanonic(CanonicSimplexProblem canonicSimplexProblem) {
        int n = canonicSimplexProblem.getInitialVariablesCount();
        int n2 = canonicSimplexProblem.getVariablesCount();
        SimplexProblemType type = canonicSimplexProblem.getType();
        SimplexTable simplexTable = constructSimplexTable(canonicSimplexProblem);
        int iterations = 0;
        while (true) {
            SimplexTableIterationStatistic statistic = performIteration(canonicSimplexProblem.getType(), simplexTable);
            iterations++;
            if (iterations > maxIterations) throw new RuntimeException("Неможливо отримати кінцевий результат, " +
                    "максимальна кількість ітерацій перевищена");
            System.out.println(Utils.getEnvelope(String.format("Симплекс-таблиця №%d", iterations), simplexTable.toString()));
            if (statistic.isOptimal()) {
                System.out.println("Оптимальний розв'язок знайдено");
                break;
            } else {
                System.out.println("Оптимальний розв'язок не знайдено, перехід до наступної ітерації...");
                System.out.println("Виводимо з базису: " + statistic.getIn());
                System.out.println("Вводимо в базис: " + statistic.getOut());
                moveToNextIteration(statistic.getIn(), statistic.getOut(), simplexTable);
            }
        }
        int[] xBasis = simplexTable.getxBasis();
        double[] beta = simplexTable.getBeta();
        double[] c = simplexTable.getC();
        int k = xBasis.length;
        double[] result = new double[n2];
        for (int i = 0; i < k; i++) {
            result[xBasis[i]] = beta[i];
        }
        double[] x = new double[n];
        System.arraycopy(result, 0, x, 0, n);
        double value = 0.0;
        for (int i = 0; i < n; i++) {
            value += x[i] * c[i];
        }
        SimplexProblemSolution solution = new SimplexProblemSolution();
        solution.setIterations(iterations);
        solution.setValue(value);
        solution.setX(x);
        solution.setType(type);
        return solution;
    }

    private void moveToNextIteration(int in, int out, SimplexTable simplexTable) {
        int[] xBasis = simplexTable.getxBasis();
        int k = xBasis.length;
        double[][] a = simplexTable.getA();
        double[] beta = simplexTable.getBeta();
        xBasis[out] = in;
        double pivot = a[out][in];
        a[out] = Utils.multiplyArray(a[out], 1 / pivot);
        beta[out] /= pivot;
        for (int i = 0; i < k; i++) {
            if (i != out) {
                double multiplier = -a[i][in];
                double[] toAdd = Utils.multiplyArray(a[out], multiplier);
                a[i] = Utils.addArrays(a[i], toAdd);
                beta[i] += beta[out] * multiplier;
            }
        }
    }

    private SimplexTableIterationStatistic performIteration(SimplexProblemType problemType, SimplexTable simplexTable) {
        boolean optimal = true;
        int k = simplexTable.getxBasis().length;
        int n = simplexTable.getDelta().length;
        int[] xBasis = simplexTable.getxBasis();
        double[] c = simplexTable.getC();
        double[][] a = simplexTable.getA();
        double[] delta = simplexTable.getDelta();
        double[] theta = simplexTable.getTheta();
        double[] beta = simplexTable.getBeta();

        for (int i = 0; i < n; i++) {
            delta[i] = 0.0;
            for (int j = 0; j < k; j++) {
                delta[i] += c[xBasis[j]] * a[j][i];
            }
            delta[i] -= c[i];
            if ((problemType.equals(SimplexProblemType.MIN) && delta[i] > 0.0) ||
                    (problemType.equals(SimplexProblemType.MAX) && delta[i] < 0.0)) {
                optimal = false;
            }
        }
        if (optimal) return new SimplexTableIterationStatistic(true);
        List<Integer> deltaIndexes = problemType.equals(SimplexProblemType.MAX) ?
                Utils.minMaxIndex(delta, null, 0.0, false, true, null) :
                Utils.minMaxIndex(delta, 0.0, null, false, false, null);
        int deltaIndex = deltaIndexes.get(0);
        for (int i = 0; i < k; i++) {
            theta[i] = a[i][deltaIndex] > 0 ? beta[i] / a[i][deltaIndex] : -1.0;
        }
        boolean valid = false;
        for (int i = 0; i < k; i++) {
            if (theta[i] > 0.0) {
                valid = true;
                break;
            }
        }
        if (!valid) throw new RuntimeException("Система не має жодного розв'язку або має безліч розв'язків");
        List<Integer> thetaIndexes = Utils.minMaxIndex(theta, 0.0, null, true, true, null);
        int thetaIndex;
        if (thetaIndexes.size() == 1) {
            thetaIndex = thetaIndexes.get(0);
        } else {
            int s = thetaIndexes.size();
            double[][] competitorRows = new double[s][n];
            for (int i = 0; i < s; i++) {
                competitorRows[i] = Utils.multiplyArray(a[thetaIndexes.get(i)], 1 / a[thetaIndexes.get(i)][deltaIndex]);
            }
            thetaIndex = thetaIndexes.get(findChampion(competitorRows));
        }
        SimplexTableIterationStatistic statistic = new SimplexTableIterationStatistic(false);
        statistic.setIn(deltaIndex);
        statistic.setOut(thetaIndex);
        return statistic;
    }

    private int findChampion(double[][] competitorRows) {
        int s = competitorRows.length;
        int n = competitorRows[0].length;
        for (int i = 0; i < n; i++) {
            double[] column = new double[s];
            System.arraycopy(competitorRows[i], 0, column, 0, s);
            List<Integer> mins = Utils.minMaxIndex(column, 0.0, null, false, true, null);
            if (mins.size() == 1) return mins.get(0);
        }
        return 0;
    }

    private SimplexTable constructSimplexTable(CanonicSimplexProblem canonicSimplexProblem) {
        int[] basis = canonicSimplexProblem.getBasis();
        int k = basis.length;
        int n = canonicSimplexProblem.getVariablesCount();
        double[] bounds = canonicSimplexProblem.getBounds();
        double[] theta = new double[k];
        double[] beta = new double[k];
        double[] delta = new double[n];
        double[][] a = new double[k][n];
        double[] c = new double[n];
        System.arraycopy(bounds, 0, beta, 0, k);
        System.arraycopy(canonicSimplexProblem.getFunction(), 0, c, 0, n);
        for (int i = 0; i < k; i++) {
            System.arraycopy(canonicSimplexProblem.getEquationsCoefficients()[i], 0, a[i], 0, n);
        }
        int[] xBasis = getSortedBasis(basis, a);
        SimplexTable simplexTable = new SimplexTable();
        simplexTable.setC(c);
        simplexTable.setA(a);
        simplexTable.setxBasis(xBasis);
        simplexTable.setBeta(beta);
        simplexTable.setDelta(delta);
        simplexTable.setTheta(theta);
        return simplexTable;
    }

    private int[] getSortedBasis(int[] basis, double[][] coefficients) {
        int k = coefficients.length;
        int[] sortedBasis = new int[k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if (coefficients[j][basis[i]] == 1.0) {
                    sortedBasis[j] = basis[i];
                    break;
                }
            }
        }
        return sortedBasis;
    }

    private SimplexProblem getDualSimplexProblem(SimplexProblem simplexProblem) {
        int m = simplexProblem.getEquationsCount();
        int n = simplexProblem.getVariablesCount();
        SimplexProblemType type = simplexProblem.getType();
        Equation[] equations = simplexProblem.getEquationSystem();
        double[] function = simplexProblem.getFunction();

        Equation[] normalizedEquations = new Equation[m];
        for (int i = 0; i < m; i++) {
            if (equations[i].getSymbol().equals(Symbol.LESS)) {
                normalizedEquations[i] = type.equals(SimplexProblemType.MIN) ?
                        EquationsHandler.multiplyEquation(equations[i], -1) :
                        EquationsHandler.copyEquation(equations[i]);
            } else if (equations[i].getSymbol().equals(Symbol.GREATER)) {
                normalizedEquations[i] = type.equals(SimplexProblemType.MIN) ?
                        EquationsHandler.copyEquation(equations[i]) :
                        EquationsHandler.multiplyEquation(equations[i], -1);
            }
        }
        normalize(n, normalizedEquations);

        Equation[] dualEquations = new Equation[n];
        SimplexProblemType dualType = type.equals(SimplexProblemType.MIN) ?
                SimplexProblemType.MAX : SimplexProblemType.MIN;
        double[] dualFunction = new double[m];
        for (int i = 0; i < n; i++) {
            Equation dualEquation = new Equation();
            double[] column = new double[m];
            for (int j = 0; j < m; j++) {
                column[j] = equations[j].getCoefficients()[i];
            }
            dualEquation.setCoefficients(column);
            dualEquation.setBound(function[i]);
            dualEquation.setSymbol(type.equals(SimplexProblemType.MIN) ? Symbol.LESS : Symbol.GREATER);
            dualEquations[i] = dualEquation;
        }
        for (int i = 0; i < m; i++) {
            dualFunction[i] = equations[i].getBound();
        }

        SimplexProblem dualSimplexProblem = new SimplexProblem();
        dualSimplexProblem.setType(dualType);
        dualSimplexProblem.setFunction(dualFunction);
        dualSimplexProblem.setEquationSystem(dualEquations);
        dualSimplexProblem.setVariablesCount(m);
        dualSimplexProblem.setEquationsCount(n);
        return dualSimplexProblem;
    }

    private CanonicSimplexProblem getSimplexProblemCanonicForm(SimplexProblem simplexProblem) {
        int m = simplexProblem.getEquationsCount();
        int n = simplexProblem.getVariablesCount();
        SimplexProblemType type = simplexProblem.getType();
        Equation[] canonicEquations = new Equation[m];

        for (int i = 0; i < m; i++) {
            canonicEquations[i] = getEquationPreCanonicForm(simplexProblem.getEquationSystem()[i]);
        }
        int additional = normalize(n, canonicEquations);

        Map<Integer, Integer> basisVariablesMap = new HashMap<>();
        List<Double> canonicFunctionList = Utils.doubleArrayToList(simplexProblem.getFunction());
        for (int i = 0; i < additional; i++) {
            canonicFunctionList.add(0.0);
        }
        int n2 = n + additional;
        int j = 0;
        for (int i = 0; i < n2; i++) {
            ColumnStatistic columnStatistic = getColumnStatistic(i, canonicEquations);
            if (columnStatistic.isPossibleBasis()) {
                int index = columnStatistic.getIndex();
                if (!basisVariablesMap.containsValue(index) && basisVariablesMap.size() < m) {
                    if (columnStatistic.getPositive()) {
                        if (!columnStatistic.getNormalized()) {
                            double value = canonicEquations[index].getCoefficients()[i];
                            EquationsHandler.multiplyEquation(canonicEquations[index], 1 / value);
                        }
                        basisVariablesMap.put(i, index);
                    } else {
                        List<Double> coefficientsList = Utils.doubleArrayToList(canonicEquations[index].getCoefficients());
                        coefficientsList.add(1.0);
                        canonicEquations[index].setCoefficients(Utils.doubleListToArray(coefficientsList));
                        basisVariablesMap.put(n2 + j++, index);
                        canonicFunctionList.add(type == SimplexProblemType.MIN ? M : -M);
                    }
                }
            }
        }
        int[] basis = Utils.convertIntegerArrBackwards(basisVariablesMap.keySet().toArray(new Integer[0]));
        if (basis.length < m) throw new RuntimeException("Неможливо знайти ДБР для системи рівнянь");
        double[] canonicFunction = Utils.doubleListToArray(canonicFunctionList);
        int additional2 = normalize(n2, canonicEquations);
        int n3 = n2 + additional2;
        double[] canonicBounds = retrieveEquationsBounds(canonicEquations);
        double[][] canonicCoefficients = retrieveEquationsCoefficients(canonicEquations);
        CanonicSimplexProblem canonicSimplexProblem = new CanonicSimplexProblem();
        canonicSimplexProblem.setBasis(basis);
        canonicSimplexProblem.setBounds(canonicBounds);
        canonicSimplexProblem.setEquationsCoefficients(canonicCoefficients);
        canonicSimplexProblem.setInitialVariablesCount(n);
        canonicSimplexProblem.setVariablesCount(n3);
        canonicSimplexProblem.setEquationsCount(m);
        canonicSimplexProblem.setFunction(canonicFunction);
        canonicSimplexProblem.setType(type);
        return canonicSimplexProblem;
    }

    private double[] retrieveEquationsBounds(Equation[] equations) {
        int m = equations.length;
        double[] bounds = new double[m];
        for (int i = 0; i < m; i++) {
            bounds[i] = equations[i].getBound();
        }
        return bounds;
    }

    private double[][] retrieveEquationsCoefficients(Equation[] equations) {
        int m = equations.length;
        int n = equations[0].getCoefficients().length;
        double[][] coefficients = new double[m][n];
        for (int i = 0; i < m; i++) {
            System.arraycopy(equations[i].getCoefficients(), 0, coefficients[i], 0, n);
        }
        return coefficients;
    }

    private ColumnStatistic getColumnStatistic(int i, Equation[] equations) {
        int nonZeroCount = 0;
        for (int j = 0; j < equations.length; j++) {
            if (equations[j].getCoefficients()[i] != 0) {
                nonZeroCount++;
            }
        }
        if (nonZeroCount > 1) {
            return new ColumnStatistic(false);
        } else {
            for (int j = 0; j < equations.length; j++) {
                if (equations[j].getCoefficients()[i] != 0) {
                    ColumnStatistic columnStatistic = new ColumnStatistic(true);
                    columnStatistic.setIndex(j);
                    columnStatistic.setNormalized(Math.abs(equations[j].getCoefficients()[i]) == 1);
                    columnStatistic.setPositive(equations[j].getCoefficients()[i] > 0);
                    return columnStatistic;
                }
            }
        }
        throw new IllegalArgumentException("Передана невалідна система рівнянь");
    }

    private int normalize(int n, Equation[] equations) {
        int additional = 0; //compute number of additional variables
        for (int i = 0; i < equations.length; i++) {
            additional += equations[i].getCoefficients().length - n;
        }
        int shift = 0;
        for (int i = 0; i < equations.length; i++) {
            double[] prefix = new double[additional];
            int k = equations[i].getCoefficients().length - n;
            if (k > 0) {
                for (int j = 0; j < k; j++) {
                    prefix[shift++] = equations[i].getCoefficients()[n + j];
                }
            }
            double[] suffix = Arrays.stream(equations[i].getCoefficients()).limit(n).toArray();
            double[] normalizedCoefficients = Utils.convertDoubleArrBackwards(
                    Utils.concatAll(Utils.convertDoubleArr(suffix),
                            Utils.convertDoubleArr(prefix)));
            equations[i].setCoefficients(normalizedCoefficients);
        }
        return additional;
    }

    private Equation getEquationPreCanonicForm(Equation equation) {
        Equation preCanonic;
        if (equation.getBound() < 0) {
            preCanonic = EquationsHandler.multiplyEquation(equation, -1);
        } else {
            preCanonic = EquationsHandler.copyEquation(equation);
        }
        if (preCanonic.getSymbol().equals(Symbol.EQUAL)) return preCanonic;
        List<Double> coefficients = Utils.doubleArrayToList(preCanonic.getCoefficients());
        if (preCanonic.getSymbol().equals(Symbol.LESS)) {
            coefficients.add(1.0);
        } else if (preCanonic.getSymbol().equals(Symbol.GREATER)) {
            coefficients.add(-1.0);
        }
        preCanonic.setCoefficients(Utils.doubleListToArray(coefficients));
        preCanonic.setSymbol(Symbol.EQUAL);
        return preCanonic;
    }
}
