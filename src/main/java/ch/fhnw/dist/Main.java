package ch.fhnw.dist;

import java.util.*;

/**
 * @author Hasan Kara <hasan.kara@students.fhnw.ch>
 */
public class Main {

    private final Tokenizer tokenizer = new WhitespaceTokenizer();
    private final FileHelper fileHelper = new FileHelper();

    private final SpamClassifier classifier = new SpamClassifier(fileHelper, tokenizer);
    private final Scorer scorer = new Scorer(fileHelper, classifier);

    private final ClassifierTester classifierTester = new ClassifierTester(fileHelper, classifier);

    public static void main(String[] args) {
        new Main().start();
    }

    private void start() {

        classifier.learn();

        int numberOfHamMails = classifier.getNumberOfHamMails();
        int numberOfSpamMails = classifier.getNumberOfSpamMails();

        int normalizedNumberOfSpamMails = classifier.getNormalizedNumberOfSpamMails();
        int normalizedNumberOfHamMails = classifier.getNormalizedNumberOfHamMails();

        double bestAlpha = scorer.calculateBestAlpha();
        System.out.println("Best Alpha: " + bestAlpha);
        classifierTester.testPerformance(bestAlpha);

//        System.out.println(classifier.totalSpamProbability(fileHelper.getFileContent("data/spam-kallibrierung/00040.949a3d300eadb91d8745f1c1dab51133"), normalizedNumberOfSpamMails, normalizedNumberOfHamMails));
    }

    private void printSpamProbabilityForEachWord(int finalNumberOfSpamMails, int finalNumberOfHamMails) {
        classifier.getWords().forEach((s, hamSpamTuple) -> {
            System.out.println(s + "\t" + hamSpamTuple.getProbabilitySpam(finalNumberOfSpamMails, finalNumberOfHamMails));
        });
    }

}
