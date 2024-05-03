/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javacodes.BlackJackMultiplayerGame.gametype;

import javacodes.BlackJackMultiplayerGame.Card;

/**
 *
 * @author malin
 */
public class GameTypeSumIsLessThan21 extends GameType{
    public GameTypeSumIsLessThan21()
    {
        System.out.println("You have selected the Win if sum is less than 21");
    }
   
     @Override
    public int calculate (Card card1,Card card2, Card card3)
    {
        return normalsum(card1.getInt(), card2.getInt(), card3.getInt());
    }
    @Override
    public int evaluate(Card card1,Card card2, Card card3)
    {
        return sumIsLessThan21(card1.getInt(), card2.getInt(), card3.getInt())?1:0;
    }
    
}
