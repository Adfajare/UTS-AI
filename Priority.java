public class Priority {

    public static Node[] setPriority(double[] ratio_array, String[] binaryString_array) {
        Node[] nodeArray = new Node[ratio_array.length];
        for (int i = 0; i < ratio_array.length; i++) {
            nodeArray[i] = new Node(binaryString_array[i], ratio_array[i], i);
        }

        // Bubble sort berdasarkan nilai rasio
        for (int i = 0; i < nodeArray.length; i++) {
            for (int j = nodeArray.length - 1; j > i; j--) {
                if (nodeArray[j].ratioValue < nodeArray[j - 1].ratioValue) {
                    Node temp = nodeArray[j];
                    nodeArray[j] = nodeArray[j - 1];
                    nodeArray[j - 1] = temp;
                }
            }
        }
        return nodeArray;
    }
}

class Node {
    String binaryString;
    double ratioValue;
    int index;

    public Node(String binaryString, double ratioValue, int index) {
        this.binaryString = binaryString;
        this.ratioValue = ratioValue;
        this.index = index;
    }
}
