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

        double bestAlpha = scorer.calculateBestAlpha();
        System.out.println("Best Alpha: " + bestAlpha);

        classifierTester.testPerformance(bestAlpha);
    }

}
