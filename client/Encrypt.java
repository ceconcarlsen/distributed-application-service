package client;

import compute.Task;
import java.io.Serializable;

/* Cifra de César  

A cifra de César opera substituindo cada letra do texto original por outra letra que está um número fixo de posições adiante ou atrás no alfabeto. 
Esse número fixo é conhecido como "chave de deslocamento" ou "chave de cifra". 
Por exemplo, com uma chave de deslocamento de 3, a letra "A" seria substituída pela letra "D", "B" seria substituída por "E", e assim por diante.

*/

public class Encrypt implements Task<String>, Serializable {

    private static final long serialVersionUID = 227L;

    private final String originalString;
    private final int shift;

    public Encrypt(String originalString, int shift) {
        this.originalString = originalString;
        this.shift = shift;
    }

    // Calls the function that performs the encryption
    public String execute() {
        return encryptString(originalString, shift);
    }

    // Performs the encryption using the Caesar cipher
    public String encryptString(String originalString, int shift) {
        StringBuilder encryptedString = new StringBuilder();

        for (int i = 0; i < originalString.length(); i++) {
            char character = originalString.charAt(i);

            if (Character.isLetter(character)) {
                // Checks if it's an uppercase or lowercase letter
                boolean isUpperCase = Character.isUpperCase(character);
                // Converts to uppercase for easier calculations
                character = Character.toUpperCase(character);
                // Applies the shift to the character
                char encryptedChar = (char) ((character - 'A' + shift) % 26 + 'A');

                if (!isUpperCase) {
                    // Converts back to lowercase if necessary
                    encryptedChar = Character.toLowerCase(encryptedChar);
                }

                // Adds the encrypted character to the final string
                encryptedString.append(encryptedChar);
            } else {
                // Keeps the original character if it's not a letter
                encryptedString.append(character);
            }
        }

        System.out.println("\nEnviando resposta da solicitação ao servidor... ");
        return encryptedString.toString();
    }
}
