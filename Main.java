import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Create file
        File file = new File("3.in");

        // Have an arraylist of all the sequences
        ArrayList<Sequence> sequences = new ArrayList<>();

        // Try reading file if not found tell user
        try {
            // Scanner to read from file
            BufferedReader reader = new BufferedReader(new FileReader(file));

            // Read a line
            String line = reader.readLine();

            // While file still has lines keep reading
            while (line != null) {
                // Name of sequence
                String name = line.substring(1);

                // Get sequence
                String sequence = reader.readLine();

                // Create sequence and add to sequences
                sequences.add(new Sequence(name, sequence));

                // Go to next line
                line = reader.readLine();
            }


        } catch (IOException e) {
            // Tell user file is not found
            System.out.println("Error.");
        }

        // Create matrix to hold distances
        ArrayList<ArrayList<Double>> distanceMatrix = new ArrayList<>();

        // Counter
        double counter = 0;

        // Loop through to find differences in sequences
        for (int i = 0; i < sequences.size(); i++) {
            distanceMatrix.add(new ArrayList<>());
            for (int j = 0; j < i; j++) {
                for (int k = 0; k < sequences.get(i).getSequence().length(); k++) {
                    if (sequences.get(i).getSequence().charAt(k) != sequences.get(j).getSequence().charAt(k)) {
                        counter++;
                    }
                }
                distanceMatrix.get(i).add(counter);
                counter = 0;
            }
        }

        /**
         * FILE OUTPUT PART A
         */

        // Create matrix to hold distances
        ArrayList<ArrayList<Double>> fileMatrix = new ArrayList<>();

        // Counter
        double fileCounter = 0;

        // Loop through to find differences in sequences
        for (int i = 0; i < sequences.size(); i++) {
            fileMatrix.add(new ArrayList<>());
            for (int j = 0; j < sequences.size(); j++) {
                for (int k = 0; k < sequences.get(i).getSequence().length(); k++) {
                    if (sequences.get(i).getSequence().charAt(k) != sequences.get(j).getSequence().charAt(k)) {
                        fileCounter++;
                    }
                }
                fileMatrix.get(i).add(fileCounter);
                fileCounter = 0;
            }
        }

        try{
            // Create writer
            BufferedWriter writer1 = new BufferedWriter(new FileWriter("3.o1"));

            // Write character
            writer1.write("- ");

            // Write sequence names on x axis
            for(int i = 0; i < sequences.size(); i++) {
                writer1.write(sequences.get(i).getName() + " ");
            }

            // Next line
            writer1.write("\n");

            // Write sequence names on y axis and distances
            for (int i = 0; i < fileMatrix.size(); i++) {
                writer1.write(sequences.get(i).getName() + " ");
                for (int j = 0; j < fileMatrix.get(i).size(); j++) {
                    writer1.write(fileMatrix.get(i).get(j).intValue() + " ");
                }
                writer1.write("\n");
            }

            // Close writer
            writer1.close();

        }catch (IOException e){
            System.out.println("IOException error writing to file 3.o1");
        }

        /**
         * END PART A
         */

        // Create UPGMA
        UPGMA upgma = new UPGMA(sequences, distanceMatrix);

        // Run UPGMA
        upgma.UPGMA();

    }

}
