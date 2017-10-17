package ch.fhnw.dist;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author Hasan Kara <hasan.kara@students.fhnw.ch>
 */
public class Score {

    private BigDecimal truePositives = new BigDecimal(0);
    private BigDecimal falsePositives = new BigDecimal(0);
    private BigDecimal falseNegatives = new BigDecimal(0);

    public void incrementTp() {
        truePositives = truePositives.add(BigDecimal.ONE);
    }

    public void incrementFp() {
        falsePositives = falsePositives.add(BigDecimal.ONE);
    }

    public void incrementFn() {
        falseNegatives = falseNegatives.add(BigDecimal.ONE);
    }


    public BigDecimal getPrecision() {
        return truePositives.divide(truePositives.add(falsePositives), MathContext.DECIMAL128);
    }

    public BigDecimal getRecall() {
        return truePositives.divide(truePositives.add(falseNegatives), MathContext.DECIMAL128);
    }

    /**
     *
     * @return harmonic mean of precision and recall
     */
    BigDecimal getF1Score() {
        BigDecimal zähler = getPrecision().multiply(getRecall());
        BigDecimal nenner = getPrecision().multiply(getRecall());
        return new BigDecimal(2).multiply(zähler.divide(nenner, MathContext.DECIMAL128));
    }
}
