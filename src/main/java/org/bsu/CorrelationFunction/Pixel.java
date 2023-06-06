package org.bsu.CorrelationFunction;

import java.util.Objects;

public class Pixel {
    double R,G,B;
    int x,y;

    void setRGB(int R, int G, int B){
        setR(R);
        setG(G);
        setB(B);
    }

    public void setR(int r) {
        R = r;
    }

    public void setG(int g) {
        G = g;
    }

    public void setB(int b) {
        B = b;
    }

    public double[] getRGB(){
        return new double[] {getR(), getG(), getB()};
    }

    public double getR() {
        return R;
    }

    public double getG() {
        return G;
    }

    public double getB() {
        return B;
    }

    public double getRGBSum(){
        return R+G+B;
    }

    public Pixel() {
        R = 0;
        G = 0;
        B = 0;
    }

    public Pixel(double r, double g, double b) {
        R = r;
        G = g;
        B = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pixel pixel = (Pixel) o;
        return R == pixel.R && G == pixel.G && B == pixel.B;
    }

    @Override
    public int hashCode() {
        return Objects.hash(R, G, B);
    }
}
