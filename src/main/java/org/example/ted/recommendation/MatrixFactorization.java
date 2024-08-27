package org.example.ted.recommendation;

import org.example.ted.models.UserAdvertisementMatrix;
import org.example.ted.models.UserAdvertisementMatrixId;
import org.example.ted.repositories.UserAdvertisementMatrixRepository;

import java.util.Optional;

public class MatrixFactorization {
    private double[][] R;
    private double[][] V;
    private double[][] F;
    private double h = 0.0002;
    private UserAdvertisementMatrixRepository userAdvertisementMatrixRepository;

    public MatrixFactorization(double[][] R, double[][] V, double[][] F) {
        this.R = R;
        this.V = V;
        this.F = F;
    }

    public double[][] factorize(int rows, int cols, int K) {

        // V , F initialization
        double[][] V = matrixInitialization(rows, K);
        double[][] F = matrixInitialization(K, cols);

        // matrix multiplication
        double[][] r = multiplication(V, F, rows, cols, K);

        // error computation
        double eij;
        int[][] R = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // gain data from database for R[i][j]
                Optional<UserAdvertisementMatrix> Rij = userAdvertisementMatrixRepository.findById(new UserAdvertisementMatrixId(i+1, j+1));
                if (Rij.get().getValue().equals("known")) {
                    r[i][j] = Rij.get().getValue();
                    continue;
                }

                eij = R[i][j] - r[i][j];
                int reps = 0;
                while (eij > 0.1 && reps < 5) {
                    for (int k = 0; k < K; k++) {
                        V[i][k] = (int) (V[i][k] + (h * 2 * eij * F[k][j]));
                        F[k][j] = (int) (F[k][j] + (h * 2 * eij * V[i][k]));
                    }
                    r[i][j] = cellMultiplication(V, F, i, j, K);
                    eij = R[i][j] - r[i][j];
                    reps++;
                }
            }
        }
        return r;
    }

    public double[][] multiplication(double[][] V, double[][] F, int rows, int cols, int K) {
        double[][] r = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                r[i][j] = 0;
                for (int k = 0; k < K; k++) {
                    r[i][j] += V[i][k] * F[k][j];
                }
            }
        }
        return r;
    }

    public double cellMultiplication(double[][] V, double[][] F, int i, int j, int K) {
        double r = 0;
        for (int k = 0; k < K; k++) {
            r += V[i][k] * F[k][j];
        }
        return r;
    }

    public double[][] matrixInitialization(int rows, int cols) {
        double[][] matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = (Math.random() * 10);
            }
        }
        return matrix;
    }
}
