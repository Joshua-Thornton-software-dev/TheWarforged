package thewarforged.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thewarforged.relics.AbstractWarforgedRelic;

import java.util.ArrayList;

public class EnergyPanelPatches {

    private static void onEnergyChanged() {
        ArrayList<AbstractRelic> heldRelics = AbstractDungeon.player.relics;
        for (AbstractRelic relic : heldRelics) {
            if (relic instanceof AbstractWarforgedRelic) ((AbstractWarforgedRelic) relic).onEnergyChanged();
        }
    }

    /**
     * Adds a call to my postfix method to the end of the setEnergy method of the EnergyPanel class.
     * The only thing this postfix method does is call my onEnergyChanged method.
     */
    @SpirePatch2(
            clz = EnergyPanel.class,
            method = "setEnergy"
    )
    public static class EnergyChangeViaSetEnergyPatch {
        public EnergyChangeViaSetEnergyPatch() {
        }

        @SpirePostfixPatch
        public static void postfix() {
            onEnergyChanged();
        }
    }

    /**
     * Adds a call to my postfix method to the end of the addEnergy method of the EnergyPanel class.
     * The only thing this postfix method does is call my onEnergyChanged method.
     */
    @SpirePatch2(
            clz = EnergyPanel.class,
            method = "addEnergy"
    )
    public static class EnergyChangeViaAddEnergyPatch {
        public EnergyChangeViaAddEnergyPatch() {
        }

        @SpirePostfixPatch
        public static void postfix() {
            onEnergyChanged();
        }
    }

    /**
     * Adds a call to my postfix method to the end of the useEnergy method of the EnergyPanel class.
     * The only thing this postfix method does is call my onEnergyChanged method.
     */
    @SpirePatch2(
            clz = EnergyPanel.class,
            method = "useEnergy"
    )
    public static class EnergyChangeViaUseEnergyPatch {
        public EnergyChangeViaUseEnergyPatch() {
        }

        @SpirePostfixPatch
        public static void postfix() {
            onEnergyChanged();
        }
    }
}
