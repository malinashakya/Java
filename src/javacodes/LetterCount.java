/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javacodes;

/**
 *
 * @author malin
 */
import javacodes.NewHashMap;
public class LetterCount {
    public static void main(String[] args) {
        NewHashMap hashmap=new NewHashMap();
        String paragraph="Hello this is letter counting program using HashMap";
        
        int length=paragraph.length();
       
        for (int i = 0; i < length; i++) {
            char currentCharacter = paragraph.charAt(i);

            if (Character.isLetter(currentCharacter)) {
                Integer count = (Integer) hashmap.get(currentCharacter); 
                if (count != null) {
                    count++; 
                } else {
                    count = 1; 
                }
                hashmap.put(currentCharacter, count);
            }
        }
        hashmap.printAllElements();
    }
}
