package client;

import compute.Task;
import java.io.Serializable;
import java.util.*;
import java.lang.Integer;

public class MagicSquare implements Task<Vector<Vector<Integer>>>, Serializable {

    private static final long serialVersionUID = 227L;

    private final int n;
    public MagicSquare(int n) {
        this.n = n;
    }

	//Chama a funcao que faz o magic square
    public Vector<Vector<Integer>> execute() {
        System.out.println("\nIniciando processamento do MagicSquare requerido...\n");
        return computeMagicSquare(n);
    }


    public Vector<Vector<Integer>> odd(int n){
        Vector<Vector<Integer>> magic_square = new Vector<Vector<Integer>>(n);
        //inicializando o magic_square com 0 em todas as posicoes
        Collections.nCopies(n, new Vector<Integer>(Collections.nCopies(n, 0))).forEach((list) -> magic_square.add(new Vector<Integer>(list)));
        
        //se for de ordem impar
        if(n%2 != 0){
            //inicializando posições para o 1 (n/2, n - 1)
            int i = n/2;
            int j = n - 1;

            Vector<Integer> temp;
            //colocando os valores no magic square
            for(int num_atual = 1; num_atual <= n*n; ){
                //condição 3.
                if(i == -1 && j == n){
                    j = n - 2;
                    i = 0;
                }
                //condição 1.
                else{
                    if(j == n){
                        j = 0;
                    }
                    if(i < 0){
                        i = n - 1;
                    }
                }
                //condicao 2.
                if(magic_square.get(i).get(j) != 0){
                    j -= 2;
                    i++;
                    continue;
                }
                else{
                    //pega linha
                    temp = magic_square.get(i);
                    //remove elemento da coluna especifica
                    temp.remove(j);
                    //adiciona o elemento novo
                    temp.add(j, num_atual++);

                    //remove a linha do original e insere a nova
                    magic_square.remove(i);
                    magic_square.add(i, temp);
                }
                //condicao base
                j++;
                i--;
            }
        }
        System.out.println("\nEnviando resposta da solicitacao ao servidor...\n");
        return magic_square;
    }

    public Vector<Vector<Integer>> DoublyEven(int n){
        Vector<Vector<Integer>> magic_square = new Vector<Vector<Integer>>(n);

        //inicializando o magic_square com 0 em todas as posicoes
        Collections.nCopies(n, new Vector<Integer>(Collections.nCopies(n, 0))).forEach((list) -> magic_square.add(new Vector<Integer>(list)));

        Vector<Integer> linha_temp;
        int temp2;
        //preenchendo a matriz com os valores na ordem de contagem
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                //removendo a linha, editando e recolocando
                linha_temp = magic_square.get(i);
                magic_square.remove(i);
                //editando linha
                linha_temp.remove(j);
                linha_temp.add(j, (n*i) + j + 1);
                //adicionando linha de volta
                magic_square.add(i, linha_temp);
            }
        }

        //editando valores de cada canto da matriz, baseando-se na regra:
        // (n*n+1) - arr[i][j]
        //canto superior esquerdo
        for(int i = 0; i < n/4; i++){
            for(int j = 0; j < n/4; j++){
                //removendo a linha, editando e recolocando
                linha_temp = magic_square.get(i);
                magic_square.remove(i);
                //editando linha
                temp2 = linha_temp.get(j);
                linha_temp.remove(j);
                linha_temp.add(j, (n*n + 1) - temp2);
                //adicionando linha de volta
                magic_square.add(i, linha_temp);
            }
        }
        //canto superior direito
        for(int i = 0; i < n/4; i++){
            for(int j = 3*(n/4); j < n; j++){
                //removendo a linha, editando e recolocando
                linha_temp = magic_square.get(i);
                magic_square.remove(i);
                //editando linha
                temp2 = linha_temp.get(j);
                linha_temp.remove(j);
                linha_temp.add(j, (n*n + 1) - temp2);
                //adicionando linha de volta
                magic_square.add(i, linha_temp);
            }
        }
        //canto inferior esquerdo
        for(int i = 3*(n/4); i < n; i++){
            for(int j = 0; j < n/4; j++){
                //removendo a linha, editando e recolocando
                linha_temp = magic_square.get(i);
                magic_square.remove(i);
                //editando linha
                temp2 = linha_temp.get(j);
                linha_temp.remove(j);
                linha_temp.add(j, (n*n + 1) - temp2);
                //adicionando linha de volta
                magic_square.add(i, linha_temp);
            }
        }

        //canto inferior direito
        for(int i = 3*(n/4); i < n; i++){
            for(int j = 3*(n/4); j < n; j++){
                //removendo a linha, editando e recolocando
                linha_temp = magic_square.get(i);
                magic_square.remove(i);
                //editando linha
                temp2 = linha_temp.get(j);
                linha_temp.remove(j);
                linha_temp.add(j, (n*n + 1) - temp2);
                //adicionando linha de volta
                magic_square.add(i, linha_temp);
            }
        }
        
        //centro da matriz
        for(int i = n/4; i < 3*(n/4); i++){
            for(int j = n/4; j < 3*(n/4); j++){
                //removendo a linha, editando e recolocando
                linha_temp = magic_square.get(i);
                magic_square.remove(i);
                //editando linha
                temp2 = linha_temp.get(j);
                linha_temp.remove(j);
                linha_temp.add(j, (n*n + 1) - temp2);
                //adicionando linha de volta
                magic_square.add(i, linha_temp);
            }
        }
        System.out.println("\nEnviando resposta da solicitacao ao servidor...\n");
        return magic_square;
    }

    public Vector<Vector<Integer>> SinglyEven(int n){
        // http://www.1728.org/magicsq3.htm
        Vector<Vector<Integer>> magic_square = new Vector<Vector<Integer>>(n);

        //inicializando o magic_square com 0 em todas as posicoes
        Collections.nCopies(n, new Vector<Integer>(Collections.nCopies(n, 0))).forEach((list) -> magic_square.add(new Vector<Integer>(list)));

        int metade = n/2;
        int k = (n-2)/4;
        int temp;
        int temp2;

        Vector<Integer> linha_temp;
        Vector<Integer> linha_temp2;

        Vector<Integer> swapCol = new Vector<Integer>(n);
        int index=0;//vai acompanhar o swapCol

        Vector<Vector<Integer>> mini_magic_square = this.odd(metade);
        

        //criando as magic boxes
        for(int i = 0; i < metade; i++){
            for(int j = 0; j < metade; j++){
                //A BOX
                linha_temp = magic_square.get(i);
                linha_temp.remove(j);
                linha_temp.add(j, mini_magic_square.get(i).get(j));
                magic_square.remove(i);
                magic_square.add(i, linha_temp);
                //B BOX
                linha_temp = magic_square.get(i+metade);
                linha_temp.remove(j+metade);
                linha_temp.add(j+metade, mini_magic_square.get(i).get(j)+metade*metade);
                magic_square.remove(i+metade);
                magic_square.add(i+metade, linha_temp);
                //C BOX
                linha_temp = magic_square.get(i);
                linha_temp.remove(j+metade);
                linha_temp.add(j+metade, mini_magic_square.get(i).get(j)+2*metade*metade);
                magic_square.remove(i);
                magic_square.add(i, linha_temp);
                //D BOX
                linha_temp = magic_square.get(i+metade);
                linha_temp.remove(j);
                linha_temp.add(j, mini_magic_square.get(i).get(j)+3*metade*metade);
                magic_square.remove(i+metade);
                magic_square.add(i+metade, linha_temp);
            }
        }

        for(int i = 1; i <= k; i++){
            swapCol.add(index, i);
            index += 1;
        }
        for(int i = n-k+2; i <= n; i++){
            swapCol.add(index, i);
            index += 1;
        }


        //trocando valores entre as "magic boxes" C-B e A-D
        for(int i = 1; i <= metade; i++){
            for(int j = 1; j <= index; j++){
                //trocando valores das posicoes magic_square[i-1][swapCol[j-1] - 1] e magic_square[i+metade - 1][swapCol[j-1]-1]
                linha_temp = magic_square.get(i - 1);
                temp = linha_temp.get(swapCol.get(j-1) - 1);

                linha_temp2 = magic_square.get(i + metade - 1);
                temp2 = linha_temp2.get(swapCol.get(j - 1) - 1);

                linha_temp.remove(swapCol.get(j-1) - 1);
                linha_temp.add(swapCol.get(j-1) - 1, temp2);

                magic_square.remove(i - 1);
                magic_square.add(i - 1, linha_temp);

                linha_temp2.remove(swapCol.get(j - 1) - 1);
                linha_temp2.add(swapCol.get(j - 1) - 1, temp);

                magic_square.remove(i + metade - 1);
                magic_square.add(i + metade - 1, linha_temp2);

            }
        }
        

        //trocando "pontas"
        //primeira parte
        temp = magic_square.get(k).get(0);
        linha_temp = magic_square.get(k);
        
        temp2 = magic_square.get(k+metade).get(0);
        linha_temp2 = magic_square.get(k+metade);

        linha_temp.remove(0);
        linha_temp.add(0, temp2);
        magic_square.remove(k);
        magic_square.add(k, linha_temp);

        linha_temp2.remove(0);
        linha_temp2.add(0, temp);
        magic_square.remove(k+metade);
        magic_square.add(k+metade, linha_temp2);

        
        
        //segunda parte
        temp = magic_square.get(k+metade).get(k);
        linha_temp = magic_square.get(k+metade);
        
        temp2 = magic_square.get(k).get(k);
        linha_temp2 = magic_square.get(k);

        linha_temp.remove(k);
        linha_temp.add(k, temp2);
        magic_square.remove(k+metade);
        magic_square.add(k+metade, linha_temp);

        linha_temp2.remove(k);
        linha_temp2.add(k, temp);
        magic_square.remove(k);
        magic_square.add(k, linha_temp2);

        return magic_square;
    }
    // Aqui o magicsquare é criado
    // Logica do magicSquare:
    // As posições são lidas como (i, j)
    // O primeiro numero é sempre guardado na posição (n/2, n-1). O próximo número a partir do primeiro vai ser sempre na posição (i - 1, j + 1)
    // Ou seja, basta decrementar a linha do número anterior por 1 e incrementar a coluna em 1.
    // 1. Caso, a linha calculada se torne -1, nós transformamos em n-1. Se a coluna calculada se tornar n, transformamos em 0.
    // 2. Caso já tenha um número preenchendo a posição calculada, decrementamos a coluna em 2 ao invés de 1, e a linha será incrementada em 1 ao invés de decrementada.
    // 3. Caso a linha calculada se torne -1 e a posição da coluna seja n, a nova posição sera (0, n-2)
    public Vector<Vector<Integer>> computeMagicSquare(int n) {
        Vector<Vector<Integer>> magic_square = new Vector<Vector<Integer>>(n);

        //inicializando o magic_square com 0 em todas as posicoes
        Collections.nCopies(n, new Vector<Integer>(Collections.nCopies(n, 0))).forEach((list) -> magic_square.add(new Vector<Integer>(list)));
        //se for de ordem impar
        if(n%2 != 0){
            System.out.println("\nProcessando caso Odd...\n");
            return this.odd(n);
        }
        //ordem multiplo de 4
        else if(n%4 == 0){
            System.out.println("\nProcessando caso DoublyEven...\n");
            return this.DoublyEven(n);
        }
        //ordem do tipo (4*n + 2) - 6, 10, 14 - singly even order
        else if(n%4 == 2){
            System.out.println("\nProcessando caso SinglyEven...\n");
            return this.SinglyEven(n);
        }
        return magic_square;
    }
 
}
