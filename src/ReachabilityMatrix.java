// Name: Brendan Woods
// Class: CS 3305/01
// Term: Fall 2022
// Instructor: Dr. Haddad
// Assignment: 9
// IDE Name: IntelliJ

import java.util.Scanner;

public class ReachabilityMatrix {

    static int inputSize, sum = 0;
    static int pathLen1, pathLen2, pathLen3, pathLen4, pathLen5 = 0;
    static int cycleLen1, cycleLen2, cycleLen3, cycleLen4, cycleLen5 = 0;
    static int[][] A1 = new int[5][5];
    static int[][] A2 = new int[5][5];
    static int[][] A3 = new int[5][5];
    static int[][] A4 = new int[5][5];
    static int[][] A5 = new int[5][5];
    static int[][] reachability = new int[5][5];

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        int option;
        boolean firstRun = true;

        // Make user press 1 first
        while (firstRun) {

            printMenu();
            int choice = input.nextInt();

            if (choice == 1) {
                readInputMatrix();
                firstRun = false;
            } else if (choice == 3) {
                System.exit(-1);
            } else {
                System.out.println("\nError: Must choose option #1 before proceeding\n");
            }
        }

        while (true) {

            printMenu();
            option = input.nextInt();

            switch (option) {
                case 1 -> // Enter Graph Data
                        readInputMatrix();
                case 2 -> // Print outputs
                        option2();
                case 3 -> { // Exit
                    input.close();
                    System.exit(255);
                }
                default -> {
                    System.out.println("Invalid option\n");
                }
            }
        }
    }

    public static void option2() {
        System.out.print("Input Matrix:");
        printMatrix(A1);

        reachabilityMatrix();
        allPaths();
        allCycles();
        inDegree();
        outDegree();
        selfLoops();

        switch (inputSize) {

            // Size 1
            case 1 -> {
                System.out.println("Total number of cycles of length 1 edges: " + cycleLen1);
                System.out.println("Total number of paths of length 1 edge: " + pathLen1);
                System.out.println();
            }

            // Size 2
            case 2 -> {
                System.out.println("Total number of cycles of length 2 edges: " + cycleLen2);
                System.out.println("Total number of paths of length 1 edge: " + pathLen1);
                System.out.println("Total number of paths of length 2 edges: " + pathLen2);
                System.out.println("Total number of paths of length 1 to 2 edges: " +
                        (pathLen1 + pathLen2));
                System.out.println("Total number of cycles of length 1 to 2 edges: " +
                        (cycleLen1 + cycleLen2));
                System.out.println();
            }

            // Size 3
            case 3 -> {
                System.out.println("Total number of cycles of length 3 edges: " + cycleLen3);
                System.out.println("Total number of paths of length 1 edge: " + pathLen1);
                System.out.println("Total number of paths of length 3 edges: " + pathLen3);
                System.out.println("Total number of paths of length 1 to 3 edges: " +
                        (pathLen1 + pathLen2 + pathLen3));
                System.out.println("Total number of cycles of length 1 to 3 edges: " +
                        (cycleLen1 + cycleLen2 + cycleLen3));
                System.out.println();
            }

            // Size 4
            case 4 -> {
                System.out.println("Total number of cycles of length 4 edges: " + cycleLen4);
                System.out.println("Total number of paths of length 1 edge: " + pathLen1);
                System.out.println("Total number of paths of length 4 edges: " + pathLen4);
                System.out.println("Total number of paths of length 1 to 4 edges: " +
                        (pathLen1 + pathLen2 + pathLen3 + pathLen4));
                System.out.println("Total number of cycles of length 1 to 4 edges: " +
                        (cycleLen1 + cycleLen2 + cycleLen3 + cycleLen4));
                System.out.println();
            }

            // Size 5
            case 5 -> {
                System.out.println("Total number of cycles of length 5 edges: " + cycleLen5);
                System.out.println("Total number of paths of length 1 edge: " + pathLen1);
                System.out.println("Total number of paths of length 5 edges: " + pathLen5);
                System.out.println("Total number of paths of length 1 to 5 edges: " +
                        (pathLen1 + pathLen2 + pathLen3 + pathLen4 + pathLen5));
                System.out.println("Total number of cycles of length 1 to 5 edges: " +
                        (cycleLen1 + cycleLen2 + cycleLen3 + cycleLen4 + cycleLen5));
                System.out.println();
            }
        }

        // Reset values will give the wrong value otherwise
        zeroMatrix(A1);
        zeroMatrix(A2);
        zeroMatrix(A3);
        zeroMatrix(A4);
        zeroMatrix(A5);
        zeroMatrix(reachability);

        cycleLen1 = 0;
        cycleLen2 = 0;
        cycleLen3 = 0;
        cycleLen4 = 0;
        cycleLen5 = 0;

        pathLen1 = 0;
        pathLen2 = 0;
        pathLen3 = 0;
        pathLen4 = 0;
        pathLen5 = 0;
    }

    // Print the main menu
    public static void printMenu() {
        System.out.println("------MAIN MENU------");
        System.out.println("1. Enter graph data");
        System.out.println("2. Print outputs");
        System.out.println("3. Exit program");

        System.out.print("\nEnter option number: ");
    }

    // Print the matrix
    public static void printMatrix(int[][] m) {

        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < inputSize; j++) {

                System.out.print(m[i][j] + "\t");
            }
            System.out.println();
        }

        System.out.println();
    }

    // Read in the matrix from user
    public static void readInputMatrix() {

        System.out.print("Enter Matrix size (1-5): ");
        inputSize = input.nextInt();

        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                System.out.print("Enter A" + inputSize + "[" + i + ", " + j + "]: ");
                A1[i][j] = input.nextInt();
            }
        }

        System.out.println();
    }

    // calculate in degree
    public static void inDegree() {

        System.out.println("In-degrees: ");
        for (int i = 0; i < inputSize; i++) {
            System.out.print("Node " + (i + 1) + " in-degree is ");
            sum = 0;
            for (int j = 0; j < inputSize; j++)
                sum += A1[j][i];
            System.out.println(sum);
        }
        System.out.println();
    }

    // calculate out degree
    public static void outDegree() {

        System.out.println("Out-degrees: ");

        for (int i = 0; i < inputSize; i++) {
            System.out.print("Node " + (i + 1) + " out-degree is ");

            sum = 0;

            for (int j = 0; j < inputSize; j++)
                sum += A1[i][j];

            System.out.println(sum);
        }

        System.out.println();
    }

    // calculate selfLoops
    public static void selfLoops() {
        sum = 0;

        System.out.print("Total number of self-loops: ");

        for (int i = 0; i < inputSize; i++) {
            sum += A1[i][i];
        }

        System.out.println(sum);
    }

    // Calculate the reachability matrix
    public static void reachabilityMatrix() {

        // A2
        for (int i = 0; i < inputSize; ++i)
            for (int j = 0; j < inputSize; ++j)
                for (int k = 0; k < inputSize; ++k)
                {
                    A2[i][j] += A1[i][k] * A1[k][j];
                }

        // A3
        for (int i = 0; i < inputSize; ++i)
            for (int j = 0; j < inputSize; ++j)
                for (int k = 0; k < inputSize; ++k)
                {
                    A3[i][j] += A2[i][k] * A1[k][j];
                }

        // A4
        for (int i = 0; i < inputSize; ++i)
            for (int j = 0; j < inputSize; ++j)
                for (int k = 0; k < inputSize; ++k)
                {
                    A4[i][j] += A3[i][k] * A1[k][j];
                }

        // A5
        for (int i = 0; i < inputSize; ++i)
            for (int j = 0; j < inputSize; ++j)
                for (int k = 0; k < inputSize; ++k)
                {
                    A5[i][j] += A4[i][k] * A1[k][j];
                }

        switch (inputSize) {
            case 1 -> {
                reachability[0][0] = A1[0][0];
            }
            case 2 -> {

                for (int i = 0; i < inputSize; i++) {
                    for (int j = 0; j < inputSize; j++) {
                        reachability[i][j] = A1[i][j] + A2[i][j];
                    }
                }
            }
            case 3 -> {

                for (int i = 0; i < inputSize; i++) {
                    for (int j = 0; j < inputSize; j++) {
                        reachability[i][j] = A1[i][j] + A2[i][j] + A3[i][j];
                    }
                }
            }
            case 4 -> {
                for (int i = 0; i < inputSize; i++) {
                    for (int j = 0; j < inputSize; j++) {
                        reachability[i][j] = A1[i][j] + A2[i][j] + A3[i][j] + A4[i][j];
                    }
                }
            }
            case 5 -> {
                for (int i = 0; i < inputSize; i++) {
                    for (int j = 0; j < inputSize; j++) {
                        reachability[i][j] = A1[i][j] + A2[i][j] + A3[i][j] + A4[i][j] + A5[i][j];
                    }
                }
            }
            default -> {
            }
        }

        System.out.println("Reachability Matrix:");
        printMatrix(reachability);
    }

    // Calculate all paths
    public static void allPaths() {

        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < inputSize; j++) {

                pathLen1 += A1[i][j];
                pathLen2 += A2[i][j];
                pathLen3 += A3[i][j];
                pathLen4 += A4[i][j];
                pathLen5 += A5[i][j];
            }
        }
    }

    // Calculate all cycles
    public static void allCycles() {

        for (int i = 0; i < inputSize; i++) {

            cycleLen1 += A1[i][i];
            cycleLen2 += A2[i][i];
            cycleLen3 += A3[i][i];
            cycleLen4 += A4[i][i];
            cycleLen5 += A5[i][i];
        }
    }

    // reset the matrix to all zeros so it reruns correctly
    public static void zeroMatrix(int[][] matrix) {

        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                matrix[i][j] = 0;
            }
        }
    }
}
