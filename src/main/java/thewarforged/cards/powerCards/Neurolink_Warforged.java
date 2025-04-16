package thewarforged.cards.powerCards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewarforged.cards.AbstractWarforgedCard;
import thewarforged.character.TheWarforged;
import thewarforged.powers.NeurolinkPower_Warforged;
import thewarforged.util.CardCosts_Warforged;
import thewarforged.util.CardStats;

public class Neurolink_Warforged extends AbstractWarforgedCard {
    public static final String ID = makeID(Neurolink_Warforged.class.getSimpleName());

    private static final CardStats info = new CardStats(
            TheWarforged.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            CardCosts_Warforged.POSITIVE_COST(4)
    );

    //The amount of Energy Shield that will be gained when attacking an attacking enemy.
    private static final int ENERGY_SHIELD_GAIN = 3;
    //How much the above number increases on upgrade.
    private static final int UPGRADED_ENERGY_SHIELD_GAIN_INCREASE = 2;

    public Neurolink_Warforged() {
        super(ID, info);
        this.isReplacementCard = true;

        setMagic(ENERGY_SHIELD_GAIN, UPGRADED_ENERGY_SHIELD_GAIN_INCREASE);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.addToBot(new ApplyPowerAction(
                player,
                player,
                new NeurolinkPower_Warforged(this.magicNumber),
                this.magicNumber));
    }
}
