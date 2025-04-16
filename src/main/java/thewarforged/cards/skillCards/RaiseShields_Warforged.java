package thewarforged.cards.skillCards;

import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewarforged.cards.AbstractWarforgedCard;
import thewarforged.character.TheWarforged;
import thewarforged.modifiers.EnergyShieldBlockModifier;
import thewarforged.util.CardCosts_Warforged;
import thewarforged.util.CardStats;

public class RaiseShields_Warforged extends AbstractWarforgedCard {
    public static final String ID = makeID(RaiseShields_Warforged.class.getSimpleName());

    private static final CardStats info = new CardStats(
            TheWarforged.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.BASIC,
            CardTarget.SELF,
            CardCosts_Warforged.POSITIVE_COST(2)
    );

    private static final int ENERGY_SHIELD_AMOUNT = 4;
    private static final int UPGRADED_ENERGY_SHIELD_AMOUNT_INCREASE = 1;

    private static final int REPEAT_GAIN_AMOUNT = 3;
    private static final int UPGRADED_REPEAT_GAIN_AMOUNT_INCREASE = 1;

    public RaiseShields_Warforged() {
        super(ID, info);

        //We can just use block directly for normal effects like Dexterity and Frail, since we are using a
        // block modifier.
        setBlock(ENERGY_SHIELD_AMOUNT, UPGRADED_ENERGY_SHIELD_AMOUNT_INCREASE);
        //We use magicNumber here to represent the number of times that the EnergyShield is gained.
        setMagic(REPEAT_GAIN_AMOUNT, UPGRADED_REPEAT_GAIN_AMOUNT_INCREASE);
        //Set up this card to use the EnergyShieldBlockModifier instead of normal block. Now, adding a normal
        // GainBlockAction to the queue will automatically use EnergyShieldBlockModifier instead of normal block
        // because that modifier is set to automatically bind.
        BlockModifierManager.addModifier(this, new EnergyShieldBlockModifier());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        for (int i = 0; i < this.magicNumber; ++i) {
            this.addToBot(new GainBlockAction(player, player, this.block));
        }
    }
}
