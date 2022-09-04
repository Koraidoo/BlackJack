package blackjack;

/**
 * Player input 2 kinds of card in their hand and 1 kind of card in dealer's
 * hand.
 * It will display the action player should take in terminal.
 */

public class App {
    public static void main(String[] args) throws Exception {
        Blackjack b = new Blackjack();
        b.startGame();
    }
}
