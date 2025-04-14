package thewarforged.powers;

import com.evacipated.cardcrawl.mod.stslib.actions.common.GainCustomBlockAction;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModContainer;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewarforged.modifiers.EnergyShieldBlockModifier;
import thewarforged.util.WarforgedUtils;

import static thewarforged.TheWarforgedMod.makeID;

public class NeurolinkPower_Warforged extends AbstractWarforgedPower {
    public static final String POWER_ID = makeID(NeurolinkPower_Warforged.class.getSimpleName());
    private static final PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURNED_BASED = false;

    private static BlockModContainer blockModContainer = null;

    private static AbstractPlayer player() { return AbstractDungeon.player; }

    public NeurolinkPower_Warforged(int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURNED_BASED, player(), amount);

        EnergyShieldBlockModifier energyShieldBlock = new EnergyShieldBlockModifier();
        blockModContainer = new BlockModContainer(this, energyShieldBlock);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = this.DESCRIPTIONS[0]
            + this.amount
            + this.DESCRIPTIONS[1];
    }

    /**
     * Every time NORMAL damage is applied to a monster from the player, if that monster intents to attack,
     * then the player receives Energy Shield.
     * @param info Information on the damage being dealt to the target
     * @param damageAmount The amount of damage being dealt to the target
     * @param target The creature being attacked
     */
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type != DamageInfo.DamageType.NORMAL || !(target instanceof AbstractMonster)) return;
        //If the creature being attacked is a monster and that monster is attacking (or attacking & something),
        if (WarforgedUtils.isMonsterAttacking((AbstractMonster) target)) {
            // then we gain some Energy Shield.
            GainCustomBlockAction blockAction =
                    new GainCustomBlockAction(blockModContainer, player(), this.amount);
            this.addToBot(blockAction);
            this.flash();
        }
    }
}
