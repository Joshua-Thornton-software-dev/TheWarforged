package thewarforged.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import thewarforged.util.GeneralUtils;

public class EnergyPanelPatches {

    private static void onEnergyChanged() {
        int currentEnergy = EnergyPanel.getCurrentEnergy();
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

//    @SpirePatch2(
//            clz = EnergyManager.class,
//            method = "recharge"
//    )
//    public static class CheckWarforgedRelicDuringRechargePatch {
//        public CheckWarforgedRelicDuringRechargePatch() {
//        }
//
//        @SpireInstrumentPatch
//        public static ExprEditor Instrument() {
//            return new ExprEditor() {
//                public void edit(MethodCall m) throws CannotCompileException {
//                    if (m.getMethodName().equals("recharge"))
//                }
//            };
//        }
//    }
}
