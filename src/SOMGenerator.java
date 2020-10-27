import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.System.exit;

public class SOMGenerator {
    public Network network;
    private double[][] input;
    private double[][] shuffledInput;
    public ArrayList<double[]> output;

    public SOMGenerator() {
        this.network = new Network();
        this.input = new double[100][100];
        this.shuffledInput = new double[100][100];
        this.output = new ArrayList<>();
    }

    //create the som network by finding representative cells and updating them and their neighbors
    public void create() {
        for (int i = 0; i < 500; i++) {
            this.network.update(this.shuffledInput[i % 100], this.network.findSimilar(this.shuffledInput[i % 100]), i);
        }
    }

    //evaluate the network by the average of the quantization error, and the topological error
    public double[] evaluate() {
        double similarity = 0, distance = 0;
        double[] similarsInfo;
        this.output = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            similarsInfo = this.network.findSimilar(this.input[i]);
            //***************maybe here put the drawing***************
            output.add(new double[]{similarsInfo[0], similarsInfo[1]});
            similarity += similarsInfo[2];
            distance += similarsInfo[3];
        }
        double[] evaluation = {(similarity / 100), (distance / 100)};
        return evaluation;
    }

    //randomized the network cells
    public void initNetwork() {
        this.network.initNetwork();
    }

    //shuffle the network to randomize example choice
    public void shuffle() {
        double[] temp;
        Random rand = new Random();
        //100 shuffle iterations
        for (int i = 0; i < 100; i++) {
            //100 bits of each input example
            int replace = rand.nextInt(100);
            temp = shuffledInput[i];
            shuffledInput[i] = shuffledInput[replace];
            shuffledInput[replace] = temp;
        }
    }

    public void readInput() {

        FileReader inputStream;
        try {
            inputStream = new FileReader("data.txt");
            char c;
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    c = (char) inputStream.read();
                    if (c != '\r' && c != '\n') {
                        // String s = String.valueOf(c);
                        this.input[i][j] = Double.parseDouble(String.valueOf(c));
                    } else {
                        j--;
                    }
                }
            }
            inputStream.close();
            this.shuffledInput = this.input.clone();

            if (shuffledInput == input) {
                int a = 0;
            }
        } catch (Exception e) {
            System.out.println("Error reading input file");
            exit(1);
        }
    }

    //write representative coordinate for each example
    public void writeOutput() {
        try {
            File myObj = new File("output.txt");
            if (myObj.createNewFile()) {
                System.out.println("Output file created successfully");
            }
        } catch (Exception e) {
            System.out.println("An error occurred");
        }
        try {
            FileWriter myWriter = new FileWriter("output.txt");
            for (int i = 0; i < 100; i++) {
                myWriter.write((i + 1) + ": " + "(" + (int) output.get(i)[0] + "," + (int) output.get(i)[1] + ")\n");
            }
            myWriter.close();
            System.out.println("Output file successfully generated.");
        } catch (Exception e) {
            System.out.println("An error occurred.");
        }
    }
}
