package ru.ssau.tk.itenion.matrix;

import ru.ssau.tk.itenion.exceptions.DifferentLengthOfArraysException;

import java.util.ArrayList;

public class Matrices {

    private static boolean isIncompatibleDimensions(Matrix firstMatrix, Matrix secondMatrix, boolean isMultiply) {
        return (!(firstMatrix.getCountRow() == secondMatrix.getCountColumn()) && isMultiply) || (!isMultiply && (firstMatrix.getCountRow() != secondMatrix.getCountRow()) || (firstMatrix.getCountColumn() != secondMatrix.getCountColumn()));
    }

    private static boolean isIncompatibleDimensions(Matrix matrix) {
        return matrix.getCountRow() != matrix.getCountColumn();
    }

    private static Matrix addAndSubtractAndMultiplyOnTheNumber(Matrix firstMatrix, Matrix secondMatrix, double multiplyOnThe, boolean isAdd) {
        if (secondMatrix == null && multiplyOnThe == 1 || firstMatrix == null) {
            return null;
        }
        if (secondMatrix != null && isIncompatibleDimensions(firstMatrix, secondMatrix, false)) {
            return null;
        }
        Matrix resultMatrix = new Matrix(firstMatrix.getCountRow(), firstMatrix.getCountColumn());
        for (int i = 0; i < firstMatrix.getCountRow(); i++) {
            for (int j = 0; j < firstMatrix.getCountColumn(); j++) {
                resultMatrix.set(i + 1, j + 1, firstMatrix.get(i + 1, j + 1) * multiplyOnThe + (multiplyOnThe != 1 ? 0 : secondMatrix.get(i + 1, j + 1) * (isAdd ? 1 : -1)));
            }
        }
        return resultMatrix;
    }

    public static Matrix add(Matrix firstMatrix, Matrix secondMatrix) {
        return addAndSubtractAndMultiplyOnTheNumber(firstMatrix, secondMatrix, 1, true);
    }

    public static Matrix subtract(Matrix firstMatrix, Matrix secondMatrix) {
        return addAndSubtractAndMultiplyOnTheNumber(firstMatrix, secondMatrix, 1, false);
    }

    public static Matrix multiply(Matrix firstMatrix, Matrix secondMatrix) {
        if (isIncompatibleDimensions(firstMatrix, secondMatrix, true)) {
            return null;
        }
        Matrix resultMatrix = new Matrix(firstMatrix.getCountRow(), secondMatrix.getCountColumn());
        for (int i = 0; i < firstMatrix.getCountRow(); i++) {
            for (int j = 0; j < secondMatrix.getCountColumn(); j++) {
                for (int k = 0; k < firstMatrix.getCountRow(); k++) {
                    resultMatrix.set(i + 1, j + 1, resultMatrix.get(i + 1, j + 1) + firstMatrix.get(i + 1, k + 1) * secondMatrix.get(k + 1, j + 1));
                }
            }
        }
        return resultMatrix;
    }

    public static Matrix multiplyOnTheNumber(Matrix matrix, double multiplyOnThe) {
        return multiplyOnThe != 1 ? addAndSubtractAndMultiplyOnTheNumber(matrix, null, multiplyOnThe, false) : new Matrix(matrix);
    }

    public static Double det(Matrix matrix) {
        if (isIncompatibleDimensions(matrix)) {
            return null;
        }
        double det = 0;
        if (matrix.getCountRow() == 2) {
            return matrix.get(1, 1) * matrix.get(2, 2) - matrix.get(1, 2) * matrix.get(2, 1);
        } else {
            int k;
            for (int j = 0; j < matrix.getCountRow(); j++) {
                k = j % 2 == 0 ? 1 : -1;
                det += k * matrix.get(1, j + 1) * det(getMinorMatrix(matrix, 0, j));
            }
        }
        return det;
    }

    private static Matrix getMinorMatrix(Matrix matrix, int row, int column) {
        int minorLength = matrix.getCountRow() - 1;
        Matrix minor = new Matrix(minorLength, minorLength);
        int a = 0;
        int b;
        for (int i = 0; i <= minorLength; i++) {
            b = 0;
            for (int j = 0; j <= minorLength; j++) {
                if (i == row) {
                    a = 1;
                } else {
                    if (j == column) {
                        b = 1;
                    } else {
                        minor.set(i - a + 1, j - b + 1, matrix.get(i + 1, j + 1));
                    }
                }
            }
        }
        return minor;
    }

    public static ArrayList<Double> solve(Matrix matrix, ArrayList<Double> rightPart) {
        return new ArrayList<>();
    }

    public static double norm(ArrayList<Double> x) {
        return x.stream().mapToDouble(x1 -> x1 * x1).sum();
    }

    public static double normOfDifference(ArrayList<Double> x0, ArrayList<Double> x1) {
        if (x0.size() != x1.size()) {
            throw new DifferentLengthOfArraysException();
        }
        ArrayList<Double> diff = new ArrayList<>();
        for (int i = 0; i < x0.size(); i++) {
            diff.add(x0.get(i) - x1.get(i));
        }
        return norm(diff);
    }
}
