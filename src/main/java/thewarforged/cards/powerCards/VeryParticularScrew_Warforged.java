package thewarforged.cards.powerCards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import thewarforged.cards.AbstractWarforgedCard;
import thewarforged.character.TheWarforged;
import thewarforged.util.CardStats;

public class VeryParticularScrew_Warforged extends AbstractWarforgedCard {
    public static final String ID = makeID(VeryParticularScrew_Warforged.class.getSimpleName());

    private static final CardStats info = new CardStats(
            TheWarforged.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.COMMON,
            CardTarget.SELF,
            CardStats.ZERO_COST()
    );

    private static final int METALLICIZE_AMOUNT = 2;
    private static final int UPGRADED_METALLICIZE_AMOUNT_INCREASE = 1;

    public VeryParticularScrew_Warforged() {
        super(ID, info);
        this.isReplacementCard = true;

        setMagic(METALLICIZE_AMOUNT, UPGRADED_METALLICIZE_AMOUNT_INCREASE);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.addToBot(new ApplyPowerAction(
                player,
                player,
                new MetallicizePower(player, this.magicNumber)));
    }
}
