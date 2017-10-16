package ch.fhnw.dist;

/**
 * @author Hasan Kara <hasan.kara@students.fhnw.ch>
 */
public class Score {

    private double truePositives = 0;
    private double falsePositives = 0;
    private double falseNegatives = 0;

    public void incrementTp() {
        truePositives++;
    }

    public void incrementFp() {
        falsePositives++;
    }

    public void incrementFn() {
        falseNegatives++;
    }


    public double getPrecision() {
        return truePositives / (truePositives + falsePositives);
    }

    public double getRecall() {
        return truePositives / (truePositives + falseNegatives);
    }

    /**
     *
     * @return harmonic mean of precision and recall
     */
    double getF1Score() {
        double zähler = getPrecision() * getRecall();
        double nenner = getPrecision() + getRecall();
        return 2 * (zähler / nenner);
    }
}
