package client;

import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.Integer;
import compute.Compute;

public class TCPClient {
    public static void main(String args[]) {
        Socket s = null;
        try {
            int serverPort = 7896;

            // Connecting to the server socket
            s = new Socket("127.0.0.1", serverPort);

            // Creating client output stream
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.flush();

            // Creating client input stream
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());

            // If the client requests "help"
            if (args[0].toLowerCase().equals("help")) {
                System.out.println("\nThe default format of inputs: <service name> <param1> <param2> ... <paramN>");
                System.out.println("The available services and their parameters are: \n");
                System.out.println("    Sort <type> <number list>\n");
                System.out.println("        <type>: {'Bubblesort', 'SelectionSort', 'InsertionSort'}\n");
                System.out.println("        Example input: Sort bubblesort 5 3 6 2 0 3 9 63 21 65\n");
                System.out.println("    MagicSquare <n>\n");
                System.out.println("        <n>: dimension of the generated matrix, n >= 3\n");
                System.out.println("        Example input: MagicSquare 11\n");
                System.out.println("    Fibonacci <n>\n");
                System.out.println("        <n>: the nth Fibonacci number will be returned, n >= 0\n");
                System.out.println("        Example input: Fibonacci 15\n");
                System.out.println("    Encrypt <string>\n");
                System.out.println("        <string>: the string to be encrypted\n");
                System.out.println("        Example input: Encrypt Hello, World!\n");
            } else {
                // Creating string to be sent to the server
                StringBuilder outString = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    if (i != 0)
                        outString.append(" ");
                    outString.append(args[i]);
                }
                // Sending the list of arguments
                out.writeObject(outString.toString());

                // Handling the return according to the requested service
                if (args[0].toLowerCase().equals("sort")) {
                    int[] sortedList = new int[in.readInt()];
                    for (int i = 0; i < sortedList.length; i++) {
                        sortedList[i] = in.readInt();
                    }
                    // Displaying the sorted list
                    System.out.print("\nResponse received from the server for the Sort service: ");
                    System.out.print("Sorted list: ");
                    for (int j = 0; j < sortedList.length; j++) {
                        System.out.print(sortedList[j] + " ");
                    }
                    System.out.println("\n");
                } else if (args[0].toLowerCase().equals("magicsquare")) {
                    int n = in.readInt();
                    int[][] magicSquare = new int[n][n];
                    // Filling the magic square with the data
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            magicSquare[i][j] = in.readInt();
                        }
                    }
                    // Displaying the magic square
                    System.out.println("\nResponse received from the server for the Magic Square service: ");
                    System.out.println("Magic Square for N = " + n + ":");
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            System.out.format("%5d", magicSquare[i][j]);
                        }
                        System.out.println();
                    }
                    System.out.println("Sum of each row/column/diagonal = " + n * (n * n + 1) / 2 + "\n");
                } else if (args[0].toLowerCase().equals("fibonacci")) {
                    int fibonacciNumber = in.readInt();
                    // Displaying the Fibonacci number
                    System.out.println("\nResponse received from the server for the Fibonacci service: ");
                    System.out.println("Fibonacci number for n = " + args[1] + ": " + fibonacciNumber);
                    System.out.println("\n");
                } else if (args[0].toLowerCase().equals("encrypt")) {
                    // Construct the string to be sent to the server
                    StringBuilder inputString = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        inputString.append(args[i]).append(" ");
                    }
                    String input = inputString.toString().trim();

                    // Send the input string to the server
                    out.writeObject(input);

                    // Receive the encrypted string from the server
                    String encryptedString = (String) in.readObject();

                    // Print the encrypted string
                    System.out.println("\nResponse received from the server for the Encrypt service:");
                    System.out.println("The encrypted string: " + encryptedString + "\n");
                } else {
                    throw new IllegalArgumentException("\nCheck if you entered an existing service.\n");
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    System.out.println("Close: " + e.getMessage());
                }
            }
        }
    }
}
