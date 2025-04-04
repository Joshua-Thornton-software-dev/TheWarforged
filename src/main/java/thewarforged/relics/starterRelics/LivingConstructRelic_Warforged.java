package thewarforged.relics.starterRelics;

import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thewarforged.core.EnergyManager_Warforged;
import thewarforged.relics.AbstractWarforgedRelic;

import static thewarforged.TheWarforgedMod.makeID;

public class LivingConstructRelic_Warforged extends AbstractWarforgedRelic  {
    private static final String NAME = LivingConstructRelic_Warforged.class.getSimpleName();
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.STARTER;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public LivingConstructRelic_Warforged() {
        super(ID, NAME, RARITY, LANDING_SOUND);
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
