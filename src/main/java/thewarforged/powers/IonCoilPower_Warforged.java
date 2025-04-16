package thewarforged.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static thewarforged.TheWarforgedMod.makeID;

public class IonCoilPower_Warforged extends AbstractWarforgedPower {
    public static final String POWER_ID = makeID(IonCoilPower_Warforged.class.getSimpleName());
    private static final AbstractPower.PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURNED_BASED = false;

    /**
     * Constructor.
     * @param owner The creature to whom the power is applied
     * @param additionalIonized The number of additional ionized to apply when the owner applies
     *                          Ionizes to another creature
     */
    public IonCoilPower_Warforged(AbstractCreature owner, int additionalIonized) {
        super(POWER_ID, POWER_TYPE, IS_TURNED_BASED, owner, additionalIonized);
    }

    @Override
    public void updateDescription() {
        this.description = this.DESCRIPTIONS[0]
                + this.amount
                + this.DESCRIPTIONS[1];
    }
}
