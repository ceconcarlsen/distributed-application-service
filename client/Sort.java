package client;

import compute.Task;
import java.io.Serializable;
import java.util.*;
import java.lang.Integer;

public class Sort implements Task<Vector<Integer>>, Serializable {

    private static final long serialVersionUID = 227L;

    //definindo a variavel que irá guardar a lista de numeros
    private final Vector<Integer> number_list;
    //definindo a variavel que ira guardar o algoritmo a ser utilizado
    private final String algorithm;
    
    public Sort(String algorithm, Vector<Integer> number_list) {
        this.algorithm = new String(algorithm);
        this.number_list = new Vector<Integer>(number_list);
    }

	//Chama a ordenacao  
    public Vector<Integer> execute() {
        return computeSort(algorithm, number_list);
    }

    public Vector<Integer> BubbleSort(Vector<Integer> number_list){
    	int n = number_list.size();
    	Vector<Integer> sorted_list = new Vector<Integer>(number_list);
    	System.out.println("\nOrdenando utilizando BubbleSort...");
        for (int i = 0; i < n-1; i++){
            for (int j = 0; j < n-i-1; j++){
                if (sorted_list.get(j) > sorted_list.get(j+1)){
                    //troca
                    int temp = sorted_list.get(j);
                    int temp2 = sorted_list.get(j+1);
                    sorted_list.remove(j); 
                    sorted_list.add(j, temp2);
                    sorted_list.remove(j+1);
                    sorted_list.add(j+1, temp);
                }
            }
        }
        System.out.println("\nEnviando resposta da solicitacao ao servidor...\n");
        return sorted_list;
    }

    public Vector<Integer> InsertionSort(Vector<Integer> number_list){
    	int n = number_list.size();
    	Vector<Integer> sorted_list = new Vector<Integer>(number_list);
    	System.out.println("\nOrdenando utilizando InsertionSort...");
		for (int i = 1; i < n; ++i) {
        	int key = sorted_list.get(i);
        	int j = i - 1;

        	while (j >= 0 && sorted_list.get(j) > key){
        		int temp = sorted_list.get(j);
        		sorted_list.remove(j+1);
        		sorted_list.add(j+1, temp);
            	j = j - 1;
        	}
        	sorted_list.remove(j+1);
        	sorted_list.add(j+1, key);
    	}
        System.out.println("\nEnviando resposta da solicitacao ao servidor...\n");
    	return sorted_list;
    }

    public Vector<Integer> SelectionSort(Vector<Integer> number_list){
    	int n = number_list.size();
    	Vector<Integer> sorted_list = new Vector<Integer>(number_list);
		System.out.println("\nOrdenando utilizando SelectionSort...");
		for (int i = 0; i < n-1; i++){
            int min_index = i;
            for (int j = i+1; j < n; j++)
                if (sorted_list.get(j) < sorted_list.get(min_index))
                    min_index = j;

            int temp1 = sorted_list.get(min_index);
            int temp2 = sorted_list.get(i);
            sorted_list.remove(min_index);
            sorted_list.add(min_index, temp2);

            sorted_list.remove(i);
            sorted_list.add(i, temp1);
    	}
        System.out.println("\nEnviando resposta da solicitacao ao servidor...\n");
    	return sorted_list;
    }
    // Aqui faz a ordenação da lista
    public Vector<Integer> computeSort(String algorithm, Vector<Integer> number_list) {
    	Vector<Integer> sorted_list = new Vector<Integer>(number_list);

    	//BubbleSort
    	if(algorithm.equals("bubblesort")){
    		return this.BubbleSort(number_list);
    	}
    	//InsertionSort
    	if(algorithm.equals("insertionsort")){
    		return this.InsertionSort(number_list);
    	}
    	//SelectionSort
    	if(algorithm.equals("selectionsort")){
    		return this.SelectionSort(number_list);	
    	}

    	return sorted_list;
    }
 
}
