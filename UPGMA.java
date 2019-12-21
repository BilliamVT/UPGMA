import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class UPGMA {
    // Final product string
    private String tree = "";

    // Holds sequences
    private ArrayList<Sequence> sequences;

    // Holds distances
    private ArrayList<ArrayList<Double>> distances;

    // Variables for finding smallest distance
    private double smallestDistance;
    private int smallestI;
    private int smallestJ;

    // Store if there are multiple trees
    boolean multipleTrees = false;

    public UPGMA(ArrayList<Sequence> sequences, ArrayList<ArrayList<Double>> distances) {
        this.sequences = sequences;
        this.distances = distances;

    }

    // Find smallest distance and sets the related variables
    public void smallestDistance() {
        // Set values
        this.smallestDistance = Double.MAX_VALUE;
        this.smallestI = -1;
        this.smallestJ = -1;

        // Smallest counter
        int smallestCounter = 0;

        // Find smallest distance
        for (int i = 0; i < distances.size(); i++) {
            for (int j = 0; j < distances.get(i).size(); j++) {
                if (distances.get(i).get(j) < smallestDistance && distances.get(i).get(j) != 0) {
                    smallestDistance = distances.get(i).get(j);
                    smallestI = i;
                    smallestJ = j;
                }
            }
        }

        // Check for multiple smallests
        for (int i = 0; i < distances.size(); i++) {
            for (int j = 0; j < distances.get(i).size(); j++) {
                if (distances.get(i).get(j) == smallestDistance) {
                    smallestCounter++;
                }
            }
        }

        // There is more than one on the counter
        if(smallestCounter > 1){
            multipleTrees = true;
        }

    }

    // Join two sequences when they are determined the smallest distance
    public void joinSequence() {
        // Remove sequence at j and then set sequence at i to be new joined sequence
        sequences.set(smallestI, new Sequence(sequences.get(smallestI).getName() + "" + sequences.get(smallestJ).getName().replaceAll("[S]", ""), null));
        sequences.remove(smallestJ);
    }

    // Updates distance table
    public void updateTable() {
        // Reconstruct row
        ArrayList<Double> row = new ArrayList<>();
        for (int i = 0; i < smallestI; i++) {
            row.add(((double) distances.get(smallestI).get(i) + distances.get(smallestJ).get(i)) / 2);
        }
        distances.get(smallestI).clear();
        distances.get(smallestI).addAll(row);

        // Keep reconstructing
        for (int i = smallestI + 1; i < smallestJ; i++) {
            distances.get(i).set(smallestI, (((double) distances.get(i).get(smallestI) + distances.get(smallestJ).get(i)) / 2));
        }

        // And reconstruct some more
        for (int i = smallestJ + 1; i < distances.size(); i++) {
            distances.get(i).set(smallestI, (((double) distances.get(i).get(smallestI) + distances.get(i).get(smallestJ)) / 2));
            distances.get(i).remove(smallestJ);
        }

        // Redundant row
        distances.remove(smallestJ);


    }

    // UPGMA algorithm
    public void UPGMA() {
        // Changes format
        boolean firstRun = true;

        // Store distance for tree
        double distance = 0;

        // Go until all sequences used
        while (sequences.size() > 1) {
            // Find smallest distance
            smallestDistance();

            // If j is smaller need to swap i and j
            if (smallestJ < smallestI) {
                int temp = smallestJ;
                smallestJ = smallestI;
                smallestI = temp;
            }

            // Special first run format
            if(firstRun) {
                tree = "(" + sequences.get(smallestI).getName() + ":" + (String.format("%.1f", distances.get(smallestJ).get(smallestI) / 2)) + ")" + "(" + sequences.get(smallestJ).getName() + ":" + (String.format("%.1f", distances.get(smallestJ).get(smallestI) / 2)) + ")";
            // Not first run
            } else {
                tree = "(" + sequences.get(smallestI).getName() + ":" + (String.format("%.1f", (distances.get(smallestJ).get(smallestI) / 2 - distance))) + tree + ")" + "(" + sequences.get(smallestJ).getName() + ":" + (String.format("%.1f", distances.get(smallestJ).get(smallestI) / 2 ))+ ")" ;
            }

            // Set distance
            distance = distances.get(smallestJ).get(smallestI) / 2;

            // False after this no matter what
            firstRun = false;

            // Join two sequences
            joinSequence();

            // Update distances
            updateTable();
        }

        /**
         * FILE OUTPUT PART B
         */

        // Try writer
        try{
            // Create writer
            BufferedWriter writer2 = new BufferedWriter(new FileWriter("3.o2"));

            // Write the tree to the file
            writer2.write(sequences.get(0).getName());
            writer2.write(tree);

            // Close writer
            writer2.close();

        }catch (IOException e){
            System.out.printf("IOException error writing to file 3.o2");
        }

        /**
         * END PART B
         */

        /**
         * FILE OUTPUT PART C
         */

        // Try writer
        try{
            // Create writer
            BufferedWriter writer3 = new BufferedWriter(new FileWriter("3.o3"));

            // If multiple trees write yes if not write no
            if (multipleTrees){
                writer3.write("YES");
            }else{
                writer3.write("NO");
            }

            // Close writer
            writer3.close();

        } catch (IOException e){
            System.out.println("IOException error writing to file 3.o3");
        }

        /**
         * END PART C
         */

    }
}
