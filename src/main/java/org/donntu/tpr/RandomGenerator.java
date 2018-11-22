package org.donntu.tpr;

import java.util.Random;

public class RandomGenerator {
    private Random r = new Random();

    public double getKcu() {
        return r.nextDouble();
    }

    private double getUniformByRange(double a, double b) {
        return a + (b - a) * getKcu();
    }

    public double getUniformByMathWait(double m, double cko) {
        return getUniformByRange(m - cko, m + cko);
    }

    public double getNormalY(double m, double cko, int n) {
        double y = 0;
        for (int i = 0; i < n; i++) {
            double a = m / n - (Math.sqrt(3) * cko) / n;
            double b = m / n + (Math.sqrt(3) * cko) / n;
            double uniformY = getUniformByRange(a, b);
            y += uniformY;
        }
        return y;
    }

    public double getExpY(double m_Exp) {
        return -m_Exp * Math.log(getKcu());
    }



}
