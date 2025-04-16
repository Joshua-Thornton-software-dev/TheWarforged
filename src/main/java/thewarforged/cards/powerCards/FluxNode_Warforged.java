package thewarforged.cards.powerCards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewarforged.cards.AbstractWarforgedCard;
import thewarforged.character.TheWarforged;
import thewarforged.powers.FluxPower_Warforged;
import thewarforged.util.CardCosts_Warforged;
import thewarforged.util.CardStats;

/**
 * This class is for a power card that represents a missing/broken part for the Warforged, the Flux Node.
 * Once played, any card played after with a cost of 4 or higher (variable) grants the player a gain of
 * 3 (variable) temporary Dexterity.
 * Upgraded, the threshold decreases by 1, and the temp Dex increases by 1.
 * For the purposes of this effect, cards that cost X are considered to cost the player's total energy spent on
 * the card, which would be their current total at the time the X-cost card is played + the amount of energy granted
 * by the CrackedAetherheart for each card played, which varies.
 */
public class FluxNode_Warforged extends AbstractWarforgedCard {
    public static final String ID = makeID(FluxNode_Warforged.class.getSimpleName());

    private static final CardStats info = new CardStats(
            TheWarforged.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            CardCosts_Warforged.POSITIVE_COST(4)
    );

    //The amount of energy that a card must cost/use in order to trigger the effect.
    private static final int ENERGY_COST_TO_ACTIVATE = 4;
    private static final int UPGRADED_ENERGY_COST_TO_ACTIVATE_INCREASE = -1;

    //The amount of temporary Dexterity that will be gained by this power when triggered.
    private static final int DEX_GAINED = 3;
    private static final int UPGRADED_DEX_GAINED_INCREASE = 1;
    private static final String CUSTOM_VAR_NAME = "DEX_GAIN";

    public FluxNode_Warforged() {
        super(ID, info);
        this.isReplacementCard = true;

        setMagic(ENERGY_COST_TO_ACTIVATE, UPGRADED_ENERGY_COST_TO_ACTIVATE_INCREASE);
        setCustomVar(CUSTOM_VAR_NAME, DEX_GAINED, UPGRADED_DEX_GAINED_INCREASE);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        FluxPower_Warforged fluxPower = new FluxPower_Warforged(
                player,
                this.magicNumber,
                this.customVar(CUSTOM_VAR_NAME)
        );
        this.addToBot(new ApplyPowerAction(player, player, fluxPower));
    }
}
