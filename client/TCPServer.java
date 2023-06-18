package client;
import java.net.*;
import java.io.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.lang.Integer;
import compute.Compute;


public class TCPServer {
	public static void main (String args[]) {
		try{
            //conectar com o cliente
			int serverPort = 7896;
			ServerSocket listenSocket = new ServerSocket(serverPort);
			while(true) {
				Socket clientSocket = listenSocket.accept();
				Connection c = new Connection(clientSocket);
			}
		} catch(IOException e) {System.out.println("Listen socket:"+e.getMessage());
		} 
	}

}
class Connection extends Thread {
	public static boolean debug = false;
	//variaveis da conexao cliente - servidor
	ObjectInputStream in;
	ObjectOutputStream out;
	Socket clientSocket;

	//variaveis da conexao executor - servidor
	String name;
	Registry registry;
	Compute comp;
	public Connection (Socket aClientSocket) {

		try {
			//preparando a conexão servidor-executor
			name = "Compute";
            registry = LocateRegistry.getRegistry("localhost", 1099);
            comp = (Compute) registry.lookup(name);

            System.out.println("\nConexao servidor-executor estabelecida com sucesso...\n");	
            //criando input e output streams
			clientSocket = aClientSocket;
			in = new ObjectInputStream(clientSocket.getInputStream());
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			out.flush();

			System.out.println("\nConexao cliente-servidor estabelecida com sucesso...\n");
			this.start();
		}catch(IOException e) {System.out.println("Connection:"+e.getMessage());
		}catch (Exception e) {
            System.err.println("\nErro ao executar a conexao servidor-executor:");
            e.printStackTrace();
        }
	}
	public void run(){
		//security da conexão server-executor
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
		try {
			System.out.println("\nIniciando leitura dos dados enviados pelo cliente\n");
            //pegando a string com as entradas do cliente: nomeServiço arg1 arg2 arg3 arg4...
			String data = (String)in.readObject();
			System.out.println("\nData: " + data + "\n");
			//separando entradas do cliente
			String[] data_separated = data.split(" ");
			//tendo agora as entradas do cliente podemos escolher qual o tipo de serviço ele deseja e executamos de acordo
			if(data_separated[0].toLowerCase().equals("magicsquare")){
				//Um "Magic Square" é uma matriz de dimensão N >= 3 onde a soma de todas as linhas, colunas e diagonais é a mesma. Ex: N = 3, soma = 15.
	            // 2   7   6
	            // 9   5   1
	            // 4   3   8
	            //Receberemos apenas o N na linha de comando e o Magic Square será gerado e exibido!

	            //pegando o N do magic square, de modo que N >= 3
	            int n = Integer.valueOf(data_separated[1]);
	            if(n >= 3){
	             	System.out.println("\nSolicitando o servico Magic Square ao executor...");
	                //criando a task Magic Square e executando
	                MagicSquare task = new MagicSquare(n);
	                Vector<Vector<Integer>> magic_square = comp.executeTask(task);

	                System.out.println("\nResposta do servico Magic Square recebida, enviando ao cliente...");
	                //enviando o magic_square
	                //primeiro enviamos a dimensao
	                out.writeInt(n);
	                //depois enviamos os numeros em ordem [numeroslinha1, numeroslinha2 , ... , numeroslinhan]
	                for(int i = 0; i < n; i++){
	                	for(int j = 0; j < n; j++){
	                		out.writeInt(magic_square.get(i).get(j));
	                	}
	                }

	                //
	                out.flush();
	            }
	            else{
	                throw new IllegalArgumentException("\n Para o servico MagicSquare 'N' tem que ser maior ou igual a 3!");
	            }
			}
			else if(data_separated[0].toLowerCase().equals("fibonacci")){
				// pegando o N referente ao numero o qual calcularemos fibonnaci
	            int n = Integer.valueOf(data_separated[1]);
	            if(n >= 0){
	                System.out.println("\nSolicitando o servico Fibonacci ao executor...");
	                //criando a task Fibonacci e executando
	                Fibonacci task = new Fibonacci(n);
	                Integer fibonacci_number = comp.executeTask(task);

	                System.out.println("\nResposta do servico Fibonacci recebida, enviando ao cliente...");
	                //enviando o numero de fibonacci para o cliente
	                out.writeInt(fibonacci_number.intValue());
	            	out.flush();
	            }
	            else{
	                throw new IllegalArgumentException("\n Para o serviço Fibonacci N tem que ser maior ou igual a 0 \n");
	            }
			}
			else if(data_separated[0].toLowerCase().equals("sort")){
				//É entrado como input o nome do algoritmo (bubblesort, insertionsort, selectionsort) e a cadeia de numeros a ser ordenada, 
	            //como ultimo argumento basta colocar a letra "e""que irá indicar o fim da cadeia de numeros

	            //criando o array de numeros a partir dos argumentos para enviar para o calculo
	            Vector<Integer> number_list = new Vector<Integer>();
	            String algorithm = data_separated[1].toLowerCase();


	            if(algorithm.equals("selectionsort") || algorithm.equals("insertionsort") || algorithm.equals("bubblesort")){
	                // a partir do argumento 1 - pois o argumento 0 é o algoritmo a ser utilizado 
	                for(int i = 2; i < data_separated.length; i++){ 
	                    number_list.add(Integer.valueOf(data_separated[i]));
	                }
	                
	                System.out.println("\nSolicitando o servico de Sort (ordenacao) ao executor...");
	                //criando e chamando a task de ordenacao
	                Sort task = new Sort(algorithm, number_list);
	                Vector<Integer> sorted_list = comp.executeTask(task);

	                System.out.println("\nResposta do servico Sort recebida, enviando ao cliente...");
	                //transformando o Vector<Integer> em um int[]
		            int sorted_list_int[] = new int[sorted_list.size()];
		            for(int j = 0; j < number_list.size(); j++){
		            	sorted_list_int[j] = sorted_list.get(j);
		            }
		        
	                //enviando a lista ordenada para o cliente
	                //primeiro envia o tamanho da lista
	                out.writeInt(sorted_list_int.length);
	                for(int i = 0; i < sorted_list_int.length; i++){
	                	out.writeInt(sorted_list_int[i]);	
	                }
	            	out.flush();
	            }
	            else{
	                throw new IllegalArgumentException("\nVoce escolheu um algoritmo indisponivel, os validos sao:\n- SelectionSort\n- InsertionSort\n- BubbleSort");
	            }
			}
		}catch(Exception e){System.err.println("\nErro ao executar servicos do servidor - verifique se as entradas do cliente foram corretas, para ajuda utilize 'help': \n");e.printStackTrace();
		}finally{ try {clientSocket.close();}catch (IOException e){/*close failed*/}}
		

	}
}
