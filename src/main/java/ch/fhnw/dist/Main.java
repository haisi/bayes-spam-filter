package ch.fhnw.dist;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author Hasan Kara <hasan.kara@students.fhnw.ch>
 */
public class Main {

    final Map<String, HamSpamTuple> words = new HashMap<>();
    final Tokenizer tokenizer = new WhitespaceTokenizer();

    private double spamProbability = 0.5d;
    private double hamProbability = 1 - spamProbability;

    public static void main(String[] args) {
        new Main().start();
    }

    void start() {
        int numberOfHamMails = countWords("data/ham-anlern", false);
        int numberOfSpamMails = countWords("data/spam-anlern", true);

        //Nenner verkleinern
        int min = Math.min(numberOfSpamMails,numberOfHamMails);
        if(min != 0) {
            numberOfSpamMails = numberOfSpamMails / min;
            numberOfHamMails = numberOfHamMails / min;
        }
        int finalNumberOfSpamMails = numberOfSpamMails;
        int finalNumberOfHamMails = numberOfHamMails;
        
        words.forEach((s, hamSpamTuple) -> {
            System.out.println(s + "\t" + hamSpamTuple.getProbabilitySpam(finalNumberOfSpamMails, finalNumberOfHamMails));
        });

        System.out.println("---------------TOTAL SPAM PROBABILITY:---------------");
        System.out.println(totalSpamProbability(getFileContent("data/spam-kallibrierung/00040.949a3d300eadb91d8745f1c1dab51133"), numberOfSpamMails, numberOfHamMails));

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

        double productSpamProbability = spamProbability;
        double productHamProbability = hamProbability;
        for (HamSpamTuple hamSpamTuple : wordsInMailAndDatabase.values()) {
            // TODO: the product of the spam and ham probability can be 0!!
            // TODO: Fix!
            productSpamProbability *= hamSpamTuple.getProbabilitySpam(numberOfSpamMails, numberOfHamMails);
            productHamProbability *= hamSpamTuple.getProbabilityHam(numberOfSpamMails, numberOfHamMails);
        }

        double zähler = productSpamProbability;
        double nenner = productSpamProbability + productHamProbability;

        return zähler / nenner;
    }

    private int countWords(String folder, boolean spam) {
        List<String> mailList = getFiles(folder);
        mailList.stream()
                           .map(fileName -> folder + "/" + fileName)
                           .map(this::getFileContent) // Read Mail content
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

    private List<String> getFiles(String folder) {
        List<String> fileNames = new ArrayList<>();
        for (File file : getResourceFolderFiles(folder)) {
            fileNames.add(file.getName());
        }
        return fileNames;
    }

    private static File[] getResourceFolderFiles(String folder) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folder);
        String path = url.getPath();
        return new File(path).listFiles();
    }

    private String getFileContent(String fileName) {

        StringBuilder result = new StringBuilder("");

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();

    }


}
