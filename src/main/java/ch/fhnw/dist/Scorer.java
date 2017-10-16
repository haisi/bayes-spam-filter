package ch.fhnw.dist;

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

        List<String> hamMailContents = getMailContentOfMails("data/ham-kallibrierung");
        List<String> spamMailContents = getMailContentOfMails("data/spam-kallibrierung");

        double bestF1Score = 0d;
        double bestAlpha = 0d;

        double alpha = 0.05;
        double endAlpha = 1d;
        double steps = 0.05; // Steps with which alpha increases;

        for (; alpha < endAlpha; alpha += steps) {

            final Score score = new Score();

            for (String hamMailContent : hamMailContents) {
                double spamProbability = spamClassifier.totalSpamProbability(hamMailContent);
                if (spamProbability < alpha) {
                    // Correct
                    score.incrementTp();
                } else {
                    // Falsely classified as spam
                    score.incrementFp();
                }
            }

            for (String spamMailContent : spamMailContents) {
                double spamProbability = spamClassifier.totalSpamProbability(spamMailContent);
                if (spamProbability > alpha) {
                    // Correctly classified as spam
                    score.incrementTp();
                } else {
                    // Should have been classified as spam --> Increase False Negative
                    score.incrementFn();
                }
            }

            // Check whether with the current alpha we get a better F1 score
            // and change the most effective alpha value accordingly.
            double f1Score = score.getF1Score();
            if (f1Score > bestF1Score) {
                bestF1Score = f1Score;
                bestAlpha = alpha;
            }
        }

        return bestAlpha;
    }

    public List<String> getMailContentOfMails(String folder) {
        return FileHelper.getFiles(folder)
                         .stream()
                         .map(fileName -> folder + "/" + fileName)
                         .map(fileHelper::getFileContent)
                         .collect(Collectors.toList());
    }

}
