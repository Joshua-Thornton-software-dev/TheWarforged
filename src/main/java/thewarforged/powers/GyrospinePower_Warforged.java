package thewarforged.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import thewarforged.util.GeneralUtils;

import static thewarforged.TheWarforgedMod.makeID;

public class GyrospinePower_Warforged extends AbstractWarforgedPower {
    public static final String POWER_ID = makeID(GyrospinePower_Warforged.class.getSimpleName());
    private static final AbstractPower.PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURNED_BASED = false;

    private final int _regenOnTrigger;

    public GyrospinePower_Warforged(AbstractCreature owner, int regenOnTrigger) {
        super(POWER_ID, POWER_TYPE, IS_TURNED_BASED, owner, -1);
        this._regenOnTrigger = regenOnTrigger;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = this.DESCRIPTIONS[0]
            + this._regenOnTrigger
            + this.DESCRIPTIONS[1];
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        GeneralUtils.easyPrint("wasHPLost of " + this.getClass().getSimpleName());
        float newHealth = this.owner.currentHealth - damageAmount;
        float halfHealth = this.owner.maxHealth / 2.0F;
        if (newHealth <= halfHealth) {
            GeneralUtils.easyPrint("is at or below half health");
            this.addToBot(
                    new ApplyPowerAction(
                            this.owner,
                            this.owner,
                            new RegenPower(this.owner, this._regenOnTrigger)));
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }
}
