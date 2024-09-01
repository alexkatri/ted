package org.example.ted.recommendation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ted.models.Advertisement;
import org.example.ted.models.UserAdvertisementRating;
import org.example.ted.models.UserAdvertisementRatingId;
import org.example.ted.repositories.AdvertisementRepository;
import org.example.ted.repositories.UserAdvertisementRatingRepository;
import org.example.ted.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MatrixFactorization {
    private double[][] R;
    private double[][] V;
    private double[][] F;
    private double[][] r;
    private double h = 0.0002;
    private UserRepository userRepository;
    private AdvertisementRepository advertisementRepository;
    private UserAdvertisementRatingRepository userAdvertisementRatingRepository;

    public double[][] factorization(int K) {

        int rows = (int) userRepository.count();
        int cols = (int) advertisementRepository.count();

        // V , F initialization
        this.V = matrixInitialization(rows, K);
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= K; j++) {
                System.out.print(V[i][j] + " ");
            }
            System.out.println();
        }
        this.F = matrixInitialization(K, cols);
        for (int i = 1; i <= K; i++) {
            for (int j = 1; j <= cols; j++) {
                System.out.print(F[i][j] + " ");
            }
            System.out.println();
        }

        // matrix multiplication
        this.r = multiplication(V, F, rows, cols, K);
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                System.out.print(r[i][j] + " ");
            }
            System.out.println();
        }

        // get R from database
        //

        // error computation
        double eij;
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                // gain data from database for R[i][j]
                Optional<UserAdvertisementRating> Rij = userAdvertisementRatingRepository.findById(new UserAdvertisementRatingId(i, j));
                if (Rij.get().getValue().equals("known")) {
                    r[i][j] = Rij.get().getValue();
                    continue;
                }

                eij = R[i][j] - r[i][j];
                int reps = 0;
                while (eij > 0.1 && reps < 5) {
                    for (int k = 1; k <= K; k++) {
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
        double[][] r = new double[rows+1][cols+1];
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                r[i][j] = 0;
                for (int k = 1; k <= K; k++) {
                    r[i][j] += V[i][k] * F[k][j];
                }
            }
        }
        return r;
    }

    public double cellMultiplication(double[][] V, double[][] F, int i, int j, int K) {
        double r = 0;
        for (int k = 1; k <= K; k++) {
            r += V[i][k] * F[k][j];
        }
        return r;
    }

    public double[][] matrixInitialization(int rows, int cols) {
        double[][] matrix = new double[rows+1][cols+1];
        for (int i = 0; i <= rows; i++) {
            for (int j = 0; j <= cols; j++) {
                matrix[i][j] = (Math.random() * 10);
            }
        }
        return matrix;
    }

    public double setRij(int i, int j, double value) {
        this.R[i][j] = value;
        return R[i][j];
    }

    public double[] getVector(double[][] R, int row) {
        double[] vector;
        vector = R[row].clone();
        return vector;
    }

    public List<Advertisement> getAdvertisements(int user_id) {
        List<Advertisement> advertisements = new ArrayList<>();
        double[] vector = getVector(R, user_id);
        for (int i = 1; i <= vector.length; i++) {
            if (vector[i] > 0) {
                Advertisement ad = advertisementRepository.findById(Long.valueOf(i)).get();
                // if ad not exists in viewed ads by user[user_id]
                advertisements.add(ad);
            }
        }
        return advertisements;
    }
}
