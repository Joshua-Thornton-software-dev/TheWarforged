package thewarforged.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static thewarforged.TheWarforgedMod.makeID;

public class HighVoltagePower_Warforged extends AbstractWarforgedPower {
    public static final String POWER_ID = makeID(HighVoltagePower_Warforged.class.getSimpleName());
    private static final AbstractPower.PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURNED_BASED = false;

    public HighVoltagePower_Warforged(AbstractCreature owner, int hpLossPerIonized) {
        super(POWER_ID, POWER_TYPE, IS_TURNED_BASED, owner, hpLossPerIonized);
    }

    @Override
    public void updateDescription() {
        this.description = this.DESCRIPTIONS[0]
                + this.amount
                + this.DESCRIPTIONS[1];
    }
}
