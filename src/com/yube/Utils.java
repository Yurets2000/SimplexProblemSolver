package com.yube;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static List<Integer> minMaxIndex(double[] arr, Double bottomBound, Double upperBound, boolean inclusive, boolean min, List<Integer> exclusion) {
        double minMax = min ? Double.MAX_VALUE : Double.MIN_VALUE;
        for (int j = 0; j < arr.length; j++) {
            if (
                    (!inclusive && (bottomBound == null || (arr[j] > bottomBound) && (upperBound == null || (arr[j] < upperBound))))
                            || (inclusive && (bottomBound == null || (arr[j] >= bottomBound) && (upperBound == null || (arr[j] <= upperBound))))) {
                minMax = arr[j];
                break;
            }
        }
        for (int j = 0; j < arr.length; j++) {
            if ((!inclusive && (bottomBound == null || (arr[j] > bottomBound) && (upperBound == null || (arr[j] < upperBound))))
                    || (inclusive && (bottomBound == null || (arr[j] >= bottomBound) && (upperBound == null || (arr[j] <= upperBound))))) {
                if ((min && arr[j] < minMax) || (!min && arr[j] > minMax)) {
                    minMax = arr[j];
                }
            }
        }
        List<Integer> indexes = new ArrayList<>();
        for (int j = 0; j < arr.length; j++) {
            if (arr[j] == minMax) {
                indexes.add(j);
            }
        }
        return indexes;
    }

    public static double[] addArrays(double[] arr1, double[] arr2) {
        if (arr1.length != arr2.length) {
            throw new IllegalArgumentException("Масиви мають різну довжину");
        }
        int n = arr1.length;
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            result[i] = arr1[i] + arr2[i];
        }
        return result;
    }

    public static double[] multiplyArray(double[] arr, double x) {
        int n = arr.length;
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            result[i] = x * arr[i];
        }
        return result;
    }

    public static List<Integer> integerArrayToList(int[] arr) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            list.add(arr[i]);
        }
        return list;
    }

    public static List<Double> doubleArrayToList(double[] arr) {
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            list.add(arr[i]);
        }
        return list;
    }

    public static int[] integerListToArray(List<Integer> list) {
        return convertIntegerArrBackwards(list.toArray(new Integer[0]));
    }

    public static double[] doubleListToArray(List<Double> list) {
        return convertDoubleArrBackwards(list.toArray(new Double[0]));
    }

    public static Integer[] convertIntegerArr(int[] arr) {
        Integer[] copy = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        return copy;
    }

    public static int[] convertIntegerArrBackwards(Integer[] arr) {
        int[] copy = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        return copy;
    }

    public static Double[] convertDoubleArr(double[] arr) {
        Double[] copy = new Double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        return copy;
    }

    public static double[] convertDoubleArrBackwards(Double[] arr) {
        double[] copy = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        return copy;
    }

    public static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public static boolean contains(int[] arr, int value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) return true;
        }
        return false;
    }

    public static String linearFunctionToString(double[] function) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < function.length; i++) {
            String sign = "";
            if (i != 0) {
                sign = function[i] >= 0.0 ? " + " : " - ";
            }
            result.append(sign).append(String.format("%.3f", Math.abs(function[i]))).append("*").append("x_").append(i);
        }
        return result.toString();
    }

    public static String arrayToString(double[] arr) {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < arr.length - 1; i++) {
            result.append(String.format("%.3f, ", arr[i]));
        }
        if (arr.length > 0) {
            result.append(String.format("%.3f", arr[arr.length - 1]));
        }
        result.append("]");
        return result.toString();
    }

    public static String matrixToString(double[][] matrix) {
        StringBuilder result = new StringBuilder();
        for (double[] row : matrix) {
            for (double element : row) {
                result.append(String.format("%9.3f", element));
            }
            result.append("\n");
        }
        return result.toString();
    }

    public static String center(String text, int len) {
        String out = String.format("%" + len + "s%s%" + len + "s", "", text, "");
        float mid = (out.length() / 2);
        float start = mid - (len / 2);
        float end = start + len;
        return out.substring((int) start, (int) end);
    }

    public static String getEnvelope(String header, String content) {
        String envelope = getHeader(header);
        envelope += content + "\n";
        envelope += Utils.getSplitter() + "\n";
        return envelope;
    }

    public static String getHeader(String content) {
        String splitter = getSplitter();
        return splitter + "\n" +
                Utils.center(content, 100) + "\n" +
                splitter + "\n";
    }

    public static String getSplitter() {
        return "----------------------------------------------------------------------------------------------------";
    }
}
