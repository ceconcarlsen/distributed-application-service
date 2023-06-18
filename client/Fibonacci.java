package client;

import compute.Task;
import java.io.Serializable;
import java.util.*;
import java.lang.Integer;

public class Fibonacci implements Task<Integer>, Serializable {

    private static final long serialVersionUID = 227L;

    private final int n;
    public Fibonacci(int n) {
        this.n = n;
    }

	//Chama a funcao que faz o magic square
    public Integer execute() {
        return computeFibonacci(n);
    }

    //usando programacao dinamica (armazenando valores previamente calculados)
    public Integer computeFibonacci(int n) {
        
        Integer f_numbers[] = new Integer[n+2];
        
        f_numbers[0] = 0;
        f_numbers[1] = 1;

        for(int i = 2; i <= n; i++){
            f_numbers[i] = f_numbers[i - 1] + f_numbers[i - 2];
        }

        System.out.println("\nEnviando resposta da solicitaçao ao servidor... ");
        return f_numbers[n];
    }
 
}
