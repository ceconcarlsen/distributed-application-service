package client;

import java.net.*;
import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.lang.Integer;
import compute.Compute;
import client.Encrypt;

public class TCPServer {
    public static void main(String args[]) {
        try {
            // Conectar com o cliente
            int serverPort = 7896;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while (true) {
                Socket clientSocket = listenSocket.accept();
                Connection c = new Connection(clientSocket);
            }
        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        }
    }
}

class Connection extends Thread {
    public static boolean debug = false;
    // Variáveis da conexão cliente - servidor
    ObjectInputStream in;
    ObjectOutputStream out;
    Socket clientSocket;

    // Variáveis da conexão executor - servidor
    String name;
    Registry registry;
    Compute comp;

    public Connection(Socket aClientSocket) {
        try {
            // Preparando a conexão servidor-executor
            name = "Compute";
            registry = LocateRegistry.getRegistry("localhost", 1099);
            comp = (Compute) registry.lookup(name);

            System.out.println("\nConexão servidor-executor estabelecida com sucesso...\n");
            // Criando input e output streams
            clientSocket = aClientSocket;
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();

            System.out.println("\nConexão cliente-servidor estabelecida com sucesso...\n");
            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        } catch (Exception e) {
            System.err.println("\nErro ao executar a conexão servidor-executor:");
            e.printStackTrace();
        }
    }

    public void run() {
        // Security da conexão server-executor
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            System.out.println("\nIniciando leitura dos dados enviados pelo cliente\n");
            // Pegando a string com as entradas do cliente: nomeServiço arg1 arg2 arg3 arg4...
            String data = (String) in.readObject();
            System.out.println("\nData: " + data + "\n");
            // Separando entradas do cliente
            String[] dataSeparated = data.split(" ");
            // Tendo agora as entradas do cliente, podemos escolher qual o tipo de serviço ele deseja e executar de acordo
            if (dataSeparated[0].toLowerCase().equals("magicsquare")) {
                // Um "Magic Square" é uma matriz de dimensão N >= 3 onde a soma de todas as linhas, colunas e diagonais é a mesma. Ex: N = 3, soma = 15.
                // 2   7   6
                // 9   5   1
                // 4   3   8
                // Receberemos apenas o N na linha de comando e o Magic Square será gerado e exibido!

                // Pegando o N do magic square, de modo que N >= 3
                int n = Integer.valueOf(dataSeparated[1]);
                if (n >= 3) {
                    System.out.println("\nSolicitando o serviço Magic Square ao executor...");
                    // Criando a task Magic Square e executando
                    MagicSquare task = new MagicSquare(n);
                    Vector<Vector<Integer>> magicSquare = comp.executeTask(task);

                    System.out.println("\nResposta do serviço Magic Square recebida, enviando ao cliente...");
                    // Enviando o magic_square
                    // Primeiro enviamos a dimensão
                    out.writeInt(n);
                    // Depois enviamos os números em ordem [numeroslinha1, numeroslinha2 , ... , numeroslinhan]
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            out.writeInt(magicSquare.get(i).get(j));
                        }
                    }

                    out.flush();
                } else {
                    throw new IllegalArgumentException("\nPara o serviço MagicSquare, 'N' tem que ser maior ou igual a 3!");
                }
            } else if (dataSeparated[0].toLowerCase().equals("fibonacci")) {
                // Pegando o N referente ao número o qual calcularemos Fibonacci
                int n = Integer.valueOf(dataSeparated[1]);
                if (n >= 0) {
                    System.out.println("\nSolicitando o serviço Fibonacci ao executor...");
                    // Criando a task Fibonacci e executando
                    Fibonacci task = new Fibonacci(n);
                    Integer fibonacciNumber = comp.executeTask(task);

                    System.out.println("\nResposta do serviço Fibonacci recebida, enviando ao cliente...");
                    // Enviando o número de Fibonacci para o cliente
                    out.writeInt(fibonacciNumber.intValue());
                    out.flush();
                } else {
                    throw new IllegalArgumentException("\nPara o serviço Fibonacci, N tem que ser maior ou igual a 0\n");
                }
            } else if (dataSeparated[0].toLowerCase().equals("sort")) {
                // É entrado como input o nome do algoritmo (bubblesort, insertionsort, selectionsort) e a cadeia de números a ser ordenada,
                // como último argumento basta colocar a letra "e" que irá indicar o fim da cadeia de números

                // Criando o array de números a partir dos argumentos para enviar para o cálculo
                Vector<Integer> numberList = new Vector<Integer>();
                String algorithm = dataSeparated[1].toLowerCase();

                if (algorithm.equals("selectionsort") || algorithm.equals("insertionsort")
                        || algorithm.equals("bubblesort")) {
                    // A partir do argumento 1, pois o argumento 0 é o algoritmo a ser utilizado
                    for (int i = 2; i < dataSeparated.length; i++) {
                        numberList.add(Integer.valueOf(dataSeparated[i]));
                    }

                    System.out.println("\nSolicitando o serviço de Sort (ordenação) ao executor...");
                    // Criando e chamando a task de ordenação
                    Sort task = new Sort(algorithm, numberList);
                    Vector<Integer> sortedList = comp.executeTask(task);

                    System.out.println("\nResposta do serviço Sort recebida, enviando ao cliente...");
                    // Transformando o Vector<Integer> em um int[]
                    int sortedListInt[] = new int[sortedList.size()];
                    for (int j = 0; j < numberList.size(); j++) {
                        sortedListInt[j] = sortedList.get(j);
                    }

                    // Enviando a lista ordenada para o cliente
                    // Primeiro envia o tamanho da lista
                    out.writeInt(sortedListInt.length);
                    for (int i = 0; i < sortedListInt.length; i++) {
                        out.writeInt(sortedListInt[i]);
                    }
                    out.flush();
                } else {
                    throw new IllegalArgumentException(
                            "\nVocê escolheu um algoritmo indisponível. Os válidos são:\n- SelectionSort\n- InsertionSort\n- BubbleSort");
                }
            }
            else if (dataSeparated[0].toLowerCase().equals("encrypt")) {
    // Receives a string and a shift value as arguments
    String originalString = dataSeparated[1];
    int shift = Integer.parseInt(dataSeparated[2]);
    Encrypt task = new Encrypt(originalString, shift);
    String encryptedString = task.execute();

    System.out.println("\nResposta do serviço Encrypt recebida, enviando ao cliente...");
    // Sends the encrypted string back to the client
    out.writeObject(encryptedString);
    out.flush();
}
        } catch (Exception e) {
            System.err.println(
                    "\nErro ao executar serviços do servidor - verifique se as entradas do cliente foram corretas. Para ajuda, utilize 'help': \n");
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                /* close failed */
            }
        }
    }
}