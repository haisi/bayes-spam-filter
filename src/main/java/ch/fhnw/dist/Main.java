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

    private double spamProbability = 0.5d;
    private double hamProbability = 1 - spamProbability;

    public static void main(String[] args) {
        new Main().start();
    }

    private void start() {

        classifier.learn();

        int numberOfHamMails = classifier.getNumberOfHamMails();
        int numberOfSpamMails = classifier.getNumberOfSpamMails();

        int normalizedNumberOfSpamMails = classifier.getNormalizedNumberOfSpamMails();
        int normalizedNumberOfHamMails = classifier.getNormalizedNumberOfHamMails();
//        printSpamProbabilityForEachWord(normalizedNumberOfSpamMails, normalizedNumberOfHamMails);

        System.out.println("Best Alpha: " + scorer.calculateBestAlpha());

        System.out.println("\n---------------TOTAL SPAM PROBABILITY FOR SPAM MAIL:---------------");
//        System.out.println(classifier.totalSpamProbability(fileHelper.getFileContent("data/spam-kallibrierung/00040.949a3d300eadb91d8745f1c1dab51133"), normalizedNumberOfSpamMails, normalizedNumberOfHamMails));

        System.out.println("\n---------------TOTAL SPAM PROBABILITY FOR HAM MAIL:---------------");
//        System.out.println(classifier.totalSpamProbability(fileHelper.getFileContent("data/ham-kallibrierung/0001.ea7e79d3153e7469e7a9c3e0af6a357e"), normalizedNumberOfSpamMails, normalizedNumberOfHamMails));
    }

    private void printSpamProbabilityForEachWord(int finalNumberOfSpamMails, int finalNumberOfHamMails) {
        classifier.getWords().forEach((s, hamSpamTuple) -> {
            System.out.println(s + "\t" + hamSpamTuple.getProbabilitySpam(finalNumberOfSpamMails, finalNumberOfHamMails));
        });
    }

}
