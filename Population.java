import java.util.Arrays;
import java.util.Random;

public class Population {
    int populationSize = 6;
    int numberOfIteration = 4;
    int maxPop = 15, minPop = 0;
    int binarySizeLimit = 4;

    int[] population_array;
    String[] binaryString_array;
    int fitness[];
    double ratio[];
    String pairs_binaryString[] = new String[6];
    int min, secondMin;

    public void initializePopulation() {
        population_array = new int[populationSize];
        binaryString_array = new String[populationSize];
        fitness = new int[populationSize];
        ratio = new double[populationSize];

        Random random = new Random();
        for (int i = 0; i < populationSize; i++) {
            population_array[i] = random.nextInt((maxPop - minPop) + 1) + minPop;
            System.out.print(population_array[i] + " ");
        }
        System.out.println();
    }

    public void startGeneric() {
        int loopCount = 0;
        while (loopCount++ < numberOfIteration) {
            populationToBinaryString();
            double totalFitness = calcFitness();
            calcRatio(totalFitness);
            selectAndMakePairs();
            crossover(); // modifikasi di sini
            mutation();
            for (int i = 0; i < populationSize; i++) {
                System.out.print(population_array[i] + " ");
            }
            System.out.println();
        }
    }

    public void crossover() {
        Random random = new Random();
        int point1 = random.nextInt(binarySizeLimit - 2);
        int point2 = point1 + 2;
        System.out.println("Two-point crossover between bits: " + point1 + " to " + (point2 - 1));

        for (int i = 0; i < 6; i += 2) {
            char[] ch1 = pairs_binaryString[i].toCharArray();
            char[] ch2 = pairs_binaryString[i + 1].toCharArray();

            for (int j = point1; j < point2; j++) {
                char temp = ch1[j];
                ch1[j] = ch2[j];
                ch2[j] = temp;
            }

            pairs_binaryString[i] = new String(ch1);
            pairs_binaryString[i + 1] = new String(ch2);
        }

        System.out.println("after crossover:");
        for (String s : pairs_binaryString) {
            System.out.print(s + " ");
        }
        System.out.println();
    }

    public void mutation() {
        String x1 = "", x2 = "", x3 = "";
        Random random = new Random();
        int i = 0, first = 0, second = 0;

        while (i < 3) {
            int index = random.nextInt(pairs_binaryString.length);
            if (i == 0) {
                x1 = pairs_binaryString[index]; first = index; i++;
            } else if (i == 1 && index != first) {
                x2 = pairs_binaryString[index]; second = index; i++;
            } else if (i == 2 && index != first && index != second) {
                x3 = pairs_binaryString[index]; i++;
            }
        }

        int bit = random.nextInt(binarySizeLimit);
        x1 = mutateBit(x1, bit);
        x2 = mutateBit(x2, bit);
        x3 = mutateBit(x3, bit);

        int[] mutated_array = {toInteger(x1), toInteger(x2), toInteger(x3)};
        Arrays.sort(mutated_array);
        population_array[min] = mutated_array[1];
        population_array[secondMin] = mutated_array[2];
    }

    public String mutateBit(String binary, int bit) {
        char[] ch = binary.toCharArray();
        ch[bit] = ch[bit] == '0' ? '1' : '0';
        return new String(ch);
    }

    public void selectAndMakePairs() {
        Node[] nodeArray = Priority.setPriority(ratio, binaryString_array);
        min = nodeArray[0].index;
        secondMin = nodeArray[1].index;
        int pairIndexCounter = 0;
        pairs_binaryString[pairIndexCounter++] = nodeArray[nodeArray.length - 1].binaryString;

        Random random = new Random();
        while (pairIndexCounter < 6) {
            int index = random.nextInt(populationSize);
            if (index != min && index != secondMin && !Arrays.asList(pairs_binaryString).contains(binaryString_array[index])) {
                pairs_binaryString[pairIndexCounter++] = binaryString_array[index];
            }
        }
    }

    public void calcRatio(double totalFitness) {
        for (int i = 0; i < populationSize; i++) {
            ratio[i] = (fitness[i] * 100) / totalFitness;
        }
    }

    public int calcFitness() {
        int totalFitness = 0;
        for (int i = 0; i < populationSize; i++) {
            int x = population_array[i];
            fitness[i] = (15 * x) - (x * x);
            totalFitness += fitness[i];
        }
        return totalFitness;
    }

    public void populationToBinaryString() {
        for (int i = 0; i < populationSize; i++) {
            binaryString_array[i] = getBinaryString(population_array[i]);
        }
    }

    public String getBinaryString(int val) {
        String binaryString = Integer.toBinaryString(val);
        while (binaryString.length() < binarySizeLimit) {
            binaryString = "0" + binaryString;
        }
        return binaryString;
    }

    public int toInteger(String binaryString) {
        return Integer.parseInt(binaryString, 2);
    }
}
