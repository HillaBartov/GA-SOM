import java.util.Random;

public class Network {
    public double map[][][];

    public Network() {
        this.map = new double[100][6][6];
    }

    //init network with random values.
    public void initNetwork() {
        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                for (int k = 0; k < 100; k++) {
                    map[k][i][j] = rand.nextDouble();
                }
            }
        }
    }

    //find Best matching cell for input example.
    public double[] findSimilar(double[] neuron) {
        double maxFit = Double.MAX_VALUE, oldI = -1, oldJ = -1, sum = 0;
        double[] similarInfo = new double[4];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                for (int k = 0; k < 100; k++) {
                    sum += Math.pow((neuron[k] - this.map[k][i][j]), 2);
                }
                sum = Math.sqrt(sum);
                if (sum < maxFit) {
                    //for the second best
                    oldI = similarInfo[0];
                    oldJ = similarInfo[1];
                    maxFit = sum;
                    similarInfo[0] = i;
                    similarInfo[1] = j;
                    similarInfo[2] = maxFit;
                }
                sum = 0;
            }
        }
        //the distance between the best fit to the second best one
        int distance = Integer.max(Math.abs(((int) similarInfo[0] - (int) oldI)), Math.abs(((int) similarInfo[1] - (int) oldJ)));
        similarInfo[3] = distance;
        return similarInfo;
    }

    //find most similar cell for given another cell.
    public double RMSToColor(int i1, int j1, int i2, int j2) {
        double sum = 0;
        for (int k = 0; k < 100; k++) {
            sum += Math.pow((this.map[k][i1][j1] - this.map[k][i2][j2]), 2);
        }
        sum = Math.sqrt(sum);
        return sum;
    }

    //update network
    public void update(double[] neuron, double[] index, int cycle) {
        double distance;
        double learningConst = 0.5 - (cycle * 0.0008);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                //get neighborhood radius
                distance = Integer.max(Math.abs(((int) index[0] - i)), Math.abs(((int) index[1] - j)));
                //change the representative in the index by 30%,in the first cycle by 20%, second by 10% and further don't change
                double neighborhood = 0.6;

                if (distance > 2) {
                    continue;
                }
                if (distance == 1) {
                    neighborhood = 0.4;
                }
                if (distance == 2) {
                    neighborhood = 0.2;
                }

                for (int k = 0; k < 100; k++) {
                    this.map[k][i][j] += (learningConst * neighborhood * (neuron[k] - this.map[k][i][j]));
                }
            }
        }
    }
}
