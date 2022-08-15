package blackjack;

import java.util.ArrayList;

/**
 * Defines a deck of cards. 
 */

public class Poker {
    private final ArrayList<Card> poker = new ArrayList<>() {
        {
            for (Integer i = 1; i <= 10; ++i) {
                add(new Card(i));
            }
        }
    };

    public Poker() {
    }

    /**
     * Make the card deck complete. 
     */
    public void newPack() {
        for (Card card : poker) {
            card.renew();
        }
    }

    /**
     * Deal the card of given kind and return this card.
     * @param kind
     * @return card of given kind
     */
    public void deal(int kind) {
        for (Card card : poker) {
            if (card.getKind() == kind) {
                card.deal();
            }
        }
    }

    /**
     * @return the card deck
     */
    public ArrayList<Card> getPoker() {
        return poker;
    }
}
