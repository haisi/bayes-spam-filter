package ch.fhnw.dist;

import java.util.*;

/**
 * @author Hasan Kara <hasan.kara@students.fhnw.ch>
 */
public class SpamClassifier {

    final Map<String, HamSpamTuple> words = new HashMap<>();
    final FileHelper fileHelper;
    final Tokenizer tokenizer;

    private int numberOfHamMails;
    private int numberNormalizedOfHamMails = -1;
    private int numberOfSpamMails;
    private int numberNormalizedOfSpamMails = -1;

    public SpamClassifier(FileHelper fileHelper, Tokenizer tokenizer) {
        this.fileHelper = fileHelper;
        this.tokenizer = tokenizer;
    }

    public void learn() {
        numberOfHamMails = countWords("data/ham-anlern", false);
        numberOfSpamMails = countWords("data/spam-anlern", true);
    }

    public Map<String, HamSpamTuple> getWords() {
        return words;
    }

    int getNumberOfHamMails() {
        return numberOfHamMails;
    }

    int getNormalizedNumberOfHamMails() {
        if (numberNormalizedOfHamMails == -1) {
            calculateAndSetNormalizedNumberOfMails();
        }

        return numberNormalizedOfHamMails;
    }

    int getNumberOfSpamMails() {
        return numberOfSpamMails;
    }

    int getNormalizedNumberOfSpamMails() {
        if (numberNormalizedOfSpamMails == -1) {
            calculateAndSetNormalizedNumberOfMails();
        }

        return numberNormalizedOfSpamMails;
    }

    private void calculateAndSetNormalizedNumberOfMails() {
        //Nenner verkleinern
        int min = Math.min(numberOfSpamMails, numberOfHamMails);
        if (min != 0) {
            numberOfSpamMails = numberOfSpamMails / min;
            numberOfHamMails = numberOfHamMails / min;
            words.forEach((s, hamSpamTuple) -> {
                hamSpamTuple.normalizeByMinBagSize(min);
            });
        }
        numberNormalizedOfSpamMails = numberOfSpamMails;
        numberNormalizedOfHamMails = numberOfHamMails;
    }

    public double totalSpamProbability(String mailContent) {
        return totalSpamProbability(mailContent, getNormalizedNumberOfSpamMails(), getNormalizedNumberOfHamMails());
    }

    /**
     * Berechnet total Wahrscheinlichkeit
     *
     * @param mailContent
     * @param numberOfSpamMails
     * @param numberOfHamMails
     * @return
     */
    public double totalSpamProbability(String mailContent, int numberOfSpamMails, int numberOfHamMails) {

        String[] tokens = tokenizer.getTokens(mailContent);
        HashSet<String> nonDuplicateTokens = new HashSet<>(Arrays.asList(tokens));

        Map<String, HamSpamTuple> wordsInMailAndDatabase = new HashMap<>();
        for (String word : nonDuplicateTokens) {
            HamSpamTuple value = words.get(word);
            // Because some words from the calibration data set aren't available in the training set
            if (value != null) {
                wordsInMailAndDatabase.put(word, value);
            }
        }

        //Variante 1
//        double productSpamProbability = spamProbability;
//        double productHamProbability = hamProbability;
//        for (HamSpamTuple hamSpamTuple : wordsInMailAndDatabase.values()) {
//            double pSpam = hamSpamTuple.getProbabilitySpam(numberOfSpamMails, numberOfHamMails);
//            double pHam = hamSpamTuple.getProbabilityHam(numberOfSpamMails, numberOfHamMails);
//            if(pSpam > 0){
//                productSpamProbability *= pSpam;
//            }
//            if(pHam > 0){
//                productHamProbability *= pHam;
//            }
//        }
//
//        double zaehler = productSpamProbability;
//        double nenner = productSpamProbability + productHamProbability;
//
//        return zaehler / nenner;

        //Variante 2
        double product = 1;
        for (HamSpamTuple hamSpamTuple : wordsInMailAndDatabase.values()) {
            double pSpam = hamSpamTuple.getProbabilitySpam(numberOfSpamMails, numberOfHamMails);
            double pHam = hamSpamTuple.getProbabilityHam(numberOfSpamMails, numberOfHamMails);
            if(pSpam > 0 && pHam > 0){
                product *= (pHam * pSpam);
            }
        }
        return 1/(1+product);
    }

    private int countWords(String folder, boolean spam) {
        List<String> mailList = FileHelper.getFiles(folder);
        mailList.stream()
                .map(fileName -> folder + "/" + fileName)
                .map(fileHelper::getFileContent) // Read Mail content
                .map(tokenizer::getTokens) // tokenize (returns String[])
                .map(array -> new HashSet<>(Arrays.asList(array)).toArray(new String[0])) // Remove duplicates tokens in mail
                .flatMap(Arrays::stream)
                .forEach(word -> count(word, spam));

        return mailList.size();
    }

    public void count(String word, boolean spam) {
        HamSpamTuple hamSpamTuple = words.get(word);
        if (hamSpamTuple == null) {
            hamSpamTuple = new HamSpamTuple();
            words.put(word, hamSpamTuple);
        }

        increment(spam, hamSpamTuple);
    }

    private void increment(boolean spam, HamSpamTuple value) {
        if (spam) {
            value.incrementSpam();
        } else {
            value.incrementHam();
        }
    }

    void printSpamProbabilityForEachWord(int finalNumberOfSpamMails, int finalNumberOfHamMails) {
        words.forEach((s, hamSpamTuple) -> {
            System.out.println(s + "\t" + hamSpamTuple.getProbabilitySpam(finalNumberOfSpamMails, finalNumberOfHamMails));
        });
    }
}
