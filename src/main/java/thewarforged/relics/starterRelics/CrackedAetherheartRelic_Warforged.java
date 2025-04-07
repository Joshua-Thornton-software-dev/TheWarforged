package thewarforged.relics.starterRelics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thewarforged.actions.utilactions.GainEnergyAction_Warforged;
import thewarforged.relics.AbstractWarforgedRelic;
import thewarforged.util.CardStats;

import static thewarforged.TheWarforgedMod.makeID;

@SuppressWarnings("FieldCanBeLocal")
public class CrackedAetherheartRelic_Warforged extends AbstractWarforgedRelic {
    private static final String NAME = CrackedAetherheartRelic_Warforged.class.getSimpleName();
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.STARTER;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    private static final int BASE_MAX_SAFE_ENERGY = 6;
    private static final int BASE_DAMAGE_PER_EXTRA_ENERGY = 1;
    public static int maxSafeEnergy = BASE_MAX_SAFE_ENERGY;
    public static int damagePerExtraEnergy = BASE_DAMAGE_PER_EXTRA_ENERGY;

    private final int NUM_CARDS_FOR_OVERLOAD = 6;
    private final int NUM_CARDS_FOR_RESET = NUM_CARDS_FOR_OVERLOAD + 1;
    private int numCardsPlayed = 0;

    private static final int STARTING_ENERGY_GAIN = 1;
    private static final int OVERLOAD_ENERGY_GAIN_MULTIPLIER = 2;
    public static int currentEnergyGain = STARTING_ENERGY_GAIN;

    private GameActionManager actionManager() {
        return AbstractDungeon.actionManager;
    }

    public CrackedAetherheartRelic_Warforged() {
        //The second argument, NAME, is the name of both this relic and the image for this relic.
        super(ID, NAME, RARITY, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart() {
        //Reset the overload counter and energyGain.
        this.numCardsPlayed = 0;
        currentEnergyGain = STARTING_ENERGY_GAIN;
    }

    /**
     * This happens AFTER a card's effects (after `use`).
     * @param targetCard The card that was used
     * @param useCardAction The action that was on the stack to be used in the previous use action.
     */
    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        //A card was played! Track the number of cards played for the overload mechanic.
        this.volatileAether_IncreaseCharge(targetCard, useCardAction);
        //If the number of cards played is NOT back to 0 (after being tracked and possibly reset above),
        if (this.numCardsPlayed != 0) {
            // then this card was NOT the overload (copy) card, and therefore counts for energy
            // gain/aetherburn. Delay the aetherburn for X_COST cards, though.
            boolean shouldDelayAetherburn = (targetCard.cost == CardStats.X_COST());
            this.volatileAether_GainEnergy(shouldDelayAetherburn);
        }
    }

    /**
     * The aetherheart is spinning wildly and generating dangerous amounts of energy!
     * The character gains energy. The amount gained increases dramatically as they play
     * more and more cards in the same turn.
     */
    private void volatileAether_GainEnergy(boolean shouldDelayAetherburn) {

        //This action will trigger aetherburn after the energy is gained.
        GainEnergyAction_Warforged gainEnergyAction =
                new GainEnergyAction_Warforged(currentEnergyGain, shouldDelayAetherburn);
        this.addToTop(gainEnergyAction);
    }

    /**
     * Handles the HP loss triggered by having too much energy after playing a card. The energy gained by
     * playing a card via this relic is included in this calculation, and this method is called by that custom
     * GainEnergyAction_Warforged after the energy is applied.
     */
    public void volatileAether_Aetherburn() {
        // Determine how much energy the character has past the maximum safe amount.
        int currEnergy = EnergyPanel.getCurrentEnergy();
        int aetherburn = currEnergy - BASE_MAX_SAFE_ENERGY;
        // If there was enough energy to cause damage, cause the character to lose that much HP.
        if (aetherburn > 0) {
            LoseHPAction aetherburnAction = new LoseHPAction(
                    this.player(),
                    this.player(),
                    aetherburn,
                    AbstractGameAction.AttackEffect.LIGHTNING);
            this.addToBot(aetherburnAction);
        }
    }

    /**
     * The aetherheart's energy builds toward an inevitable release of power!
     * Tracks the number of cards played and, once it reaches a certain points, overloads the last card.
     * Then the amount of energy gained per card played increases (multiplicative).
     * @param targetCard The card that was last played by the character
     */
    private void volatileAether_IncreaseCharge(AbstractCard targetCard, UseCardAction useCardAction) {
        this.numCardsPlayed++;
        if (numCardsPlayed == this.NUM_CARDS_FOR_OVERLOAD) {
            //Overload this card and double the amount of energy gained per card played.
            this.volatileAether_Overload(targetCard, useCardAction);
            currentEnergyGain *= OVERLOAD_ENERGY_GAIN_MULTIPLIER;
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
        CardQueueItem cardQueueItem = new CardQueueItem(tempCard, monster, targetCard.energyOnUse, true, true);
        this.actionManager().addCardQueueItem(cardQueueItem, true);
    }
}
