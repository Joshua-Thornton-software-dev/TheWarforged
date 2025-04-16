package thewarforged.cards.skillCards;

import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewarforged.cards.AbstractWarforgedCard;
import thewarforged.character.TheWarforged;
import thewarforged.modifiers.EnergyShieldBlockModifier;
import thewarforged.powers.StableShieldsPower_Warforged;
import thewarforged.util.CardStats;

public class StabilizeShields_Warforged extends AbstractWarforgedCard {
    public static final String ID = makeID(StabilizeShields_Warforged.class.getSimpleName());

    private static final CardStats info = new CardStats(
            TheWarforged.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            CardStats.ZERO_COST()
    );

    private static final int ENERGY_SHIELD_AMOUNT = 6;
    private static final int UPGRADED_ENERGY_SHIELD_AMOUNT_INCREASE = 3;
    private static final int NUM_STABILIZE_SHIELDS_POWER = 1;

    public StabilizeShields_Warforged() {
        super(ID, info);

        setBlock(ENERGY_SHIELD_AMOUNT, UPGRADED_ENERGY_SHIELD_AMOUNT_INCREASE);

        BlockModifierManager.addModifier(this, new EnergyShieldBlockModifier());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.addToBot(new GainBlockAction(player, player, this.block));
        this.addToBot(new ApplyPowerAction(
                player,
                player,
                new StableShieldsPower_Warforged(player, NUM_STABILIZE_SHIELDS_POWER)));
    }
}
