package thewarforged.relics.starterRelics;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thewarforged.relics.AbstractWarforgedRelic;

import static thewarforged.TheWarforgedMod.makeID;

public class BiocircuitryRelic_Warforged extends AbstractWarforgedRelic {
    private static final String NAME = BiocircuitryRelic_Warforged.class.getSimpleName();
    public static final String ID = makeID(NAME);
    private static final AbstractRelic.RelicTier RARITY = AbstractRelic.RelicTier.STARTER;
    private static final AbstractRelic.LandingSound LANDING_SOUND = AbstractRelic.LandingSound.CLINK;

    private final int STARTING_DEX = 2;
    private final int STARTING_STR = -2;
    private final int NUM_ENERGY_PER_TIER = 2;

    private int numDexApplied = 0;
    private int numStrApplied = 0;

    private AbstractPlayer _player;

    private AbstractPlayer player() {
        if (this._player == null) this._player = AbstractDungeon.player;
        return this._player;
    }

    public BiocircuitryRelic_Warforged() {
        //The second argument, NAME, is the name of both this relic and the image for this relic.
        super(ID, NAME, RARITY, LANDING_SOUND);
    }

    @Override
    public void atPreBattle() {
        this.setPowers(this.STARTING_DEX, this.STARTING_STR);
    }

    /**
     * Balances the Dex/Str on the Warforged according to the current energy tier. For every tier,
     * Dexterity reduces by 1 and Strength increases by 1. When energy is lost, rewinds those differences.
     */
    public void powerFluctuations_BalancePowers() {
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
}
