package thewarforged.core;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thewarforged.relics.starterRelics.LivingConstructRelic_Warforged;

public class EnergyManager_Warforged extends EnergyManager {

    private static LivingConstructRelic_Warforged _livingConstructRelic;

    private static AbstractPlayer player() {
        return AbstractDungeon.player;
    }

    private static GameActionManager actionManager() {
        return AbstractDungeon.actionManager;
    }

    public EnergyManager_Warforged(int energy) {
        super(energy);
    }

    private static LivingConstructRelic_Warforged livingConstructRelic() {
        if (_livingConstructRelic == null) {
            AbstractRelic tmp = player().getRelic(LivingConstructRelic_Warforged.ID);
            if (tmp instanceof LivingConstructRelic_Warforged) {
                _livingConstructRelic = (LivingConstructRelic_Warforged) tmp;
            } else {
                _livingConstructRelic = null;
            }
        }
        return _livingConstructRelic;
    }

    /**
     * Rather than patch the base class's recharge method, we override it to either work exactly like normal (if
     * the character doesn't have the Living Construct relic for some reason) or to simulate the same interaction
     * it would normally have with Ice Cream, since this relic has the same effect.
     */
    @Override
    public void recharge() {
        //If the character has the Living Construct relic,
        if (livingConstructRelic() != null) {
            // then treat them the same as if they had the Ice Cream relic.
            if (EnergyPanel.totalCount > 0) {
                livingConstructRelic().flash();
                RelicAboveCreatureAction relicAboveCreatureAction =
                        new RelicAboveCreatureAction(player(), livingConstructRelic());
                actionManager().addToTop(relicAboveCreatureAction);
            }
            //This should be adding zero, unless the character's core design has changed.
            EnergyPanel.addEnergy(this.energy);
            actionManager().updateEnergyGain(this.energy);
        } else {
            //Character did not have the Living Construct relic. Just call the super's recharge like normal.
            super.recharge();
        }

    }
}
