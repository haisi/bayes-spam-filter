package ch.fhnw.dist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Hasan Kara <hasan.kara@students.fhnw.ch>
 */
public class Main {

    private final Tokenizer tokenizer = new WhitespaceTokenizer();
    private final FileHelper fileHelper = new FileHelper();

    private final SpamClassifier classifier = new SpamClassifier(fileHelper, tokenizer);
    private final Scorer scorer = new Scorer(fileHelper, classifier);

    private final ClassifierTester classifierTester = new ClassifierTester(fileHelper, classifier);

    public static void main(String[] args) throws IOException {
        new Main().start();
    }

    private void start() throws IOException {

        classifier.learn();

        Path path = Paths.get("./test-mail");
        String testMailContent = new String(Files.readAllBytes(path));
        System.out.println(testMailContent);
        System.out.println("Spam probability: " + classifier.totalSpamProbability(testMailContent));

        System.out.print("Do you agree? (y/n)");
        Scanner sc = new Scanner(System.in);
        String answer = sc.next();
        if ("y".equals(answer)) {
            
        } else {

        }


        double bestAlpha = scorer.calculateBestAlpha();
        System.out.println("Best Alpha: " + bestAlpha);

        classifierTester.testPerformance(bestAlpha);
    }

}
