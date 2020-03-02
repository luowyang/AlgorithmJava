package pers.luo.algs;

import edu.princeton.cs.algs4.DoublingRatio;
import edu.princeton.cs.algs4.Picture;

import java.awt.*;

public class Seam {
    public static Picture compress(Picture image) {
        int m = image.height();
        int n = image.width();
        double[][] score = score(image);
        double[][] cost = new double[m][n];
        int[][] prev = new int[m][n];
        for (int j = 0; j < n; j++) {
            cost[0][j] = score[0][j];
            prev[0][j] = -1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 0; j < n; j++) {
                double c = cost[i-1][j];
                prev[i][j] = j;
                if (j > 0 && cost[i-1][j-1] < c) {
                    c = cost[i-1][j-1];
                    prev[i][j] = j-1;
                }
                if (j < n-1 && cost[i-1][j+1] < c) {
                    c = cost[i-1][j+1];
                    prev[i][j] = j+1;
                }
                cost[i][j] = score[i][j] + c;
            }
        }
        int[] pos = new int[m];
        int j = 0;
        double min = cost[m-1][0];
        for (int k = 1; k < n; k++) {
            if (cost[m-1][k] < min) {
                min = cost[m-1][k];
                j = k;
            }
        }
        for (int i = m-1; i >= 0; i--) {
            pos[i] = j;
            j = prev[i][j];
        }
        Picture pic = new Picture(n-1, m);
        for (int i = 0; i < m; i++) {
            int k = 0;
            for (j = 0; j < n; j++)
                if (j != pos[i]) {
                    pic.set(k, i, image.get(j, i));
                    k++;
                } else {
                    image.set(j, i, Color.GREEN);
                }
        }
        return pic;
    }

    private static double[][] score(Picture image) {
        int m = image.height();
        int n = image.width();
        double[][] score = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                score[i][j] = score(image, i, j);
        return score;
    }

    private static double score(Picture image, int row, int col) {
        double score = 0.0;
        Color cur = image.get(col, row);
        if (row > 0) score += similarity(image.get(col,row-1), cur);
        if (row < image.height()-1) score += similarity(image.get(col, row+1), cur);
        if (col > 0) score += similarity(image.get(col-1, row), cur);
        if (col < image.width()-1) score += similarity(image.get(col+1, row), cur);
        return score;
    }

    private static double similarity(Color x, Color y) {
        double sim = 0.0;
        sim += Math.abs(x.getRed() - y.getRed());
        sim += Math.abs(x.getGreen() - y.getGreen());
        sim += Math.abs(x.getBlue() - y.getBlue());
        return sim;
    }

    public static void main(String[] args) {
        Picture image = new Picture(args[0]);
        compress(image).show();
        image.show();
    }
}
