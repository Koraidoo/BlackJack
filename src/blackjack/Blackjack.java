package blackjack;

import java.util.ArrayList;
import java.util.Scanner;

public class Blackjack {
    public static final int STAND = 0;
    public static final int HIT = 1;
    public static final int SPLIT = 2;
    public static final int DOUBLE = 3;

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
            System.out.print("Enter your cards: ");
            Integer kind = scanner.nextInt();

            if (kind == -1) {
                break;
            }
            
            poker.deal(kind);
            cards.add(new Card(kind));

            kind = scanner.nextInt();
            poker.deal(kind);
            cards.add(new Card(kind));

            System.out.println("Enter dealer's card: ");
            kind = scanner.nextInt();
            poker.deal(kind);
            dealerCards.add(new Card(kind));

            getTotal();
            
            // get result action
            if (cards.get(0).equals(cards.get(1))) {
                // two cards are the same, check for split
                printResult(isSplit());
            } else if (isSurrender()) {
                System.out.println("You should surrender!");
            } else {
                if (hasAce()) {
                    printResult(softTotal());
                } else {
                    printResult(hardTotal());
                }
            }

            cards.clear();
            dealerCards.clear();
            poker.newPack();
        }

        scanner.close();
    }

    /**
     * Prints action in terminal.
     * 
     * @param action
     */
    public void printResult(int action) {
        String act = "";
        switch (action) {
            case STAND:
                act = "stand";
                break;
            case HIT:
                act = "hit";
                break;
            case SPLIT:
                act = "split";
                break;
            case DOUBLE:
                act = "double";
                break;
            default:
                act = "======== UNKNOWN ERROR ========";

        }
        System.out.println("You should " + act + ". ");
    }

    /**
     * Assume given 2 cards are of the same kind, compute which action to do.
     * 
     * @return SPLIT, HIT, or STAND
     */
    public int isSplit() {
        int value = cards.get(0).getValue();
        int dealer = dealerCards.get(0).getValue();
        switch (value) {
            case 11:
                return SPLIT;
            case 2:
            case 3:
                if (dealer >= 2 && dealer <= 7) {
                    return SPLIT;
                } else {
                    return HIT;
                }
            case 4:
                if (dealer >= 5 && dealer <= 6) {
                    return SPLIT;
                } else {
                    return HIT;
                }
            case 5:
                if (dealer >= 2 && dealer <= 9) {
                    return DOUBLE;
                } else {
                    return HIT;
                }
            case 6:
                if (dealer >= 2 && dealer <= 6) {
                    return SPLIT;
                } else {
                    return HIT;
                }
            case 7:
                if (dealer >= 2 && dealer <= 7) {
                    return SPLIT;
                } else {
                    return HIT;
                }
            case 8:
                return SPLIT;
            case 9:
                if (dealer == 7 || (dealer >= 10 && dealer <= 11)) {
                    return STAND;
                } else {
                    return SPLIT;
                }
            case 10:
            default:
                return STAND;
        }

    }

    /**
     * Check if player should surrender.
     * 
     * @return
     */
    public boolean isSurrender() {
        int dealer = dealerCards.get(0).getValue();
        if (getTotal() == 15) {
            if (dealer == 10) {
                return true;
            }
        } else if (getTotal() == 16) {
            if (dealer >= 9 && dealer <= Card.ACE_VALUE) {
                return true;
            }
        }
        return false;
    }

    /**
     * A soft total is any hand that has an Ace as one of the first two cards,
     * and the Ace count as 11 to start.
     * 
     * @return suggested action
     */
    public int softTotal() {
        int value = cards.get(0).getValue() == Card.ACE_VALUE ? cards.get(1).getValue() : cards.get(0).getValue();
        int dealer = dealerCards.get(0).getValue();
        switch (value) {
            case 2:
            case 3:
                if (dealer >= 5 && dealer <= 6) {
                    return DOUBLE;
                } else {
                    return HIT;
                }
            case 4:
            case 5:
                if (dealer >= 4 && dealer <= 6) {
                    return DOUBLE;
                } else {
                    return HIT;
                }
            case 6:
                if (dealer >= 3 && dealer <= 6) {
                    return DOUBLE;
                } else {
                    return HIT;
                }
            case 7:
                if (dealer >= 3 && dealer <= 6) {
                    return DOUBLE;
                } else if (dealer >= 9 && dealer <= Card.ACE_VALUE) {
                    return HIT;
                } else {
                    return STAND;
                }
            case 8:
            case 9:
            default:
                return STAND;

        }
    }

    /**
     * A hard total is any hand that does not start with an Ace in it,
     * or the Ace has value of 1.
     * 
     * @return suggested action
     */
    public int hardTotal() {
        int dealer = dealerCards.get(0).getValue();
        int total = getTotal();
        switch (total) {
            case 9:
                if (dealer >= 3 && dealer <= 6) {
                    return DOUBLE;
                } else {
                    return HIT;
                }
            case 10:
                if (dealer >= 2 && dealer <= 9) {
                    return DOUBLE;
                } else {
                    return HIT;
                }
            case 11:
                if (dealer == Card.ACE_VALUE) {
                    return HIT;
                } else {
                    return DOUBLE;
                }
            case 12:
                if (dealer >= 4 && dealer <= 6) {
                    return STAND;
                } else {
                    return HIT;
                }
            case 13:
            case 14:
            case 15:
            case 16:
                if (dealer >= 2 && dealer <= 6) {
                    return STAND;
                } else {
                    return HIT;
                }
            default:
                if (total <= 8) {
                    return HIT;
                } else {
                    // total >= 17
                    return STAND;
                }
        }
    }

    /**
     * Check if hand is busted.
     * 
     * @return true if total > 21
     */
    public boolean isBusted() {
        if (getTotal() > 21) {
            return true;
        }

        return false;
    }

    /**
     * Calculate hand total using correct value of Ace.
     * 
     * @return value in hand
     */
    public int getTotal() {
        while (true) {
            int sum = sumOfCards();
            if (sum <= 21) {
                return sum;
            } else {
                if (hasAce()) {
                    changeAce();
                } else {
                    return sum;
                }
            }
        }
    }

    /**
     * Calculate sum of cards.
     * 
     * @return
     */
    private int sumOfCards() {
        int sum = 0;
        for (Card card : cards) {
            sum += card.getValue();
        }

        return sum;
    }

    /**
     * Find if Ace is in hand and has value 11.
     * 
     * @return true if Ace has value 11
     */
    private boolean hasAce() {
        for (Card card : cards) {
            if (card.getValue() == Card.ACE_VALUE) {
                return true;
            }
        }
        return false;
    }

    /**
     * Change the value of Ace from 11 to 1.
     */
    private void changeAce() {
        for (Card card : cards) {
            if (card.getValue() == Card.ACE_VALUE) {
                card.changeAceValue();
                return;
            }
        }
    }
}
