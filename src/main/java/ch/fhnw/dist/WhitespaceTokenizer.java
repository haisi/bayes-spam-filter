package ch.fhnw.dist;

/**
 * @author Hasan Kara <hasan.kara@students.fhnw.ch>
 */
public class WhitespaceTokenizer implements Tokenizer {
    @Override
    public String[] getTokens(String sentence) {
        return sentence.split("\\s+");
    }
}
