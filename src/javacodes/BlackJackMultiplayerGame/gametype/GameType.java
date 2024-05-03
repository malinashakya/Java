/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javacodes.BlackJackMultiplayerGame.gametype;

/**
 *
 * @author malin
 */
import javacodes.BlackJackMultiplayerGame.Card;
public abstract class GameType {
public abstract int calculate(Card card1, Card card2, Card card3);
public abstract int evaluate (Card card1, Card card2, Card card3);

    public static boolean sumIsLessThan21(int card1, int card2, int card3) {
        return card1 + card2 + card3 < 21;
    }

    public static boolean sumIsLessThan17(int card1, int card2, int card3) {
        return (card1 + card2 + card3) < 17;
    }

    public static boolean AJQKare10(int card1, int card2, int card3) {
        if (card1 > 10 || card1 == 10) {
            card1 = 10;
        }
        if (card2 > 10 || card2 == 10) {
            card2 = 10;
        }
        if (card3 > 10 || card3 == 10) {
            card3 = 10;
        }
        return (card1 + card2 + card3) < 21;
    }

    public static boolean JQKare10(int card1, int card2, int card3) {
        if (card1 > 10) {
            card1 = 10;
        }
        if (card2 > 10) {
            card2 = 10;
        }
        if (card3 > 10) {
            card3 = 10;
        }
        return (card1 + card2 + card3) < 21;
    }

    public static int sumvalueAJQKare10(int card1, int card2, int card3) {
        if (card1 > 10 || card1 == 10) {
            card1 = 10;
        }
        if (card2 > 10 || card2 == 10) {
            card2 = 10;
        }
        if (card3 > 10 || card3 == 10) {
            card3 = 10;
        }
        return (card1 + card2 + card3);
    }

    public static int sumvalueJQKare10(int card1, int card2, int card3) {
        if (card1 > 10) {
            card1 = 10;
        }
        if (card2 > 10) {
            card2 = 10;
        }
        if (card3 > 10) {
            card3 = 10;
        }
        return (card1 + card2 + card3);
    }

    public static int normalsum(int card1, int card2, int card3) {
        return (card1 + card2 + card3);
    }
}
