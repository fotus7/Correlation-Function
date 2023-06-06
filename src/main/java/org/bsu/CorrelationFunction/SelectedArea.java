package org.bsu.CorrelationFunction;

public class SelectedArea {
    int x,y;
    Pixel[][] area;

    public SelectedArea() {
        this.x = 10;
        this.y = 10;
        this.area = new Pixel[10][10];
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                this.area[i][j] = new Pixel();
            }
        }
    }

    public SelectedArea(int x, int y) {
        this.x = x;
        this.y = y;
        this.area = new Pixel[x][y];
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                this.area[i][j] = new Pixel();
            }
        }
    }

    public SelectedArea(int x, int y, double[][][] pic) {
        this.x = x;
        this.y = y;
        this.area = new Pixel[x][y];
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                this.area[i][j] = new Pixel(pic[i][j][0],pic[i][j][1],pic[i][j][2]);
            }
        }
    }

    public Pixel getPixel(int i, int j){
        return area[i][j];
    }

    public void setPixel(int i, int j, int[] RGB){
        area[i][j].setRGB(RGB[0],RGB[1],RGB[2]);
    }

    public double getPixelSum(int i, int j){
        return area[i][j].getRGBSum();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
