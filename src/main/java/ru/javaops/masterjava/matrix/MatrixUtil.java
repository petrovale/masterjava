package ru.javaops.masterjava.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    /** Поток-вычислитель группы ячеек матрицы. */
    public static class MultiplierThread implements Callable {
        /** Первая (левая) матрица. */
        private final int[][] matrixA;
        /** Вторая (правая) матрица. */
        private final int[][] matrixB;
        /** Результирующая матрица. */
        private final int[][] matrixC;
        /** Начальный индекс. */
        private final int firstIndex;
        /** Конечный индекс. */
        private final int lastIndex;
        /** Число членов суммы при вычислении значения ячейки. */
        private final int sumLength;

        public MultiplierThread(final int[][] matrixA,
                                final int[][] matrixB,
                                final int[][] matrixC,
                                final int firstIndex,
                                final int lastIndex) {
            this.matrixA  = matrixA;
            this.matrixB = matrixB;
            this.matrixC = matrixC;
            this.firstIndex   = firstIndex;
            this.lastIndex    = lastIndex;

            sumLength = matrixB.length;
        }

        /**Вычисление значения в одной ячейке.
         *
         * @param row Номер строки ячейки.
         * @param col Номер столбца ячейки.
         */
        private void calcValue(final int row, final int col)
        {
            int sum = 0;
            for (int i = 0; i < sumLength; ++i)
                sum += matrixA[row][i] * matrixB[i][col];
            matrixC[row][col] = sum;
        }

        @Override
        public int[][] call() throws Exception {

            final int colCount = matrixB[0].length;  // Число столбцов результирующей матрицы.
            for (int index = firstIndex; index < lastIndex; ++index)
                calcValue(index / colCount, index % colCount);

            return null;
        }
    }

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        final int threadCount = 10; // Число потоков
        final int cellsForThread = (matrixSize*matrixSize) / threadCount;  // Число вычисляемых ячеек на поток.
        int firstIndex = 0; // Индекс первой вычисляемой ячейки.

        List<Callable<Integer>> tasks = new ArrayList<>();

        // Создание и запуск потоков.
        for (int threadIndex = threadCount - 1; threadIndex >=0; --threadIndex) {
            int lastIndex = firstIndex + cellsForThread;  // Индекс последней вычисляемой ячейки.

            tasks.add(new MultiplierThread(matrixA, matrixB, matrixC, firstIndex, lastIndex));
            firstIndex = lastIndex;
        }

        executor.invokeAll(tasks);

        return matrixC;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }
                matrixC[i][j] = sum;
            }
        }
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
