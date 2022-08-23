package blackjack.poker;

/**
 * Class {@code Card} defines a card that has its own kind, suit and value.
 */

public class Card {
    private final int kind;
    private final Suit suit;
    private int value;

    /**
     * Constructs a new card
     * 
     * @param kind
     * @param suit
     */
    public Card(int kind, Suit suit) {
        this.kind = kind;
        this.suit = suit;
        if (kind == Poker.ACE) {
            this.value = Poker.ACE_VALUE;
        } else if (kind >= 2 && kind <= 10) {
            this.value = kind;
        } else {
            // if (kind >= 11 && kind <= KING)
            this.value = Poker.COURT_CARD_VALUE;
        }
    }

    /**
     * Check if two cards are of the same number.
     * 
     * @param card1
     * @param card2
     * @return {@code true} if have same value or kind, {@code false} otherwise
     */
    public static boolean isSameNumber(Card card1, Card card2) {
        return card1.value == card2.value || card1.kind == card2.kind;
    }

    /**
     * Change Ace card value from 11 to 1
     * or from 1 to 11.
     */
    public void changeAceValue() {
        if (kind == Poker.ACE) {
            if (value == Poker.ACE) {
                value = Poker.ACE_VALUE;
            } else {
                value = Poker.ACE;
            }
        }
    }

    /**
     * @return the kind
     */
    public int getKind() {
        return kind;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @return the suit
     */
    public Suit getSuit() {
        return suit;
    }
}
