package ch.fhnw.dist;

import java.util.*;

/**
 * @author Hasan Kara <hasan.kara@students.fhnw.ch>
 */
public class Main {

    private final Tokenizer tokenizer = new WhitespaceTokenizer();
    private final FileHelper fileHelper = new FileHelper();

    private final SpamClassifier classifier = new SpamClassifier(fileHelper, tokenizer);

    private double spamProbability = 0.5d;
    private double hamProbability = 1 - spamProbability;

    public static void main(String[] args) {
        new Main().start();
    }

    private void start() {

        classifier.learn();

        int numberOfHamMails = classifier.getNumberOfHamMails();
        int numberOfSpamMails = classifier.getNumberOfSpamMails();

        //Nenner verkleinern
        int min = Math.min(numberOfSpamMails, numberOfHamMails);
        if (min != 0) {
            numberOfSpamMails = numberOfSpamMails / min;
            numberOfHamMails = numberOfHamMails / min;
            classifier.getWords().forEach((s, hamSpamTuple) -> {
                hamSpamTuple.normalizeByMinBagSize(min);
            });
        }
        int finalNumberOfSpamMails = numberOfSpamMails;
        int finalNumberOfHamMails = numberOfHamMails;

        classifier.getWords().forEach((s, hamSpamTuple) -> {
            System.out.println(s + "\t" + hamSpamTuple.getProbabilitySpam(finalNumberOfSpamMails, finalNumberOfHamMails));
        });

        System.out.println("\n---------------TOTAL SPAM PROBABILITY FOR SPAM MAIL:---------------");
        System.out.println(classifier.totalSpamProbability(fileHelper.getFileContent("data/spam-kallibrierung/00040.949a3d300eadb91d8745f1c1dab51133"), finalNumberOfSpamMails, finalNumberOfHamMails));

        System.out.println("\n---------------TOTAL SPAM PROBABILITY FOR HAM MAIL:---------------");
        System.out.println(classifier.totalSpamProbability(fileHelper.getFileContent("data/ham-kallibrierung/0001.ea7e79d3153e7469e7a9c3e0af6a357e"), finalNumberOfSpamMails, finalNumberOfHamMails));
    }

}
