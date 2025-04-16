package thewarforged.actions.powerActions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewarforged.powers.IonCoilPower_Warforged;
import thewarforged.powers.IonizedPower_Warforged;

public class ApplyIonizedPowerAction_Warforged extends AbstractGameAction {
    private final AbstractCreature TARGET_CREATURE;
    private final AbstractCreature SOURCE;
    private final int IONIZED_AMOUNT;

    public ApplyIonizedPowerAction_Warforged(AbstractCreature targetCreature, AbstractCreature source, int ionizedAmount) {
        this.TARGET_CREATURE = targetCreature;
        this.SOURCE = source;
        this.IONIZED_AMOUNT = ionizedAmount;
    }

    @Override
    public void update() {
        //Adjust the amount of Ionized applied based on the "amount" on the owner's IonCoilPower, if applicable.
        int adjustedNumIonized = this.IONIZED_AMOUNT;
        AbstractPower ionCoilPowerWarforged = this.SOURCE.getPower(IonCoilPower_Warforged.POWER_ID);
        if (ionCoilPowerWarforged instanceof IonCoilPower_Warforged) {
            adjustedNumIonized += ionCoilPowerWarforged.amount;
        }

        //Then apply the Ionized to the target. Add the action to the top, since this action is meant to be a stand-in
        // for applying Ionized normally, just with extra logic. We don't want it taking longer than necessary to be
        // applied just because there was extra logic to it.
        IonizedPower_Warforged ionizedPower =
                new IonizedPower_Warforged(this.TARGET_CREATURE, this.SOURCE, adjustedNumIonized);
        this.addToTop(new ApplyPowerAction(this.TARGET_CREATURE, this.SOURCE, ionizedPower, adjustedNumIonized));

        this.isDone = true;
    }
}
