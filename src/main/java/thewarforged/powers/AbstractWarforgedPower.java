package thewarforged.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public class AbstractWarforgedPower extends BasePower {


    public AbstractWarforgedPower(String id, PowerType powerType, boolean isTurnBased, AbstractCreature owner, int amount) {
        this(id, powerType, isTurnBased, owner, null, amount);
    }
    public AbstractWarforgedPower(String id, PowerType powerType, boolean isTurnBased, AbstractCreature owner, AbstractCreature source, int amount) {
        this(id, powerType, isTurnBased, owner, source, amount, true);
    }
    public AbstractWarforgedPower(String id, PowerType powerType, boolean isTurnBased, AbstractCreature owner, AbstractCreature source, int amount, boolean initDescription) {
        super(id, powerType, isTurnBased, owner, source, amount, initDescription, true);
    }

    //Convenience methods for setting amount without having to worry about canGoNegative, min, and max for every power.
    public void setAmount(float newAmount) {
        setAmount(newAmount, 999, -999);
    }

    public void setAmount(float newAmount, int max) {
        setAmount(newAmount, max, -999);
    }

    public void setAmount(float newAmount, int max, int min) {
        if (newAmount < 0 && !this.canGoNegative) {
            this.amount = (int) Math.max(newAmount, min);
        } else {
            this.amount = (int) Math.min(newAmount, max);
        }
    }
}
