package thewarforged.core;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thewarforged.relics.starterRelics.CrackedAetherheartRelic_Warforged;
import thewarforged.util.GeneralUtils;

public class EnergyManager_Warforged extends EnergyManager {

    AbstractPlayer _player;
    CrackedAetherheartRelic_Warforged _crackedAetherheartRelic;
    GameActionManager _actionManager;

    public EnergyManager_Warforged(int energy) {
        super(energy);
    }

    private AbstractPlayer player() {
        if (this._player == null) this._player = AbstractDungeon.player;
        return this._player;
    }

    private CrackedAetherheartRelic_Warforged crackedAetherheartRelic() {
        if (this._crackedAetherheartRelic == null) {
            AbstractRelic tmp = this.player().getRelic(CrackedAetherheartRelic_Warforged.ID);
            if (tmp instanceof CrackedAetherheartRelic_Warforged) {
                this._crackedAetherheartRelic = (CrackedAetherheartRelic_Warforged) tmp;
            } else {
                this._crackedAetherheartRelic = null;
            }
        }
        return this._crackedAetherheartRelic;
    }

    private GameActionManager actionManager() {
        if (this._actionManager == null) this._actionManager = AbstractDungeon.actionManager;
        return this._actionManager;
    }

    /**
     * Rather than patch the base class's recharge method, we override it to either work exactly like normal (if
     * the character doesn't have the Cracked Aetherheart relic for some reason) or to simulate the same interaction
     * it would normally have with Ice Cream, since this relic has the same effect.
     */
    @Override
    public void recharge() {
        GeneralUtils.easyPrint("1 ---- " + EnergyPanel.totalCount);
        //If the character has the Cracked Aetherheart relic,
        if (this.crackedAetherheartRelic() != null) {
            GeneralUtils.easyPrint("2 ---- " + EnergyPanel.totalCount);
            // then treat them the same as if they had the Ice Cream relic.
            if (EnergyPanel.totalCount > 0) {
                GeneralUtils.easyPrint("3 ---- " + EnergyPanel.totalCount);
                this.crackedAetherheartRelic().flash();
                RelicAboveCreatureAction relicAboveCreatureAction =
                        new RelicAboveCreatureAction(this.player(), this.crackedAetherheartRelic());
                this.actionManager().addToTop(relicAboveCreatureAction);
                GeneralUtils.easyPrint("4 ---- " + EnergyPanel.totalCount);
            }
            GeneralUtils.easyPrint("5 ---- " + EnergyPanel.totalCount);
            //This should be adding zero, unless the character's core design has changed.
            EnergyPanel.addEnergy(this.energy);
            GeneralUtils.easyPrint("6 ---- " + EnergyPanel.totalCount);
            this.actionManager().updateEnergyGain(this.energy);
            GeneralUtils.easyPrint("7 ---- " + EnergyPanel.totalCount);
        } else {
            //Character did not have the Cracked Aetherheart relic. Just call the super's recharge like normal.
            super.recharge();
        }

    }
}
