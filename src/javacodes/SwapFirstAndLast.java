package javacodes;

import java.util.Scanner;

public class SwapFirstAndLast {

    public static void swap(char[] sentence) {
        int first = 0;
        int count = 0;
        boolean isDigit = false;
        for (int i = 0; i <= sentence.length; i++) {
            //short circuit
            if (i == sentence.length || sentence[i] == ' ') {
                int last = i - 1;
                if (!isDigit) {
                    char temp = sentence[first];
                    sentence[first] = sentence[last];
                    sentence[last] = temp;
                }
                isDigit = false;
                first = i + 1;
                count++;
            } else if (Character.isDigit(sentence[i])) {
                isDigit = true;
            }
        }

        count++;
        System.out.println("Count of Words in Sentence: " + count);
        System.out.println(sentence);
    }
    //Finding needle in haystack, delta->difference
    public static boolean contains(String haystack, String needle) {
        int haystackLength = haystack.length();
        int needleLength = needle.length();
        int delta = haystackLength - needleLength;
        for (int i = 0; i < delta; i++) {
            boolean isFound = true;
            for (int j = 0; j < needleLength; j++) {
                if (haystack.charAt(i + j) != needle.charAt(j)) {
                    isFound = false;
                }
            }
            if (isFound) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("Enter sentence:");
        Scanner sc = new Scanner(System.in);
        String sentence = sc.nextLine();
        swap(sentence.toCharArray());
        System.out.println(contains("Hello World", "llo"));

    }
}
