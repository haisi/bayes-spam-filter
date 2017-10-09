package ch.fhnw.dist;

/**
 * @author Hasan Kara <hasan.kara@students.fhnw.ch>
 */
public class HamSpamTuple {

    private int hamCount = 0;
    private int spamCount = 0;

    /**
     * @return P(Word , S)
     */
    public double getProbabilitySpam(int numberOfSpamMails, int numberOfHamMails) {
        int totalNumberOfMail = numberOfHamMails + numberOfSpamMails;
        double zähler = (double) spamCount / (double) totalNumberOfMail;
        double nenner = (double) numberOfSpamMails / (double) totalNumberOfMail;
        return zähler / nenner;
    }

    /**
     * @return P(Word , H)
     */
    public double getProbabilityHam(int numberOfSpamMails, int numberOfHamMails) {
        int totalNumberOfMail = numberOfHamMails + numberOfSpamMails;
        double zähler = (double) hamCount / (double) totalNumberOfMail;
        double nenner = (double) numberOfHamMails / (double) totalNumberOfMail;
        return zähler / nenner;
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
