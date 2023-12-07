import java.util.Arrays;

public class Hand implements Comparable<Hand> {
    final private char[] cards;
    final String handType;
    final private int bid;
    final private String[] TYPE_ORDER =
            {"HC", "1P", "2P", "3K", "FH", "4K", "5K"}; // high card, one pair, two pair, three of a kind, full house, four of a kind, five of a kind
    final private char[] CARD_ORDER =
            {'J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A'}; // joker, 2, 3, 4, 5, 6, 7, 8, 9, 10, queen, king, ace

    public Hand(String hand, int bid) {
        if (hand.length() != 5) {
            throw new IllegalArgumentException("Hand must be 5 cards");
        }
        this.cards = hand.toCharArray();
        this.handType = getHandType(this.cards);
        this.bid = bid;
    }

    private String getHandType(char[] cards) {
        int jokers = 0;
        int[] cardCounts = new int[13];
        for (char card : cards) {
            if (card == 'J') {
                jokers++;
            } else {
                cardCounts[getCardIndex(card)]++;
            }
        }
        int[] cardCountsSorted = cardCounts.clone();
        Arrays.sort(cardCountsSorted);
        cardCountsSorted[12] += jokers; // add jokers to the highest card count
        if (cardCountsSorted[12] == 5) {
            return "5K";
        } else if (cardCountsSorted[12] == 4) {
            return "4K";
        } else if (cardCountsSorted[12] == 3 && cardCountsSorted[11] == 2) {
            return "FH";
        } else if (cardCountsSorted[12] == 3) {
            return "3K";
        } else if (cardCountsSorted[12] == 2 && cardCountsSorted[11] == 2) {
            return "2P";
        } else if (cardCountsSorted[12] == 2) {
            return "1P";
        } else {
            return "HC";
        }
    }

    private int getCardIndex(char card) {
        for (int i = 0; i < CARD_ORDER.length; i++) {
            if (card == CARD_ORDER[i]) {
                return i;
            }
        }
        throw new IllegalArgumentException("Invalid card");
    }

    public int getBid() {
        return bid;
    }

    @Override
    public int compareTo(Hand other) {
        int thisHandTypeIndex = getHandTypeIndex(this.handType);
        int otherHandTypeIndex = getHandTypeIndex(other.handType);
        if (thisHandTypeIndex != otherHandTypeIndex) {
            return thisHandTypeIndex - otherHandTypeIndex;
        } else {
            return compareHands(this.cards, other.cards);
        }
    }

    private int getHandTypeIndex(String handType) {
        for (int i = 0; i < TYPE_ORDER.length; i++) {
            if (handType.equals(TYPE_ORDER[i])) {
                return i;
            }
        }
        throw new IllegalArgumentException("Invalid hand type");
    }

    private int compareHands(char[] hand1, char[] hand2) {
        for (int i = 0; i < 5; i++) {
            int card1Index = getCardIndex(hand1[i]);
            int card2Index = getCardIndex(hand2[i]);
            if (card1Index != card2Index) {
                return card1Index - card2Index;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return String.format("%s %d", new String(cards), bid);
    }
}
