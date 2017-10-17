package ch.fhnw.dist;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author Hasan Kara <hasan.kara@students.fhnw.ch>
 */
public class HamSpamTuple {

    private BigDecimal hamCount = new BigDecimal(0);
    private BigDecimal spamCount = new BigDecimal(0);

    /**
     * @return P(Word , S)
     */
    public BigDecimal getProbabilitySpam(int numberOfSpamMails, int numberOfHamMails) {
        int totalNumberOfMail = numberOfHamMails + numberOfSpamMails;
        BigDecimal zähler = spamCount.divide(new BigDecimal(totalNumberOfMail));
        BigDecimal nenner = new BigDecimal(numberOfSpamMails).divide(new BigDecimal(totalNumberOfMail));
        return zähler.divide(nenner);
    }

    /**
     * @return P(Word , H)
     */
    public BigDecimal getProbabilityHam(int numberOfSpamMails, int numberOfHamMails) {
        int totalNumberOfMail = numberOfHamMails + numberOfSpamMails;
        BigDecimal zähler = hamCount.divide(new BigDecimal(totalNumberOfMail));
        BigDecimal nenner = new BigDecimal(numberOfHamMails).divide(new BigDecimal(totalNumberOfMail));
        return zähler.divide(nenner);
    }

    public void normalizeByMinBagSize(BigDecimal min) {
        hamCount = hamCount.divide(min, MathContext.DECIMAL128);
        spamCount = spamCount.divide(min, MathContext.DECIMAL128);
    }

    public BigDecimal incrementHam() {
        hamCount = hamCount.add(new BigDecimal(1));
        return hamCount;
    }

    public BigDecimal incrementSpam() {
        spamCount = spamCount.add(new BigDecimal(1));
        return spamCount;
    }

    public BigDecimal getHamCount() {
        return hamCount;
    }

    public BigDecimal getSpamCount() {
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
