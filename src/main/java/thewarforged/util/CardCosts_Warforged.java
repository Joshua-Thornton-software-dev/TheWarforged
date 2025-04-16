package thewarforged.util;

/**
 * This class is meant to deobfuscate the base cost values of cards.
 */
public class CardCosts_Warforged {

    //The following few methods are meant to deobfuscate the card's baseCost value.

    /**
     * Ensures that the card's baseCost will be a positive integer.
     * @param cost The cost of the card
     * @return Absolute Value of cost
     */
    public static int POSITIVE_COST(Integer cost) {
        return Math.abs(cost);
    }

    /**
     * Returns the value necessary for a card's baseCost to be 0.
     * @return 0
     */
    public static int ZERO_COST() {
        return 0;
    }

    /**
     * Returns the value necessary for a card's baseCost to be X (uses all remaining energy).
     * @return -1
     */
    public static int X_COST() {
        return -1;
    }

    /**
     * Returns the value necessary for a card's baseCost to be Unplayable (such as status cards).
     * @return -2
     */
    public static int UNPLAYABLE() {
        return -2;
    }
}
