package ch.fhnw.dist;

import java.util.List;

/**
 * @author Hasan Kara <hasan.kara@students.fhnw.ch>
 */
public class ClassifierTester {

    final FileHelper fileHelper;
    final SpamClassifier classifier;

    public ClassifierTester(FileHelper fileHelper, SpamClassifier classifier) {
        this.fileHelper = fileHelper;
        this.classifier = classifier;
    }

    void testPerformance(double alpha) {
        List<String> hamMailContent = fileHelper.getMailContentOfMails("data/ham-test");
        int numberOfHamMails = hamMailContent.size();
        int correctClassifiedHam = 0;

        for (String content : hamMailContent) {
            double spamProbability = classifier.totalSpamProbability(content);
            if (spamProbability < alpha) {
                correctClassifiedHam++;
            }
        }

        double correctPercentageHam = 100 / numberOfHamMails * correctClassifiedHam;
        System.out.println("Percentage of correctly classified ham mail: " + correctPercentageHam);

        List<String> spamMailContents = fileHelper.getMailContentOfMails("data/spam-test");
        int numberOfSpamMails = spamMailContents.size();
        int correctClassifiedSpam = 0;

        for (String content : spamMailContents) {
            double spamProbability = classifier.totalSpamProbability(content);
            if (spamProbability < alpha) {
                correctClassifiedSpam++;
            }
        }

        double correctPercentageSpam = 100 / numberOfSpamMails * correctClassifiedSpam;
        System.out.println("Percentage of correctly classified spam mail: " + correctPercentageSpam);

    }

}
