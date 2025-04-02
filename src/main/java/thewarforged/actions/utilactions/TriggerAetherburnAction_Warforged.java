package thewarforged.actions.utilactions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import thewarforged.relics.starterRelics.CrackedAetherheartRelic_Warforged;
import thewarforged.util.GeneralUtils;

public class TriggerAetherburnAction_Warforged extends AbstractGameAction {
    private final CrackedAetherheartRelic_Warforged crackedAetherheartRelic;

    public TriggerAetherburnAction_Warforged(CrackedAetherheartRelic_Warforged crackedAetherheartRelic) {
        this.crackedAetherheartRelic = crackedAetherheartRelic;
    }

    @Override
    public void update() {
        if (this.crackedAetherheartRelic != null) {
            this.crackedAetherheartRelic.runawayAetherheart_Aetherburn();
        } else {
            final String errorStr =
                    "Bad reference to CrackedAetherheartRelic_Warforged passed to TriggerAetherburnAction_Warforged.";
            GeneralUtils.easyPrint(errorStr);
        }
        this.isDone = true;
    }
}
