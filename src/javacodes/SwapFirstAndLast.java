package javacodes;

import java.util.Scanner;

public class SwapFirstAndLast {
    public static void swap(char[] sentence) {
        int first = 0;
        int count = 0;
        boolean Digit = false; 
        for (int i = 0; i < sentence.length; i++) {
            if (sentence[i] == ' ') {
                int last = i - 1;
                if (!Digit) { 
                    char temp = sentence[first];
                    sentence[first] = sentence[last];
                    sentence[last] = temp;
                }
                Digit = false;
                first = i + 1;
                count++;
            } else if (Character.isDigit(sentence[i])) {
                Digit = true; 
            }
        }
        int last = sentence.length - 1;
        if (!Digit) {
            char temp = sentence[first];
            sentence[first] = sentence[last];
            sentence[last] = temp;
        }
        count++; 
        System.out.println("Count of Words in Sentence: " + count);
        System.out.println(sentence);
    }
    
    public static boolean  contains(String paragraph, String characters)
    {
        return paragraph.toLowerCase().contains(characters.toLowerCase());
    }
    public static void main(String[] args) {
        System.out.println("Enter sentence:");
        Scanner sc = new Scanner(System.in);
        String sentence = sc.nextLine();
        swap(sentence.toCharArray());
        System.out.println(contains(sentence,"hel"));
        System.out.println(contains(sentence,"llo e"));
        System.out.println(contains(sentence,"apple"));
        
    }
}
