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
        Main main = new Main();
        main.getFiles("data/ham-anlern");
        String fileContent = main.getFileContent("data/ham-anlern/2551.3b1f94418de5bd544c977b44bcc7e740");
        for (String word : main.tokenizer.getTokens(fileContent)) {
            main.count(word, false);
        }

        main.words.forEach((s, hamSpamTuple) -> {
            System.out.println(s + "\t" + hamSpamTuple);
        });
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
            fileNames.add("data/" + file.getName());
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
