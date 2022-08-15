package blackjack;

/**
 * Defines a kind of card. 
 */

public class Card {
    public static final int NUM_KINDS = 10;
    public static final int ACE_VALUE = 11;

    private int kind;
    private int value;
    private int max_copy;
    private int num_copy;

    public Card(int kind) {
        this.kind = kind;
        if (kind == 1 || kind == ACE_VALUE) {
            this.kind = 1;
            this.value = 11;
        } else {
            this.value = kind;
        }

        if (kind != 10) {
            this.max_copy = 4;
        } else {
            this.max_copy = 16;
        }

       this. num_copy = this.max_copy;
    }

    /**
     * This kind of cards have one less copy. 
     */
    public void deal() {
        --num_copy;
    }

    /**
     * The number of this kind of cards is back to its original. 
     */
    public void renew() {
        num_copy = max_copy;
    }

    /**
     * Change Ace value from 1 to 11 
     * or from 11 to 1.
     */
    public void changeAceValue() {
        if (kind == 1) {
            if (value == 1) {
                value = 11;
            } else {
                value = 1;
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
     * @return the num_copy
     */
    public int getNum_copy() {
        return num_copy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        Card other = (Card) obj;
        if (this.kind != other.kind) {
            return false;
        }

        return true;
    }
}
