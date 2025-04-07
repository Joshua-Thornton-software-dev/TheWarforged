package thewarforged.relics.uncommonRelics;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewarforged.relics.AbstractWarforgedRelic;

import static thewarforged.TheWarforgedMod.makeID;

public class QuantumTransistorRelic_Warforged extends AbstractWarforgedRelic {
    private static final String NAME = QuantumTransistorRelic_Warforged.class.getSimpleName();
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    private final int NUM_CARDS_FOR_OVERLOAD = 6;
    @SuppressWarnings("FieldCanBeLocal")
    private final int NUM_CARDS_FOR_RESET = NUM_CARDS_FOR_OVERLOAD + 1;
    private int numCardsPlayed = 0;

    public QuantumTransistorRelic_Warforged() {
        super(ID, NAME, RARITY, LANDING_SOUND);
    }

    private GameActionManager actionManager() {
        return AbstractDungeon.actionManager;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart() {
        //Reset the overload counter and energyGain.
        this.numCardsPlayed = 0;
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        this.numCardsPlayed++;
        if (numCardsPlayed == this.NUM_CARDS_FOR_OVERLOAD) {
            //Overload this card and double the amount of energy gained per card played.
            this.volatileAether_Overload(targetCard, useCardAction);
            //If this is the card played immediately after overloading a card,
        } else if (numCardsPlayed == this.NUM_CARDS_FOR_RESET) {
            // then this is the overloaded card's copy. It does not count towards the next overload. Reset.
            this.numCardsPlayed = 0;
        }
    }

    /**
     * The aetherheart cannot hold the energy any longer. It demands to be released.
     * Duplicates the target card.
     * @param targetCard The card to duplicate
     */
    private void volatileAether_Overload(AbstractCard targetCard, UseCardAction useCardAction) {
        //This follows the pattern shown in EchoPower.class, which doubles a card the same way overload should.
        this.flash();
        //Get the monster target of the card being copied, if one exists.
        AbstractMonster monster = null;
        if (useCardAction.target != null) {
            monster = (AbstractMonster)useCardAction.target;
        }

        //Create the copy.
        AbstractCard tempCard = targetCard.makeSameInstanceOf();
        //Then place it into... limbo? I have no idea, but it's what EchoPower does. I will trust it for now.
        this.player().limbo.addToBottom(tempCard);
        //The rest of this section seems to be setting variables for screen placement/animation.
        tempCard.current_x = targetCard.current_x;
        tempCard.current_y = targetCard.current_y;
        tempCard.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        tempCard.target_y = (float) Settings.HEIGHT / 2.0F;

        //Calculate damage if there is a monster to calculate against. I assume this does nothing if the card
        // being copied targets a monster without damaging it. I haven't looked into it yet.
        if (monster != null) {
            tempCard.calculateCardDamage(monster);
        }

        //Ensure that this copy will disappear after being played, rather than added to the dungeon deck.
        targetCard.purgeOnUse = true;
        CardQueueItem cardQueueItem =
                new CardQueueItem(tempCard, monster, targetCard.energyOnUse, true, true);
        this.actionManager().addCardQueueItem(cardQueueItem, true);
    }
}
