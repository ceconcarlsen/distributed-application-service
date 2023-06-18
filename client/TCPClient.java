package client;
import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.Integer;
import compute.Compute;



public class TCPClient {
	public static void main (String args[]) {
	
		Socket s = null;
		try{
		

			int serverPort = 7896;

			//se conectando ao socket
			s = new Socket("127.0.0.1", serverPort);

			//criando output stream do client
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.flush();
	
			//criando input stream do cliente
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());

			//caso o cliente peça "help"
			if(args[0].toLowerCase().equals("help")){
				System.out.println("\nO formato padrao dos inputs: <nome do servico> <param1> <param2> ... <paramN>");
				System.out.println("Os servicos disponiveis e seus parametros sao: \n");
				System.out.println("	Sort <tipo> <lista de numeros>\n");
				System.out.println("		<tipo>: {'Bubblesort', 'SelectionSort', 'InsertionSort'}\n");
				System.out.println("		exemplo de entrada: Sort bubblesort 5 3 6 2 0 3 9 63 21 65\n");
				System.out.println("	MagicSquare <n>\n");
				System.out.println("		<n>: dimensao da matriz a ser gerada, n >= 3\n");
				System.out.println("		exemplo de entrada: MagicSquare 11\n");
				System.out.println("	Fibonacci <n>\n");
				System.out.println("		<n>: n-esimo numero de Fibonacci sera retornado, n >= 0\n");
				System.out.println("		exemplo de entrada: Fibonacci 15\n");
			}
			else{
			//criando string a ser enviada pelo servidor
				String out_string = "";
				for(int i = 0; i < args.length; i++){
					if(i != 0)
						out_string += " ";
					out_string += args[i];
				}
				//enviando lista de argumentos
				out.writeObject(out_string);

				//de acordo com o serviço requerido, o tratamento com o retorno será diferente
				if(args[0].toLowerCase().equals("sort")){
					//criando um vetor com o tamanho passado primeiro
					int sorted_list[] = new int[in.readInt()];
					for(int i = 0; i < sorted_list.length; i++){
						sorted_list[i] = in.readInt();
					}

					//mostrando a lista ordenada na tela
	                System.out.print("\nResposta recebida do servidor, sobre o servico Sort: ");
	                System.out.print("Lista ordenada: ");
	                for(int j = 0; j < sorted_list.length; j++){
	                    System.out.print(sorted_list[j] + " ");
	                }
	                System.out.println("\n");
				}
				else if(args[0].toLowerCase().equals("magicsquare")){
					int n = in.readInt();
					int[][] magic_square = new int[n][n];
					//preenchendo o magic square com os dados
					for(int i = 0; i < n; i++){
	                	for(int j = 0; j < n; j++){
	                		magic_square[i][j] = in.readInt();
	                	}
	                }

					//mostrando o magic square
	                System.out.println("\nResposta recebida do servidor, sobre o servico Magic Square: ");
	                System.out.println("Magic Square para N = " + n + ":");
	                for(int i = 0; i < n; i++){
	                    for(int j = 0; j < n; j++){
	                    	System.out.format("%5d", magic_square[i][j]);
	                    }
	                    System.out.println();
	                }
	                System.out.println("Soma de cada linha/coluna/diagonal = " + n*(n*n+1)/2 + "\n");
				}
				else if(args[0].toLowerCase().equals("fibonacci")){
					int fibonacci_number = in.readInt();
					//mostrando numero
					//mostrando a resposta do servidor
                	System.out.println("\nResposta recebida do servidor, sobre o servico Fibonacci: ");
                	System.out.println("Numero de Fibonacci para n = " + args[1] + ": " + fibonacci_number);
                	System.out.println("\n");
				}
				//caso nao seja nenhum servico conhecido, dar um erro
				else{
					throw new IllegalArgumentException("\n Verifique se entrou um servico existente. \n");
				}
				 
			}
		}catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
		}catch (EOFException e){System.out.println("EOF:"+e.getMessage());
		}catch (IOException e){System.out.println("readline:"+e.getMessage());
		}finally {if(s!=null) try {s.close();}catch (IOException e){System.out.println("close:"+e.getMessage());}}
     }
}
