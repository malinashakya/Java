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
public class ContainsCheck {
    public static void main(String[] args) {
        String mainString="Hello World";
        boolean isFound=false;
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the combination of character to  be found");
        String char1=sc.next();
        for(int i=0;i<mainString.length()-char1.length();i++)
        {
            boolean match=true;
            for(int j=0;j<char1.length();j++)
            {
                if(mainString.charAt(i+j)!=char1.charAt(j))
                {
                    match=false;
                    break;
                }
            }
            if(match)
            {
                isFound=true;
                break;
            }
        }
        if(isFound)
    {
        System.out.println("Substring found");
    }
        else{
            System.out.println("Not found");
        }
    }
    
}
