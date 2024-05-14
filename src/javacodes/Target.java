/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javacodes;

/**
 *
 * @author malin
 */
public class Target {
    public static void targetAchieve(int[] array,int target)
    {
     for(int i=0;i<array.length;i++)
     {
         for(int j=0;j<array.length;j++ )
         {
             if(array[i]+array[j]==target)
             {
                 System.out.println("["+array[i] +","+ array[j]+"]");
                 return;
           
             }
             
         }
     }
    }
    public static void main(String[] args) {
        int[] array1={1,2,3,4,5,6};
        int target1=10;
        int[] array2={2,7,11,15};
        int target2=9;
        targetAchieve(array1,target1);
        targetAchieve(array2,target2);
    }
            
}
