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
public class GameTypeSumIsLessThan17 extends GameType{
    public GameTypeSumIsLessThan17(){
       System.out.println("You have selected the Win if sum is less than 17 condition");
    }

    @Override
    public int calculate (Card card1,Card card2, Card card3)
    {
        return normalsum(card1.getInt(), card2.getInt(), card3.getInt());
    }
    @Override
    public int evaluate(Card card1,Card card2, Card card3)
    {
        return sumIsLessThan17(card1.getInt(), card2.getInt(), card3.getInt())?1:0;
    }
}
