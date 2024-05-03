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
public class GameTypeAJQKare10 extends GameType {

      public GameTypeAJQKare10()
    {
        System.out.println("You have selected the Win if A,J, K, Q are equal to 10condition");
    }
    @Override
    public int evaluate (Card card1,Card card2, Card card3)
    {
        return sumvalueAJQKare10(card1.getInt(), card2.getInt(), card3.getInt());
    }
    @Override
    public int calculate(Card card1,Card card2, Card card3)
    {
        return AJQKare10(card1.getInt(),card2.getInt(),card3.getInt())?1:0;
    }
}