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
import java.util.Arrays;
import java.util.Scanner;
public class Exchangingnumbers {
    public static void main(String[] args) {
        int valueRemoveIndex;
        int right;
        int removeValue;
        Scanner sc =new Scanner(System.in);
        System.out.println("Provide the number that is to be removed:");
        removeValue=sc.nextInt();
        int[] numbers = {1,3,2,3,4,3,5,6,6};
        right=numbers.length-1;
        System.out.println("Right:"+right);
        for(int i=0;i<numbers.length;i++)
        {
            if(numbers[i]==removeValue && right>0)
            {
                numbers[i]=numbers[right];
                numbers[right]=0;
                right--;
            }
        }
        for(int values:numbers)
        {
            System.out.println(values);
        }
    }
}
