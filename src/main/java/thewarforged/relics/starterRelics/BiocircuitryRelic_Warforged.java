package thewarforged.relics.starterRelics;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thewarforged.actions.utilactions.BiocircuitryBalancePowersAction_Warforged;
import thewarforged.relics.AbstractWarforgedRelic;

import static thewarforged.TheWarforgedMod.makeID;

public class BiocircuitryRelic_Warforged extends AbstractWarforgedRelic {
    private static final String NAME = BiocircuitryRelic_Warforged.class.getSimpleName();
    public static final String ID = makeID(NAME);
    private static final AbstractRelic.RelicTier RARITY = AbstractRelic.RelicTier.STARTER;
    private static final AbstractRelic.LandingSound LANDING_SOUND = AbstractRelic.LandingSound.CLINK;

    //These base bonuses are what the character would receive if at 0 energy.
    private final int BASE_DEX_MODIFIER = 2;
    private final int BASE_STR_MODIFIER = -2;
    //The number of energy past 0 required to alter the bonuses (-1 Dex, +1 Str per tier).
    private final int NUM_ENERGY_PER_TIER = 2;

    //The number of bonuses to either power granted via this stat this combat.
    private int numDexApplied = 0;
    private int numStrApplied = 0;

    private CrackedAetherheartRelic_Warforged _crackedAetherheartRelic;

    private CrackedAetherheartRelic_Warforged crackedAetherheartRelic() {
        if (this._crackedAetherheartRelic == null) {
            this._crackedAetherheartRelic =
                    (CrackedAetherheartRelic_Warforged) this.player().getRelic(CrackedAetherheartRelic_Warforged.ID);
        }
        return this._crackedAetherheartRelic;
    }

    public BiocircuitryRelic_Warforged() {
        //The second argument, NAME, is the name of both this relic and the image for this relic.
        super(ID, NAME, RARITY, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0]
                + this.BASE_DEX_MODIFIER
                + DESCRIPTIONS[1]
                + this.BASE_STR_MODIFIER
                + DESCRIPTIONS[2]
                + DESCRIPTIONS[3]
                + this.NUM_ENERGY_PER_TIER
                + DESCRIPTIONS[4];
    }

    @Override
    public void atBattleStart() {
        //Start of a new battle. We of course have not applied anything yet.
        this.numDexApplied = 0;
        this.numStrApplied = 0;
    }

    /**
     * This happens AFTER a card's effects (after `use`).
     * @param targetCard The card that was used
     * @param useCardAction The action that was on the stack to be used in the previous use action.
     */
    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        //Logic moved to onEnergyChanged instead. That fixed the bugs.
    }

    @Override
    public void onEnergyChanged() {
        addToBot(new BiocircuitryBalancePowersAction_Warforged());
    }

    /**
     * Balances the Dex/Str on the Warforged according to the current energy tier. For every tier,
     *      Dexterity reduces by 1 and Strength increases by 1. When energy is lost, rewinds those
     *      differences. This gets triggered by the BiocircuitryBalancePowerAction_Warforged, which is
     *      created whenever the character's energy changes.
     */
    public void powerFluctuations_BalancePowers() {
        int newDex;
        int newStr;

        int currEnergy = EnergyPanel.getCurrentEnergy();
        //Each tier represents a full set of {NUM_ENERGY_PER_TIER} energy above 0.
        int currEnergyTier = MathUtils.floor((float) (currEnergy / this.NUM_ENERGY_PER_TIER));

        newDex = this.BASE_DEX_MODIFIER - currEnergyTier;
        newStr = this.BASE_STR_MODIFIER + currEnergyTier;

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

        //If there is a difference that needs to be applied,
        if (dexDiff != 0) {
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

        if (strDiff != 0) {
            StrengthPower newStrPower = new StrengthPower(this.player(), strDiff);
            newStrPower.type = AbstractPower.PowerType.BUFF;
            ApplyPowerAction powerAction = new ApplyPowerAction(this.player(), this.player(), newStrPower, strDiff);
            this.addToBot(powerAction);
        }

        this.numStrApplied += strDiff;
    }
}
