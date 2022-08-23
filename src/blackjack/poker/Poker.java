package blackjack.poker;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Class {@code Poker} defines a deck of cards and its behaviour.
 */

public class Poker {
    /**
     * smallest kind
     */
    public static final int ACE = 1;

    /**
     * largest kind
     */
    public static final int KING = 13;

    /**
     * value of Ace
     */
    public static final int ACE_VALUE = 11;

    /**
     * value of court cards or face cards i.e. Jack, Queen, King
     */
    public static final int COURT_CARD_VALUE = 10;

    /**
     * LinkedList that stores cards
     */
    private final LinkedList<Card> poker = new LinkedList<>();

    /**
     * Constructs a new deck of poker
     */
    public Poker() {
        newPack();
    }

    /**
     * Make the card deck complete.
     */
    public void newPack() {
        poker.clear();
        for (int kind = ACE; kind <= KING; ++kind) {
            for (Suit suit : Suit.values()) {
                poker.add(new Card(kind, suit));
            }
        }
    }

    /**
     * Deal the card of given kind and return this card.
     * 
     * @param kind
     * @return {@code Card} of given kind, {@code null} if not found
     */
    public Card deal(int kind) {
        for (Iterator<Card> iter = poker.iterator(); iter.hasNext();) {
            Card card = iter.next();
            if (card.getKind() == kind) {
                iter.remove();
                return card;
            }
        }
        return null;
    }
}
