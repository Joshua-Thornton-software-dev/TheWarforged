package thewarforged.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thewarforged.relics.starterRelics.CrackedAetherheartRelic_Warforged;
import thewarforged.util.CardCosts_Warforged;

import static thewarforged.TheWarforgedMod.makeID;

public class FluxPower_Warforged extends AbstractWarforgedPower {
    public static final String POWER_ID = makeID(FluxPower_Warforged.class.getSimpleName());
    private static final AbstractPower.PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURNED_BASED = false;

    private static CrackedAetherheartRelic_Warforged _aetherheartRelic;

    /**
     * Constructor.
     * @param owner Must be the player, or this power will break
     * @param energyToTrigger The amount of energy a card must cost/use in order to trigger the effect
     * @param tempDexOnTrigger The amount of temporary Dexterity gained when triggered
     */
    public FluxPower_Warforged(AbstractCreature owner, int energyToTrigger, int tempDexOnTrigger) {
        super(POWER_ID, POWER_TYPE, IS_TURNED_BASED, owner, energyToTrigger);
        this.amount2 = tempDexOnTrigger;
    }

    private static CrackedAetherheartRelic_Warforged get_aetherheartRelic() {
        if (_aetherheartRelic == null) {
            AbstractRelic relic = AbstractDungeon.player.getRelic(CrackedAetherheartRelic_Warforged.ID);
            _aetherheartRelic = (CrackedAetherheartRelic_Warforged) relic;
        }
        return _aetherheartRelic;
    }

    private static AbstractPlayer getPlayer() { return AbstractDungeon.player; }

    @Override
    public void updateDescription() {
        this.description = this.DESCRIPTIONS[0]
                + this.amount
                + this.DESCRIPTIONS[1]
                + this.amount2
                + this.DESCRIPTIONS[2];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action){
        int cost = card.cost;
        if (cost == CardCosts_Warforged.X_COST()) {
            cost = EnergyPanel.getCurrentEnergy() + get_aetherheartRelic().getCurrentEnergyGain();
        }
        if (cost >= this.amount) {
            AbstractPlayer player = getPlayer();

            DexterityPower gainDexPower = new DexterityPower(player, this.amount2);
            ApplyPowerAction positivePowerAction =
                    new ApplyPowerAction(player, player, gainDexPower, this.amount2);
            this.addToBot(positivePowerAction);

            LoseDexterityPower loseDexterityPower = new LoseDexterityPower(player, this.amount2);
            ApplyPowerAction negativePowerAction =
                    new ApplyPowerAction(player, player, loseDexterityPower, this.amount2);
            this.addToBot(negativePowerAction);

            this.flash();
        }
    }
}
