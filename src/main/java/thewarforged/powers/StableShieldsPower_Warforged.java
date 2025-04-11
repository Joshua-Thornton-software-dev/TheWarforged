package thewarforged.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static thewarforged.TheWarforgedMod.makeID;

public class StableShieldsPower_Warforged extends AbstractWarforgedPower {
    public static final String POWER_ID = makeID(GyrospinePower_Warforged.class.getSimpleName());
    private static final AbstractPower.PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURNED_BASED = true;

    public StableShieldsPower_Warforged(AbstractCreature owner, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURNED_BASED, owner, amount);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public void atEndOfRound() {
        if (this.amount == 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
        }

    }
}
