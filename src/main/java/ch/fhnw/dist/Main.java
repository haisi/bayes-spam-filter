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

    public static void main(String[] args) {
        new Main().start();
    }

    void start() {
        countWords("data/ham-anlern", false);
        countWords("data/spam-anlern", true);

        words.forEach((s, hamSpamTuple) -> {
            System.out.println(s + "\t" + hamSpamTuple);
        });
    }

    private void countWords(String folder, boolean spam) {
        List<String> fileListHamLearning = getFiles(folder);
        fileListHamLearning.stream()
                           .map(fileName -> folder + "/" + fileName)
                           .map(this::getFileContent) // Read Mail content
                           .map(tokenizer::getTokens) // tokenize (returns String[])
                           .map(array -> new HashSet<>(Arrays.asList(array)).toArray(new String[0])) // Remove duplicates tokens in mail
                           .flatMap(Arrays::stream)
                           .forEach(word -> count(word, spam));
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