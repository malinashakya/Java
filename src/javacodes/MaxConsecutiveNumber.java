/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javacodes;

/**
 *
 * @author malin
 */
public class MaxConsecutiveNumber {
    public static void main(String[] args) {
        String numbers = "10011110001111110";
        int max = 0;
        int maxIndex = 0;
        int length = numbers.length();
        int currentConsecutiveLength = 1;

        for (int i = 1; i <length ; i++) {
//            System.out.println(i);
            if (numbers.charAt(i - 1) == numbers.charAt(i)) {
                currentConsecutiveLength++;
            }
            else{
                currentConsecutiveLength=1;
            }
            if(currentConsecutiveLength>max)
            {
                max=currentConsecutiveLength;
//                System.out.println("Inside:"+i);
                maxIndex=i-max+1;
            }
        }
        System.out.println(numbers.length());
        System.out.println(max);
        System.out.println("First index of max Consecutive number: "+maxIndex);
    }
}
