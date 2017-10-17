package ch.fhnw.dist;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hasan Kara <hasan.kara@students.fhnw.ch>
 */
public class Scorer {

    private final FileHelper fileHelper;
    private final SpamClassifier spamClassifier;

    public Scorer(FileHelper fileHelper, SpamClassifier spamClassifier) {
        this.fileHelper = fileHelper;
        this.spamClassifier = spamClassifier;
    }

    public double calculateBestAlpha() {

        List<String> hamMailContents = fileHelper.getMailContentOfMails("data/ham-kallibrierung");
        List<String> spamMailContents = fileHelper.getMailContentOfMails("data/spam-kallibrierung");

        double bestF1Score = 0d;
        BigDecimal bestAlpha = BigDecimal.ZERO;
        Score bestScore = new Score();

        BigDecimal alpha = new BigDecimal("0.00000000005");
        BigDecimal endAlpha = new BigDecimal("0.0000000002");
        BigDecimal steps = new BigDecimal("0.00000000005"); // Steps with which alpha increases;

        for (; alpha.compareTo(endAlpha) == -1 ; alpha = alpha.add(steps)) {

            System.out.println("Iteration: " + alpha);

            final Score score = new Score();

            for (String hamMailContent : hamMailContents) {
                double spamProbability = spamClassifier.totalSpamProbability(hamMailContent).doubleValue();
                if (new BigDecimal(spamProbability).compareTo(alpha) == -1) {
                    // Correct
                    score.incrementTp();
                } else {
                    // Falsely classified as spam
                    score.incrementFp();
                }
            }

            for (String spamMailContent : spamMailContents) {
                double spamProbability = spamClassifier.totalSpamProbability(spamMailContent).doubleValue();
                if (new BigDecimal(spamProbability).compareTo(alpha) == 1) {
                    // Correctly classified as spam
                    score.incrementTp();
                } else {
                    // Should have been classified as spam --> Increase False Negative
                    score.incrementFn();
                }
            }

            // Check whether with the current alpha we get a better F1 score
            // and change the most effective alpha value accordingly.
            double f1Score = score.getF1Score().doubleValue();
            if (f1Score > bestF1Score) {
                bestF1Score = f1Score;
                bestAlpha = alpha;
                bestScore = score;
                System.out.println("Found new best");
            }
        }

        System.out.println("Precision: " + bestScore.getPrecision());
        System.out.println("Recall: " + bestScore.getRecall());
        System.out.println("Best F1 score: " + bestF1Score);
        System.out.println("Best alphaaa: " + bestAlpha.toPlainString());

        return bestAlpha.doubleValue();
    }

}
