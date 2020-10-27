
public class Main {

    public static void main(String[] args) {

        SOMGenerator som = new SOMGenerator();
        som.readInput();
        som.shuffle();

        double minSimilarity = 10, minDistance = 10;
        double[] a;
        SOMGenerator bestSom = null;
        //run 100 times to find best solution
        for (int iterations = 0; iterations < 100; iterations++) {
            som.initNetwork();
            som.create();
            a = som.evaluate();
            //found better solution
            if (a[0] < minSimilarity && a[1] < minDistance) {
                minSimilarity = a[0];
                minDistance = a[1];
                bestSom = som;
            }
        }
        //Display Solution
        Painter painter = new Painter(bestSom);
        painter.paint();
        som.writeOutput();
    }
}

