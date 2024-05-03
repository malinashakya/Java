/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javacodes.BlackJackMultiplayerGame;

/**
 *
 * @author malin
 */
public class Card {
    int card;
    Card(int card)
    {
        this.card=card;
    }
    public int getInt()
    {
        return this.card;
    }
   public String toString() 
    {
        char[] temp_card = new char[3];
        temp_card[2] = '\0';
        if (card == 11) {
            temp_card[0] = 'J';
        } else if (card == 12) {
            temp_card[0] = 'Q';
        } else if (card == 13) {
            temp_card[0] = 'K';
        } else if (card == 1) {
            temp_card[0] = 'A';
        } else if (card >= 2 && card < 10) {
            temp_card[0] = (char) (card + 48);
        } else if (card == 10) {
            temp_card[0] = '1';
            temp_card[1] = '0';
        }
        return new String(temp_card);
    }
}
