package org.example.ted.recommendation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ted.models.Advertisement;
import org.example.ted.models.User;
import org.example.ted.repositories.AdvertisementRepository;
import org.example.ted.repositories.UserAdvertisementRatingRepository;
import org.example.ted.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Component
public class AdvertisementRecommendation {
    private double[][] R;
    private double[][] V;
    private double[][] F;
    private double[][] r;
    private double h = 0.5;
    private int K = 5;  // number of features

    private UserRepository userRepository;
    private AdvertisementRepository advertisementRepository;
    private UserAdvertisementRatingRepository userAdvertisementRatingRepository;

    @Autowired
    public AdvertisementRecommendation(UserRepository userRepository, AdvertisementRepository advertisementRepository, @Lazy UserAdvertisementRatingRepository userAdvertisementRatingRepository) {
        this.userRepository = userRepository;
        this.advertisementRepository = advertisementRepository;
        this.userAdvertisementRatingRepository = userAdvertisementRatingRepository;
    }

//    @Scheduled(fixedRate = 86_400_000)  // execute with 24 hours rate
    public void factorization() {
        System.out.println("advertisement factorization started");
        int rows = (int) userRepository.count();
        int cols = (int) advertisementRepository.count();

        // V , F initialization
        this.V = matrixInitialization(rows, K);
        this.F = matrixInitialization(K, cols);

        // matrix multiplication
        this.r = multiplication(V, F, rows, cols, K);

        this.R = new double[rows+1][cols+1];

        // error computation
        double eij;
        for (User user : userRepository.findAll()) {
            long userId = user.getId();
            int i = (int) userId;
            for (Advertisement advertisement : advertisementRepository.findAll()) {
                long advertisementId = advertisement.getId();
                int j = (int) advertisementId;
                // gain data from database for R[i][j]
                if (!userAdvertisementRatingRepository.existsByUserAndAdvertisement(user, advertisement)) {   // user has not rated the advertisement
                    R[i][j] = r[i][j];
                }
                else {    // user has visited advertisement
                    R[i][j] = 1;

                    eij = Math.abs(R[i][j] - r[i][j]);
                    int reps = 0;
                    while (eij >= 0.5 && reps++ < 5) {
                        for (int k = 1; k <= K; k++) {
                            V[i][k] = (int) (V[i][k] + (h * 2 * eij * F[k][j]));
                            F[k][j] = (int) (F[k][j] + (h * 2 * eij * V[i][k]));
                        }
                        r[i][j] = cellMultiplication(V, F, i, j, K);
                        eij = R[i][j] - r[i][j];
                    }
                }
            }
        }
        System.out.println("advertisement factorization finished");
    }

    public double[][] getMatrixR() {
        return this.R;
    }

    public double[][] multiplication(double[][] V, double[][] F, int rows, int cols, int K) {
        double[][] r = new double[rows+1][cols+1];
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                r[i][j] = 0;
                for (int k = 1; k <= K; k++) {
                    r[i][j] += V[i][k] * F[k][j];
                }
                if (r[i][j] >= 1) {
                    r[i][j] = 1;
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
        if (r >= 1) {
            r = 1;
        }
        else {
            r = 0;
        }
        return r;
    }

    public double[][] matrixInitialization(int rows, int cols) {
        Random rand = new Random();
        double[][] matrix = new double[rows+1][cols+1];
        for (int i = 0; i <= rows; i++) {
            for (int j = 0; j <= cols; j++) {
                matrix[i][j] = Math.abs(rand.nextInt() % 2);
            }
        }
        return matrix;
    }

    public double[] getUserVector(double[][] R, int row) {
        if (R == null) {
            return null;
        }
        double[] vector;
        vector = R[row].clone();
        return vector;
    }
}
