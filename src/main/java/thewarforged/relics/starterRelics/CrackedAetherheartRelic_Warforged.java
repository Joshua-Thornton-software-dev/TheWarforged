package thewarforged.relics.starterRelics;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thewarforged.actions.utilactions.GainEnergyAction_Warforged;
import thewarforged.core.EnergyManager_Warforged;
import thewarforged.relics.AbstractWarforgedRelic;
import thewarforged.util.CardStats;

import static thewarforged.TheWarforgedMod.makeID;

@SuppressWarnings("FieldCanBeLocal")
public class CrackedAetherheartRelic_Warforged extends AbstractWarforgedRelic {
    private static final String NAME = CrackedAetherheartRelic_Warforged.class.getSimpleName();
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.STARTER;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    private AbstractPlayer _player;
    private GameActionManager _actionManager;

    private final int STARTING_DEX = 2;
    private final int STARTING_STR = -2;
    private final int NUM_ENERGY_PER_TIER = 2;

    private int numDexApplied = 0;
    private int numStrApplied = 0;

    private final int MAX_SAFE_ENERGY = 6;
    private final int DAMAGE_PER_ENERGY_PAST_MAX = 1;

    private final int NUM_CARDS_FOR_OVERLOAD = 6;
    private final int NUM_CARDS_FOR_RESET = NUM_CARDS_FOR_OVERLOAD + 1;
    private int numCardsPlayed = 0;

    private final int STARTING_ENERGY_GAIN = 1;
    private final int OVERLOAD_ENERGY_GAIN_MULTIPLIER = 2;
    private int currentEnergyGain = STARTING_ENERGY_GAIN;

    public CrackedAetherheartRelic_Warforged() {
        //The second argument, NAME, is the name of both this relic and the image for this relic.
        super(ID, NAME, RARITY, LANDING_SOUND);
    }

    private AbstractPlayer player() {
        if (this._player == null) this._player = AbstractDungeon.player;
        return this._player;
    }

    private GameActionManager actionManager() {
        if (this._actionManager == null) this._actionManager = AbstractDungeon.actionManager;
        return this._actionManager;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0]
                + DESCRIPTIONS[1]
                + DESCRIPTIONS[2]
                + this.NUM_CARDS_FOR_OVERLOAD
                + DESCRIPTIONS[3]
                + DESCRIPTIONS[4]
                + DESCRIPTIONS[5]
                + this.MAX_SAFE_ENERGY
                + DESCRIPTIONS[6];
    }

    @Override
    public void atPreBattle() {
        this.ensureCustomEnergyManager();
        this.setPowers(this.STARTING_DEX, this.STARTING_STR);
    }

    @Override
    public void atTurnStart() {
        //Reset the overload counter and energyGain.
        this.numCardsPlayed = 0;
        this.currentEnergyGain = STARTING_ENERGY_GAIN;
    }

    /**
     * This happens BEFORE the card's effects (before `use`).
     * @param card The card that the player played.
     * @param monster The monster that was targeted by the card.
     */
    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster monster) {

    }

    /**
     * This happens AFTER a card's effects (after `use`).
     * @param targetCard The card that was used
     * @param useCardAction The action that was on the stack to be used in the previous use action.
     */
    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        //A card was played! Track the number of cards played for the overload mechanic.
        this.runawayAetherheart_IncreaseCharge(targetCard, useCardAction);
        //If the number of cards played is NOT back to 0 (after being tracked and possibly reset above),
        if (this.numCardsPlayed != 0) {
            // then this card was NOT the overload (copy) card, and therefore counts for energy
            // gain/aetherburn. Delay the aetherburn for X_COST cards, though.
            boolean shouldDelayAetherburn = (targetCard.cost == CardStats.X_COST());
            this.runawayAetherheart_GainEnergy(shouldDelayAetherburn);
        }
    }

    /**
     * The aetherheart is spinning wildly and generating dangerous amounts of energy!
     * The character gains energy. The amount gained increases dramatically as they play
     * more and more cards in the same turn.
     */
    private void runawayAetherheart_GainEnergy(boolean shouldDelayAetherburn) {

        //This action will trigger aetherburn after the energy is gained.
        GainEnergyAction_Warforged gainEnergyAction =
                new GainEnergyAction_Warforged(this.currentEnergyGain, shouldDelayAetherburn);
        this.addToTop(gainEnergyAction);
    }

    /**
     * Handles the HP loss triggered by having too much energy after playing a card. The energy gained by
     * playing a card via this relic is included in this calculation, and this method is called by that custom
     * GainEnergyAction_Warforged after the energy is applied. Once aetherburn is handled, this method calls
     * runawayAetherheart_BalancePowers() to adjust the character's Dex/Str according to the new energy level.
     */
    public void runawayAetherheart_Aetherburn() {
        // Determine how much energy the character has past the maximum safe amount.
        int currEnergy = EnergyPanel.getCurrentEnergy();
        int aetherburn = currEnergy - MAX_SAFE_ENERGY;
        // If there was enough energy to cause damage, cause the character to lose that much HP.
        if (aetherburn > 0) {
            LoseHPAction aetherburnAction = new LoseHPAction(
                    this.player(),
                    this.player(),
                    aetherburn,
                    AbstractGameAction.AttackEffect.LIGHTNING);
            this.addToBot(aetherburnAction);
        }

        //After handling aetherburn, balance the character's Dex/Str based on new energy levels.
        this.runawayAetherheart_BalancePowers();
    }

    /**
     * Balances the Dex/Str on the Warforged according to the current energy tier. For every tier,
     * Dexterity reduces by 1 and Strength increases by 1. When energy is lost, rewinds those differences.
     */
    private void runawayAetherheart_BalancePowers() {
        int newDex;
        int newStr;

        int currEnergy = EnergyPanel.getCurrentEnergy();
        int currEnergyTier = MathUtils.floor((float) (currEnergy / this.NUM_ENERGY_PER_TIER));

        newDex = this.STARTING_DEX - currEnergyTier;
        newStr = this.STARTING_STR + currEnergyTier;

        this.setPowers(newDex, newStr);
    }

    /**
     * Brings the character's Dex and Str up/down to the new values if necessary. Keeps track of Dex and Str that
     *  have already been applied (positive and negative) so ensure Dex/Str buffs/debuffs from other sources are
     *  not affected. Loss of Dex/Str in this manner is not considered a debuff in order to avoid odd
     *  interactions with Artifact.
     * @param newDex The amount of Dexterity that should be applied to the character from this relic
     *               (ignores Dexterity from other sources)
     * @param newStr The amount of Strength that should be applied to the character from this relic
     *               (ignores Strength from other sources)
     */
    private void setPowers(int newDex, int newStr) {
        //Get the current DexterityPower on the player, if any.
        AbstractPower existingDexPower = this.player().getPower("Dexterity");
        //Determine the difference between the current amount of Dex applied to the character via this relic and
        // new amount that should be applied instead.
        int dexDiff = newDex - this.numDexApplied;
        //If the amount of Dex needs to drop, and they already have some Dex (either positive or negative),
        if (dexDiff < 0 && existingDexPower != null) {
            // then reduce the character's Dex power by that much. This action needs a positive number to know
            // how much to reduce the power by, but dexDiff is guaranteed to be negative, so we make it positive.
            ReducePowerAction reducePowerAction =
                    new ReducePowerAction(this.player(), this.player(), existingDexPower, (dexDiff * -1));
            this.addToBot(reducePowerAction);
            // Else, either no change is required, or the character does not have any Dex at the moment.
            // If a change is Dex is required,
        } else if (dexDiff != 0) {
            // then create a change to the current Dex power on the character to apply.
            DexterityPower newDexPower = new DexterityPower(this.player(), dexDiff);
            //Regardless of whether this is negative, count it as a BUFF so avoid odd interactions with Artifact.
            newDexPower.type = AbstractPower.PowerType.BUFF;
            ApplyPowerAction powerAction = new ApplyPowerAction(this.player(), this.player(), newDexPower, dexDiff);
            this.addToBot(powerAction);
        }
        //Track the amount of Dex (positive, negative, or 0) that is currently applied to the character via this relic.
        this.numDexApplied += dexDiff;

        //Repeat the same as above, but for Str.
        AbstractPower existingStrPower = this.player().getPower("Strength");
        int strDiff = newStr - this.numStrApplied;
        if (strDiff < 0 && existingStrPower != null) {
            ReducePowerAction reducePowerAction =
                    new ReducePowerAction(this.player(), this.player(), existingStrPower, (strDiff * -1));
            this.addToBot(reducePowerAction);
        } else if (strDiff != 0) {
            StrengthPower newStrPower = new StrengthPower(this.player(), strDiff);
            newStrPower.type = AbstractPower.PowerType.BUFF;
            ApplyPowerAction powerAction = new ApplyPowerAction(this.player(), this.player(), newStrPower, strDiff);
            this.addToBot(powerAction);
        }
        this.numStrApplied += strDiff;
    }

    /**
     * The aetherheart's energy builds toward an inevitable release of power!
     * Tracks the number of cards played and, once it reaches a certain points, overloads the last card.
     * Then the amount of energy gained per card played increases (multiplicative).
     * @param targetCard The card that was last played by the character
     */
    private void runawayAetherheart_IncreaseCharge(AbstractCard targetCard, UseCardAction useCardAction) {
        this.numCardsPlayed++;
        if (numCardsPlayed == this.NUM_CARDS_FOR_OVERLOAD) {
            //Overload this card and double the amount of energy gained per card played.
            this.runawayAetherheart_Overload(targetCard, useCardAction);
            this.currentEnergyGain *= this.OVERLOAD_ENERGY_GAIN_MULTIPLIER;
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
    private void runawayAetherheart_Overload(AbstractCard targetCard, UseCardAction useCardAction) {
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

    /**
     * Ensure that the character is using the custom energy manager meant for the Warforged, and set its energy
     * gained per turn to 0 as the relic intends. I allowed the default to be 3 just in case the Warforged was ever
     * played without this relic for some reason.
     */
    private void ensureCustomEnergyManager() {
        if (!(this.player().energy instanceof EnergyManager_Warforged)) {
            this.player().energy = new EnergyManager_Warforged(EnergyPanel.getCurrentEnergy());
        }
        this.player().energy.energyMaster = 0;
        this.player().energy.energy = 0;
    }
}
