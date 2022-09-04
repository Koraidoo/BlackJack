package blackjack;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

import blackjack.poker.Card;
import blackjack.poker.Poker;

/**
 * Class {@code Blackjack} defines behaviours in a blackjack game.
 */
public class Blackjack {
    private static final int QUIT = 0;
    private static final int ERROR = -1;

    private final ArrayList<Card> cards = new ArrayList<>();
    private final ArrayList<Card> dealerCards = new ArrayList<>();

    /**
     * Looping blackjack games.
     */
    public void startGame() {
        Poker poker = new Poker();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // deal cards
            System.out.print("Enter your first card: ");
            if (!addCard(poker, cards, scanner)) {
                return;
            }

            System.out.println("Enter your second card: ");
            if (!addCard(poker, cards, scanner)) {
                return;
            }

            System.out.println("Enter dealer's card: ");
            if (!addCard(poker, dealerCards, scanner)) {
                return;
            }

            getTotal();

            // get result action
            if (Card.isSameNumber(cards.get(0), cards.get(1))) {
                // two cards have the same value or kind, check for split
                printResult(isSplit());
            } else if (hasAce()) {
                printResult(softTotal());
            } else if (isSurrender()) {
                printResult(Action.SURRENDER);
            } else {
                printResult(hardTotal());
            }

            cards.clear();
            dealerCards.clear();
            poker.newPack();
        }
    }

    /**
     * Scan kind input from terminal and adds a card of that kind to
     * player or dealer's hand.
     * 
     * @param poker   the poker deck
     * @param hand    player or dealer's cards in hand
     * @param scanner the scanner
     * @return {@code true} if successfully adds a card to hand,
     *         {@code false} if signal to quit the game
     */
    private boolean addCard(Poker poker, ArrayList<Card> hand, Scanner scanner) {
        int kind;
        while (true) {
            kind = scanInputKind(scanner);

            if (kind == ERROR) {
                continue;
            } else if (kind == QUIT) {
                return false;
            }

            Card card = poker.deal(kind);
            if (card == null) {
                continue;
            }

            hand.add(card);
            return true;
        }
    }

    /**
     * Scan input from terminal and return as kind of card.
     * 
     * @param scanner scanner that scans input from terminal
     * @return integer input, or {@code ERROR} otherwise
     */
    private int scanInputKind(Scanner scanner) {
        String errMessage = "Please enter integer between 1 and 13 inclusive, or 0 to quit";
        try {
            int kind = scanner.nextInt();
            if (!(kind >= Poker.ACE && kind <= Poker.KING || kind == QUIT)) {
                System.out.println(errMessage);
                scanner.nextLine();
                return ERROR;
            }
            return kind;
        } catch (InputMismatchException err) {
            System.out.println(errMessage);
            scanner.nextLine();
            return ERROR;
        }
    }

    /**
     * Prints action in terminal.
     * 
     * @param action the action
     */
    private void printResult(Action action) {
        String act = "";
        switch (action) {
            case STAND -> act += "stand";
            case HIT -> act += "hit";
            case SPLIT -> act += "split";
            case DOUBLE -> act += "double";
            case SURRENDER -> act += "surrender";
            default -> act += "======== UNKNOWN ERROR ========";
        }
        System.out.println("\n-> You should " + act + ".\n ");
    }

    /**
     * Assume given cards are of the same kind, compute which action to do.
     * 
     * @return {@code SPLIT}, {@code HIT}, or {@code STAND}
     */
    private Action isSplit() {
        int kind = cards.get(0).getKind();
        int dealer = dealerCards.get(0).getValue();
        return switch (kind) {
            case Poker.ACE, 8 -> Action.SPLIT;
            case 2, 3, 7 -> dealer >= 2 && dealer <= 7 ? Action.SPLIT : Action.HIT;
            case 4 -> dealer >= 5 && dealer <= 6 ? Action.SPLIT : Action.HIT;
            case 5 -> dealer >= 2 && dealer <= 9 ? Action.DOUBLE : Action.HIT;
            case 6 -> dealer >= 2 && dealer <= 6 ? Action.SPLIT : Action.HIT;
            case 9 -> dealer == 7 || (dealer >= 10 && dealer <= Poker.ACE_VALUE) ? Action.STAND : Action.SPLIT;
            default -> Action.STAND;
        };
    }

    /**
     * @return {@code true} if player should surrender, {@code false} otherwise
     */
    private boolean isSurrender() {
        int dealer = dealerCards.get(0).getValue();
        if (getTotal() == 15) {
            return dealer == 10;
        } else if (getTotal() == 16) {
            return dealer >= 9 && dealer <= Poker.ACE_VALUE;
        }
        return false;
    }

    /**
     * A soft total is any hand that has an Ace as one of the first two cards,
     * and the Ace count as 11 to start.
     * 
     * @return suggested action
     */
    private Action softTotal() {
        int value = cards.get(0).getValue() == Poker.ACE_VALUE ? cards.get(1).getValue() : cards.get(0).getValue();
        int dealer = dealerCards.get(0).getValue();
        switch (value) {
            case 2, 3:
                return dealer >= 5 && dealer <= 6 ? Action.DOUBLE : Action.HIT;
            case 4, 5:
                return dealer >= 4 && dealer <= 6 ? Action.DOUBLE : Action.HIT;
            case 6:
                return dealer >= 3 && dealer <= 6 ? Action.DOUBLE : Action.HIT;
            case 7:
                if (dealer >= 3 && dealer <= 6) {
                    return Action.DOUBLE;
                } else if (dealer >= 9 && dealer <= Poker.ACE_VALUE) {
                    return Action.HIT;
                } else {
                    return Action.STAND;
                }
            default:
                return Action.STAND;

        }
    }

    /**
     * A hard total is any hand that does not start with an Ace in it,
     * or the Ace has value of 1.
     * 
     * @return suggested action
     */
    private Action hardTotal() {
        int dealer = dealerCards.get(0).getValue();
        int total = getTotal();
        return switch (total) {
            case 9 -> dealer >= 3 && dealer <= 6 ? Action.DOUBLE : Action.HIT;
            case 10 -> dealer >= 2 && dealer <= 9 ? Action.DOUBLE : Action.HIT;
            case 11 -> dealer == Poker.ACE_VALUE ? Action.HIT : Action.DOUBLE;
            case 12 -> dealer >= 4 && dealer <= 6 ? Action.STAND : Action.HIT;
            case 13, 14, 15, 16 -> dealer >= 2 && dealer <= 6 ? Action.STAND : Action.HIT;
            default ->
                // otherwise total >= 17
                total <= 8 ? Action.HIT : Action.STAND;
        };
    }

    /**
     * Calculate hand total using correct value of Ace.
     * 
     * @return total value in hand
     */
    private int getTotal() {
        while (true) {
            int sum = sumOfCards();
            if (sum <= 21) {
                return sum;
            }
            if (hasAce()) {
                changeAce();
            } else {
                return sum;
            }
        }
    }

    /**
     * @return sum of cards
     */
    private int sumOfCards() {
        return cards.stream()
                .mapToInt(Card::getValue)
                .sum();
    }

    /**
     * @return {@code true} if Ace is in hand and has value of 11, {@code false}
     *         otherwise
     */
    private boolean hasAce() {
        return cards.stream()
                .anyMatch(card -> card.getValue() == Poker.ACE_VALUE);
    }

    /**
     * Change the value of Ace from 11 to 1.
     */
    private void changeAce() {
        Optional<Card> ace = cards.stream()
                .filter(card -> card.getValue() == Poker.ACE_VALUE)
                .findFirst();
        ace.ifPresent(Card::changeAceValue);
    }
}
