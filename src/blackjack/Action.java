package blackjack;

/**
 * Class {@code Action} enumerates actions that need to be taken in blackjack.
 */
public enum Action {
    /**
     * Opt out of the game.
     */
    SURRENDER,
    /**
     * Keep current cards and stop drawing.
     */
    STAND,
    /**
     * Draw a card.
     */
    HIT,
    /**
     * Split the first two cards that are of the same number in hand and play them
     * as separate hands.
     */
    SPLIT,
    /**
     * Double Down in full.
     * Double the money of original wager, draw one card.
     * Cannot be used after using {@code HIT}.
     */
    DOUBLE,
}
