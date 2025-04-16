package thewarforged.cards.powerCards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewarforged.cards.AbstractWarforgedCard;
import thewarforged.character.TheWarforged;
import thewarforged.powers.HighVoltagePower_Warforged;
import thewarforged.powers.IonCoilPower_Warforged;
import thewarforged.util.CardStats;

public class IonCoil_Warforged extends AbstractWarforgedCard {
    public static final String ID = makeID(IonCoil_Warforged.class.getSimpleName());

    private static final CardStats info = new CardStats(
            TheWarforged.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            CardStats.POSITIVE_COST(2)
    );

    private static final int ADDITIONAL_IONIZED = 1;
    private static final int UPGRADED_ADDITIONAL_IONIZED_INCREASE = 0;

    private static final int HP_LOSS_PER_IONIZED = 0;
    private static final int UPGRADED_HP_LOSS_PER_IONIZED_INCREASE = 3;
    private static final String HP_LOSS_VAR = "HP_LOSS_VAR";

    public IonCoil_Warforged() {
        super(ID, info);

        setMagic(ADDITIONAL_IONIZED, UPGRADED_ADDITIONAL_IONIZED_INCREASE);
        setCustomVar(HP_LOSS_VAR, HP_LOSS_PER_IONIZED, UPGRADED_HP_LOSS_PER_IONIZED_INCREASE);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        IonCoilPower_Warforged ionCoilPower = new IonCoilPower_Warforged(
                player,
                this.magicNumber
        );
        this.addToBot(new ApplyPowerAction(player, player, ionCoilPower, this.magicNumber));

        if (this.upgraded) {
            int hpLossVar = this.customVar(HP_LOSS_VAR);
            HighVoltagePower_Warforged highVoltagePower = new HighVoltagePower_Warforged(
                    player,
                    hpLossVar
            );
            this.addToBot(new ApplyPowerAction(player, player, highVoltagePower, hpLossVar));
        }
    }
}
