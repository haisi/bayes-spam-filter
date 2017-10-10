package ch.fhnw.dist;

/**
 * @author Hasan Kara <hasan.kara@students.fhnw.ch>
 */
public class HamSpamTuple {

    private double hamCount = 0;
    private double spamCount = 0;

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

    public void normalizeByMinBagSize(double min) {
        hamCount /= min;
        spamCount /= min;
    }

    public double incrementHam() {
        hamCount++;
        return hamCount;
    }

    public double incrementSpam() {
        spamCount++;
        return spamCount;
    }

    public double getHamCount() {
        return hamCount;
    }

    public double getSpamCount() {
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
