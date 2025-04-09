package thewarforged.cards.powerCards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewarforged.cards.AbstractWarforgedCard;
import thewarforged.character.TheWarforged;
import thewarforged.powers.GyrospinePower_Warforged;
import thewarforged.util.CardStats;

@SuppressWarnings("FieldCanBeLocal")
public class Gyrospine_Warforged extends AbstractWarforgedCard {
    public static final String ID = makeID(Gyrospine_Warforged.class.getSimpleName());

    private final int REGEN_ON_TRIGGER = 4;
    private final int UPGRADED_REGEN_ON_TRIGGER_INCREASE = 0;

    private static final CardStats info = new CardStats(
            TheWarforged.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            CardStats.POSITIVE_COST(2)
    );

    public Gyrospine_Warforged() {
        super(ID, info);
        this.isReplacementCard = true;

        setMagic(this.REGEN_ON_TRIGGER, this.UPGRADED_REGEN_ON_TRIGGER_INCREASE);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        for(AbstractPower pow : player.powers) {
            if (pow.ID.equals(GyrospinePower_Warforged.POWER_ID)) {
                return;
            }
        }

        this.addToBot(
                new ApplyPowerAction(
                        player,
                        player,
                        new GyrospinePower_Warforged(player, this.magicNumber)));
    }
}
