/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javacodes;

/**
 *
 * @author malin
 */
import java.util.Scanner;

public class IndexOf {
    public static int IndexOf(String word, char letter) {
        int length = word.length();
        for (int i = 0; i < length; i++) {
            if (word.charAt(i) == letter) {
                return i;
            }
        }
        return -1;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String word = "Hello worlds";
        word=word.toLowerCase();
        //To ask only one character instead of whole word/String
        System.out.println("Input the character");
        char input = sc.next().charAt(0);
        if (IndexOf(word, input) == -1) {
            System.out.println("No such character");
        } else {
            System.out.println("Index of:" + input + " is:" + IndexOf(word, input));
        }
    }
}
