import java.util.Random;

class MatrixMultiplierHelper     Runnable {
    private int[][] matrixA;
    private int[][] matrixB;
    private int[][] matrixC;
    private int row;
    private int col;

    public MatrixMultiplierHelper(int[][] matrixA, int[][] matrixB, int[][] matrixC, int row, int col) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.matrixC = matrixC;
        this.row = row;
        this.col = col;
    }

    @Override
    public void run() {
        int colsA = matrixA[0].length;
        for (int k = 0; k < colsA; k++) {
            matrixC[row][col] += matrixA[row][k] * matrixB[k][col];
        }
    }
}

public class MatrixMultiplication {
    public static void main(String[] args) throws InterruptedException {
        int rowsA = 4;
        int colsA = 4;
        int rowsB = 4;
        int colsB = 4;

        int[][] matrixA = generateRandomMatrix(rowsA, colsA);
        int[][] matrixB = generateRandomMatrix(rowsB, colsB);
        int[][] matrixC = new int[rowsA][colsB];

        Thread[] threads = new Thread[rowsA * colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                threads[i * colsB + j] = new Thread(new MatrixMultiplierHelper(matrixA, matrixB, matrixC, i, j));
                threads[i * colsB + j].start();
                printMatrix(matrixC);
            }
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Matrix A:");
        printMatrix(matrixA);
        System.out.println("Matrix B:");
        printMatrix(matrixB);
        System.out.println("Matrix C:");
        printMatrix(matrixC);
    }

    private static int[][] generateRandomMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = random.nextInt(10);
            }
        }
        return matrix;
    }

    static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
