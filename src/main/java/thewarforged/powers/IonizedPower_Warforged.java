package thewarforged.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewarforged.util.WarforgedUtils;

import static thewarforged.TheWarforgedMod.makeID;

public class IonizedPower_Warforged extends AbstractWarforgedPower {
    public static final String POWER_ID = makeID(IonizedPower_Warforged.class.getSimpleName());
    private static final AbstractPower.PowerType POWER_TYPE = PowerType.DEBUFF;
    private static final boolean IS_TURNED_BASED = false;

    public IonizedPower_Warforged(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURNED_BASED, owner, amount);
        this.source = source;
    }

    private void handleHighVoltage() {
        AbstractPower sourceHighVoltagePower = this.source.getPower(HighVoltagePower_Warforged.POWER_ID);
        if (sourceHighVoltagePower instanceof HighVoltagePower_Warforged) {
            LoseHPAction loseHPAction = new LoseHPAction(
                    this.owner,
                    this.source,
                    (sourceHighVoltagePower.amount * this.amount),
                    AbstractGameAction.AttackEffect.LIGHTNING);
            this.addToBot(loseHPAction);
            sourceHighVoltagePower.flash();
        }
    }

    /**
     * This is called when the power is stacked, meaning the power already existed but has now been added onto
     * (or removed from if the stackAmount is negative).
     * @param stackAmount The change in amount for this power.
     */
    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        //As long as the amount was increased, handle the source's High Voltage power, is applicable.
        if (stackAmount > 0) this.handleHighVoltage();
    }

    /**
     * This runs AFTER the initial application. The amount that was applied will already be on
     * the owner when this runs.
     */
    @Override
    public void onInitialApplication() {
        WarforgedUtils.easyPrint("onInitialApplication");
        this.handleHighVoltage();
    }

    /**
     * Whenever a creature which this power (DEBUFF) deals NORMAL (attack) damage to any other target,
     * half of that damage (rounded up) is also dealt to the attacking creature as THORNS damage.
     * @param info The info regarding the damage being dealt by the attacking creature
     * @param damageAmount The amount of damage being dealt by the attacking creature to its target
     * @param target The target of the attacking creature
     */
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type != DamageInfo.DamageType.NORMAL || target == info.owner) {
            return;
        }

        int ionizedDamageToDeal = MathUtils.floorPositive((float) damageAmount / 2);
        this.flash();

        //I believe that the check for it being 0 is just an abundance of caution, but the base game does it
        // with powers like Vulnerable, so I will imitate them.
        if (this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        } else {
            //Because we are adding these to the top of the queue, and this should happen *after* the
            // damage action, this needs to be added first. Same with the RemoveSpecificPowerAction above.
            this.addToTop(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
        }

        DamageInfo damageInfo = new DamageInfo(this.owner, ionizedDamageToDeal, DamageInfo.DamageType.THORNS);
        DamageAction damageAction = new DamageAction(
                this.owner, damageInfo,
                AbstractGameAction.AttackEffect.LIGHTNING,
                true);
        this.addToTop(damageAction);
    }

    @Override
    public void updateDescription() {
        String str1 = (this.owner == AbstractDungeon.player) ? this.DESCRIPTIONS[0] : this.DESCRIPTIONS[1];
        this.description = str1 + this.DESCRIPTIONS[2];
    }
}
