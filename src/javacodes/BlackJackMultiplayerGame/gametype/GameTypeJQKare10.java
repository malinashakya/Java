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
public class GameTypeJQKare10 extends GameType{
    public GameTypeJQKare10()
    {
        System.out.println("You have selected the Win if J, K, Q are equal to 10, A is equal to 1 condition");
    }
    @Override
    public int evaluate (Card card1,Card card2, Card card3)
    {
        return sumvalueJQKare10(card1.getInt(), card2.getInt(), card3.getInt());
    }
    @Override
    public int calculate(Card card1,Card card2, Card card3)
    {
        return JQKare10(card1.getInt(), card2.getInt(), card3.getInt())?1:0;
    }
}
