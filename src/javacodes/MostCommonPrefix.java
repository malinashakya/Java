/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javacodes;

/**
 *
 * @author malin
 */
import java.util.ArrayList;
public class MostCommonPrefix {
    public static void main(String[] args) {
        ArrayList<String> animal=new ArrayList<>();
        animal.add("Raccoon");
        animal.add("Rat");
        animal.add("Rabbit");
        String commonPrefix="";
        int arraySize=animal.size();
        int leastArrayLength=Integer.MAX_VALUE;
         for (String string : animal) {
            if (string.length() < leastArrayLength) {
                leastArrayLength = string.length();
            }
         }
         for(int i=0;i<leastArrayLength;i++)
         {
          char currentCharacter=animal.get(0).charAt(i);
          boolean isSame=true;
          for(String string:animal)
          {
              if(string.charAt(i)!=currentCharacter)
              {
                  isSame=false;
                  break;
              }
          }
          if (isSame)
          {
              commonPrefix+=currentCharacter;
          }
          else{
              break;
          }
         }
         System.out.println("Common prefix:"+commonPrefix);
    }
}
