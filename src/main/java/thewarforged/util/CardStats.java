package thewarforged.util;

import com.megacrit.cardcrawl.cards.AbstractCard;

public class CardStats {
    public final int baseCost;
    public final AbstractCard.CardType cardType;
    public final AbstractCard.CardTarget cardTarget;
    public final AbstractCard.CardRarity cardRarity;
    public final AbstractCard.CardColor cardColor;

    public CardStats(AbstractCard.CardColor cardColor, AbstractCard.CardType cardType, AbstractCard.CardRarity cardRarity, AbstractCard.CardTarget cardTarget, int baseCost)
    {
        this.baseCost = baseCost;
        this.cardType = cardType;
        this.cardTarget = cardTarget;
        this.cardRarity = cardRarity;
        this.cardColor = cardColor;
    }

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