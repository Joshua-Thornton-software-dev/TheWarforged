package thewarforged.relics.starterRelics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thewarforged.actions.utilactions.GainEnergyAction_Warforged;
import thewarforged.core.EnergyManager_Warforged;
import thewarforged.relics.AbstractWarforgedRelic;
import thewarforged.util.CardStats;

import static thewarforged.TheWarforgedMod.makeID;

@SuppressWarnings("FieldCanBeLocal")
public class CrackedAetherheartRelic_Warforged extends AbstractWarforgedRelic {
    private static final String NAME = CrackedAetherheartRelic_Warforged.class.getSimpleName();
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.STARTER;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    private final int BASE_MAX_SAFE_ENERGY = 3;
    private int maxSafeEnergy = this.BASE_MAX_SAFE_ENERGY;

    private final int DAMAGE_PER_ENERGY_PAST_MAX = 1;

    private int numCardsPlayed = 0;
    private final int BASE_ENERGY_RAMP_MARKER = 3;
    private int energyRampMarker = this.BASE_ENERGY_RAMP_MARKER;

    private final int STARTING_ENERGY_GAIN = 1;
    private final int ENERGY_RAMP_MULTIPLIER = 2;
    private int currentEnergyGain = STARTING_ENERGY_GAIN;

    public CrackedAetherheartRelic_Warforged() {
        //The second argument, NAME, is the name of both this relic and the image for this relic.
        super(ID, NAME, RARITY, LANDING_SOUND);
    }

    public int getCurrentEnergyGain() {
        return this.currentEnergyGain;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atPreBattle() {
        this.ensureCustomEnergyManager();
        this.setEnergyPerTurn();
    }

    @Override
    public void atTurnStart() {
        //Reset the overload counter and energyGain.
        this.numCardsPlayed = 0;
        this.currentEnergyGain = STARTING_ENERGY_GAIN;
    }

    /**
     * This happens AFTER a card's effects (after `use`).
     * @param targetCard The card that was used
     * @param useCardAction The action that was on the stack to be used in the previous use action.
     */
    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        //A card was played! Track the number of cards played.
        this.numCardsPlayed++;

        //Gain energy for playing a card. Delay the subsequent Aetherburn until after the card's
        // effect if that card is an X_COST card.
        this.volatileAether_GainEnergy(targetCard.cost == CardStats.X_COST());

        //Ramp up the energy gained per card played if appropriate.
        if (this.numCardsPlayed % this.energyRampMarker == 0) {
            this.currentEnergyGain *= this.ENERGY_RAMP_MULTIPLIER;
        }
    }

    /**
     * The aetherheart is spinning wildly and generating dangerous amounts of energy!
     * The character gains energy. The amount gained increases dramatically as they play
     * more and more cards in the same turn.
     */
    private void volatileAether_GainEnergy(boolean shouldDelayAetherburn) {

        //This action will trigger aetherburn after the energy is gained.
        GainEnergyAction_Warforged gainEnergyAction =
                new GainEnergyAction_Warforged(this.currentEnergyGain, shouldDelayAetherburn);
        this.addToTop(gainEnergyAction);
    }

    /**
     * Handles the HP loss triggered by having too much energy after playing a card. The energy gained by
     * playing a card via this relic is included in this calculation, and this method is called by that custom
     * GainEnergyAction_Warforged after the energy is applied.
     */
    public void volatileAether_Aetherburn() {
        // Determine how much energy the character has past the maximum safe amount.
        int currEnergy = EnergyPanel.getCurrentEnergy();
        int aetherburn = currEnergy - this.maxSafeEnergy;
        // If there was enough energy to cause damage, cause the character to lose that much HP.
        if (aetherburn > 0) {
            LoseHPAction aetherburnAction = new LoseHPAction(
                    this.player(),
                    this.player(),
                    aetherburn,
                    AbstractGameAction.AttackEffect.LIGHTNING);
            this.addToBot(aetherburnAction);
        }
    }

    /**
     * Ensure that the character is using the custom energy manager meant for the Warforged.
     * I allowed the default to be 3 just in case the Warforged was ever played without this relic
     * for some reason.
     */
    private void ensureCustomEnergyManager() {
        //The custom energy manager is required
        if (!(this.player().energy instanceof EnergyManager_Warforged)) {
            this.player().energy = new EnergyManager_Warforged(EnergyPanel.getCurrentEnergy());
        }
    }

    private void setEnergyPerTurn() {
        this.player().energy.energyMaster = 0;
        this.player().energy.energy = 0;
    }
}
