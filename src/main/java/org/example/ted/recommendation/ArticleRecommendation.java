package org.example.ted.recommendation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ted.models.Article;
import org.example.ted.models.User;
import org.example.ted.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Component
public class ArticleRecommendation {
    private double[][] R;
    private double[][] V;
    private double[][] F;
    private double[][] r;
    private double h = 0.5;
    private int K = 5;  // number of features

    private UserRepository userRepository;
    private ArticleRepository articleRepository;
    private UserArticleRatingRepository userArticleRatingRepository;

    @Autowired
    public ArticleRecommendation(UserRepository userRepository, ArticleRepository articleRepository, @Lazy UserArticleRatingRepository userArticleRatingRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.userArticleRatingRepository = userArticleRatingRepository;
    }

    @Scheduled(fixedRate = 86_400_000)  // execute with 24 hours rate
    public void factorization() {
        System.out.println("article factorization started");
        int rows = (int) userRepository.count();
        int cols = (int) articleRepository.count();

        // V , F initialization
        this.V = matrixInitialization(rows, K);
        this.F = matrixInitialization(K, cols);

        // matrix multiplication
        this.r = multiplication(V, F, rows, cols, K);

        this.R = new double[rows+1][cols+1];

        // error computation
        double eij;
        for (int i = 1; i <= rows; i++) {
            User user = userRepository.findById(i).orElse(null);
            for (int j = 1; j <= cols; j++) {
                Article ar = articleRepository.findById((long) j).orElse(null);
                if (ar == null) {
                    R[i][j] = 0;
                    continue;
                }
                // gain data from database for R[i][j]
                if (!userArticleRatingRepository.existsByUserAndArticle(user, ar)) {    // user has not rated the article
                    R[i][j] = r[i][j];
                } else {    // user has visited ad
                    R[i][j] = userArticleRatingRepository.findByUserAndArticle(user,ar).getValue();

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
        System.out.println("article factorization finished");
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

    public List<Article> getArticles(int user_id) {
        List<Article> articles = new ArrayList<>();
        double[] vector = getUserVector(R, user_id);
        for (int i = 1; i <= vector.length; i++) {
            if (vector[i] > 0) {
                Article ar = articleRepository.findById((long) i).orElse(null);
                // if ar not exists in viewed ads by user[user_id]
                if (ar != null) {
                    articles.add(ar);
                }
            }
        }
        return articles;
    }
}
