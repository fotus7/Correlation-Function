package org.bsu.CorrelationFunction;

import static java.lang.Math.*;

public class Function {

    public static double[][] count(double[][][] area1, double[][][] area2) {
        int dx, dy, I2sizeX, I2sizeY;
        I2sizeX = area2.length;
        I2sizeY = area2[0].length;
        dx = area1.length;
        dy = area1[0].length;

        SelectedArea I1 = new SelectedArea(dx, dy, area1);
        SelectedArea I2 = new SelectedArea(I2sizeX, I2sizeY, area2);
        double[][] R = new double[(I2sizeX - dx) * (I2sizeY - dy)][3];
        int i = 0;
        for (int du = 0; du < I2sizeX - dx; du++) {
            for (int dv = 0; dv < I2sizeY - dy; dv++) {
                R[i] = R_correspondence(du, dv, dx, dy, I1, I2);
                if (R[i][0] != 0) {
                    System.out.print(R[i][0] + "(смещение: du=" + R[i][1] + " dv=" + R[i][2] + ")   ");
                    System.out.println('\n');
                    System.out.println("-----------------------------");
                }
                i++;
            }
        }
        double[] maxR = maxR(R);
        System.out.println("Максимальное R: " + maxR[0] + " (смещение: du=" + maxR[1] + "  dv=" + maxR[2] + ")");
        return R;
    }

    public static double[][] count(int x, int y, double[][][] area2) {
        int dx, dy, I2sizeX, I2sizeY;
        I2sizeX = area2.length;
        I2sizeY = area2[0].length;
        dx = x;
        dy = y;
        
        SelectedArea I2 = new SelectedArea(I2sizeX, I2sizeY, area2);
        double[][] R = new double[(I2sizeX - x) * (I2sizeY - y)][3];
        int i = 0;
        for (int du = 0; du+1 <= I2sizeX - dx; du++) {
            for (int dv = 0; dv+1 <= I2sizeY - dy; dv++) {
                R[i] = R_difference(du, dv, dx, dy, I2);
                //if (R[i][0] != 0) {
                    System.out.print(R[i][0] + "(смещение: du=" + R[i][1] + " dv=" + R[i][2] + ")   ");
                    System.out.println('\n');
                    System.out.println("-----------------------------");

                i++;
            }
        }
        double[] minR = minR(R);
        System.out.println("Минимальное R: " + minR[0] + " (смещение: du=" + minR[1] + "  dv=" + minR[2] + ")");
        return R;
    }

    private static double ILineN(int du, int dv, int dx, int dy, SelectedArea In) {
        double sumIn = 0;
        for (int i = du; i < dx + du; i++) {
            for (int j = dv; j < dy + dv; j++) {
                sumIn += In.getPixelSum(i, j);
            }
        }
        return sumIn / (dx * dy);
    }

    private static double[] R_correspondence(int du, int dv, int dx, int dy, SelectedArea I1, SelectedArea I2) {
        double numerator = 0;
        double denumerator = 0;
        double sum1 = 0, sum2 = 0;
        double ILine1 = ILineN(0, 0, dx, dy, I1);
        double ILine2 = ILineN(du, dv, dx, dy, I2);
        double[] R = new double[3];
        for (int i = 0; i < dx; i++) {
            for (int j = 0; j < dy; j++) {
                numerator += ((I1.getPixelSum(i, j) - ILine1) * (I2.getPixelSum(i + du, j + dv) - ILine2));
                sum1 += pow((I1.getPixelSum(i, j) - ILine1), 2);
                sum2 += pow((I2.getPixelSum(i + du, j + dv) - ILine2), 2);
            }
        }
        denumerator = sqrt(sum1 * sum2);
        R[0] = Math.abs(numerator / denumerator);
        R[1] = du;
        R[2] = dv;
        if (Double.isNaN(R[0])) R[0] = 0;
        return R;
    }

    private static double[] R_difference(int du, int dv, int dx, int dy, SelectedArea I2) {
        double numerator = 0;
        double denumerator = 0;
        double sum1 = 0, sum2 = 0;
        double ILine1 = ILineN(du, dv, dx, dy, I2);
        double ILine2 = ILineN(du + 1, dv + 1, dx, dy, I2);
        double[] R = new double[3];
        for (int i = du; i < dx + du; i++) {
            for (int j = dv; j < dy + dv; j++) {
                numerator += ((I2.getPixelSum(i, j) - ILine1) * (I2.getPixelSum(i + 1, j + 1) - ILine2));
                sum1 += pow((I2.getPixelSum(i, j) - ILine1), 2);
                sum2 += pow((I2.getPixelSum(i + 1, j + 1) - ILine2), 2);
            }
        }
        denumerator = sqrt(sum1 * sum2);
        R[0] =  1 - Math.abs(numerator / denumerator);
        R[1] = du;
        R[2] = dv;
        if (Double.isNaN(R[0])) R[0] = 1;
        return R;
    }


    public static double[] maxR(double[][] R) {
        double[] maxR = new double[3];
        maxR[0] = R[0][0];
        maxR[1] = R[0][1];
        maxR[2] = R[0][2];
        for (int i = 0; i < R.length; i++) {
            if (R[i][0] > maxR[0]) {
                maxR[0] = R[i][0];
                maxR[1] = R[i][1];
                maxR[2] = R[i][2];
            }
        }
        return maxR;
    }

    public static double[] minR(double[][] R) {
        double[] minR = new double[3];
        minR[0] = R[0][0];
        minR[1] = R[0][1];
        minR[2] = R[0][2];
        for (int i = 0; i < R.length; i++) {
            if (R[i][0] < minR[0]) {
                minR[0] = R[i][0];
                minR[1] = R[i][1];
                minR[2] = R[i][2];
            }
        }
        return minR;
    }
}


