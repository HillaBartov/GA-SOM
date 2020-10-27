import biuoop.DrawSurface;
import biuoop.GUI;

import java.awt.*;

public class Painter {
    private SOMGenerator som;
    private int[][] representatives;

    public Painter(SOMGenerator som) {
        this.som = som;
        this.representatives = init();
    }

    public void paint() {
        GUI gui = new GUI("SOM", 480, 480);
        DrawSurface d = gui.getDrawSurface();
        represent();

        //Draw Grid
        for (int i = 0; i < 6; i++) {
            d.drawLine(0, i * 80, 500, i * 80);//row
            d.drawLine(i * 80, 0, i * 80, 500);//col
        }

        float[] a = new float[3];
        float[] b;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                //representative cell - get matching color
                if (representatives[i][j] != -1) {
                    d.setColor(getColor(representatives[i][j]));
                    d.fillRectangle(i * 80, j * 80, 80, 80);
                    //Not representative - get color by mixing 2 BMS representatives
                } else {
                    Color second = getColor(findSecondSimilar(i, j));
                    Color first = getColor(findSimilar(i, j));
                    //get 80% of the color from BMS, 20% from 2nd BMS.
                    int red = (int) (first.getRed() * 0.80 + second.getRed() * 0.20);
                    int green = (int) (first.getGreen() * 0.80 + second.getGreen() * 0.20);
                    int blue = (int) (first.getBlue() * 0.80 + second.getBlue() * 0.20);
                    b = Color.RGBtoHSB(red, green, blue, a);
                    d.setColor(Color.getHSBColor(b[0], b[1], b[2]));
                    d.fillRectangle(i * 80, j * 80, 80, 80);
                }
            }
        }
        gui.show(d);
    }

    //return color for each representative by input number
    public Color getColor(int num) {
        float[] a = new float[3];
        float[] b = null;
        switch (num) {
            case 0:
                b = Color.RGBtoHSB(0, 0, 255, a);
                break;
            case 1:
                b = Color.RGBtoHSB(0, 255, 0, a);
                break;
            case 2:
                b = Color.RGBtoHSB(255, 0, 0, a);
                break;
            case 3:
                b = Color.RGBtoHSB(0, 0, 0, a);
                break;
            case 4:
                b = Color.RGBtoHSB(255, 0, 255, a);
                break;
            case 5:
                b = Color.RGBtoHSB(255, 255, 0, a);
                break;
            case 6:
                b = Color.RGBtoHSB(0, 255, 255, a);
                break;
            case 7:
                b = Color.RGBtoHSB(255, 255, 255, a);
                break;
            case 8:
                b = Color.RGBtoHSB(128, 128, 128, a);
                break;
            case 9:
                b = Color.RGBtoHSB(100, 150, 190, a);
                break;
        }
        return (Color.getHSBColor(b[0], b[1], b[2]));
    }

    //create representatives matrix
    public void represent() {
        int counter = 0;
        int[][] averages = init();
        int number = 0;
        for (int i = 0; i < 100; i++) {
            averages[(int) this.som.output.get(i)[0]][(int) this.som.output.get(i)[1]]++;

            if (counter == 9) {
                counter = 0;
                int[] evaled = findRepresentative(averages);
                representatives[evaled[0]][evaled[1]] = number;
                number++;
                averages = init();
            } else {
                counter++;
            }

        }
    }

    //init Matrix with '-1'
    public int[][] init() {
        int[][] c = new int[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                c[i][j] = -1;
            }
        }
        return c;
    }

    //find the representative which appears the most
    public int[] findRepresentative(int[][] c) {
        int max = 0;
        int[] ret = new int[2];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (c[i][j] > max) {
                    max = c[i][j];
                    ret[0] = i;
                    ret[1] = j;
                }
            }
        }
        return ret;
    }

    //find similar representative
    public int findSimilar(int i, int j) {
        double min = 100;
        int index = -1;
        for (int k = 0; k < 6; k++) {
            for (int l = 0; l < 6; l++) {
                if (representatives[k][l] != -1) {
                    if (min > this.som.network.RMSToColor(i, j, k, l)) {
                        min = this.som.network.RMSToColor(i, j, k, l);
                        index = representatives[k][l];
                    }
                }
            }
        }
        return index;
    }

    //find second similar representative
    public int findSecondSimilar(int i, int j) {
        double min = 100;
        int secondIndex = -1;
        int firstindex = findSimilar(i, j);
        int firstI = -1, firstJ = -1;
        for (int k = 0; k < 6; k++) {
            for (int l = 0; l < 6; l++) {
                if (representatives[k][l] == firstindex) {
                    firstI = k;
                    firstJ = l;
                }

            }
        }

        for (int k = 0; k < 6; k++) {
            for (int l = 0; l < 6; l++) {
                if (k != firstI && l != firstJ && representatives[k][l] != -1) {
                    if (min > this.som.network.RMSToColor(i, j, k, l)) {
                        min = this.som.network.RMSToColor(i, j, k, l);
                        secondIndex = representatives[k][l];
                    }

                }
            }
        }
        return secondIndex;
    }
}
