package ch.fhnw.dist;

/**
 * @author Hasan Kara <hasan.kara@students.fhnw.ch>
 */
public class HamSpamTuple {

    private int hamCount = 0;
    private int spamCount = 0;

    public float getAlpha() {
        return spamCount / (hamCount + spamCount);
    }

    public int incrementHam() {
        hamCount++;
        return hamCount;
    }

    public int incrementSpam() {
        spamCount++;
        return spamCount;
    }

    public int getHamCount() {
        return hamCount;
    }

    public int getSpamCount() {
        return spamCount;
    }

    @Override
    public String toString() {
        return "{" +
                "hamCount=" + hamCount +
                ", spamCount=" + spamCount +
                '}';
    }
}
