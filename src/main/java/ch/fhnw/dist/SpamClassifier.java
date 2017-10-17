package ch.fhnw.dist;

import java.math.BigDecimal;
import java.math.MathContext;
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
                hamSpamTuple.normalizeByMinBagSize(new BigDecimal(min));
            });
        }
        numberNormalizedOfSpamMails = numberOfSpamMails;
        numberNormalizedOfHamMails = numberOfHamMails;
    }

    public BigDecimal totalSpamProbability(String mailContent) {
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
    public BigDecimal totalSpamProbability(String mailContent, int numberOfSpamMails, int numberOfHamMails) {

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
//        BigDecimal productSpamProbability = new BigDecimal("0.5");
//        BigDecimal productHamProbability = new BigDecimal("0.5");
//        for (HamSpamTuple hamSpamTuple : wordsInMailAndDatabase.values()) {
//            BigDecimal pSpam = hamSpamTuple.getProbabilitySpam(numberOfSpamMails, numberOfHamMails);
//            BigDecimal pHam = hamSpamTuple.getProbabilityHam(numberOfSpamMails, numberOfHamMails);
//            if(pSpam.compareTo(BigDecimal.ZERO) == 1){
//                productSpamProbability = productHamProbability.multiply(pSpam);
//            }
//            if(pHam.compareTo(BigDecimal.ZERO) == 1){
//                productHamProbability = productHamProbability.multiply(pHam);
//            }
//        }
//
//        BigDecimal zaehler = productSpamProbability;
//        BigDecimal nenner = productSpamProbability.add(productHamProbability);
//
//        return zaehler.divide(nenner, MathContext.DECIMAL128);

        //Variante 2
        BigDecimal product = new BigDecimal(1);
        for (HamSpamTuple hamSpamTuple : wordsInMailAndDatabase.values()) {
            BigDecimal pSpam = hamSpamTuple.getProbabilitySpam(numberOfSpamMails, numberOfHamMails);
            BigDecimal pHam = hamSpamTuple.getProbabilityHam(numberOfSpamMails, numberOfHamMails);
            if(pSpam.compareTo(new BigDecimal(0)) == 1 && pHam.compareTo(new BigDecimal(0)) == 1){
                product = product.multiply(pHam.multiply(pSpam));
            }
        }
        return new BigDecimal(1).divide(new BigDecimal(1).add(product), MathContext.DECIMAL128);
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
