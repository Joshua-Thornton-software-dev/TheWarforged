package thewarforged.actions.utilactions;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thewarforged.relics.starterRelics.CrackedAetherheartRelic_Warforged;

public class GainEnergyAction_Warforged extends GainEnergyAction {
    private final boolean shouldDelayAetherburn;

    public GainEnergyAction_Warforged(int amount) {
        super(amount);
        this.shouldDelayAetherburn = false;
    }

    public GainEnergyAction_Warforged(int amount, boolean shouldDelayAetherburn) {
        super(amount);
        this.shouldDelayAetherburn = shouldDelayAetherburn;
    }

    @Override
    public void update() {
        super.update();
        //Find the character's Cracked Heart relic.
        AbstractRelic crackedAetherheartRelic =
                AbstractDungeon.player.getRelic(CrackedAetherheartRelic_Warforged.ID);
        //If it could not be found, then we're done here.
        if (!(crackedAetherheartRelic instanceof CrackedAetherheartRelic_Warforged)) return;

        //If this Warforged-specific GainEnergyAction should delay the aetherburn trigger,
        if (this.shouldDelayAetherburn) {
            // then create an action that will trigger it and add it to the bottom of the queue.
            TriggerAetherburnAction_Warforged triggerAetherburnAction_warforged =
                    new TriggerAetherburnAction_Warforged( (CrackedAetherheartRelic_Warforged) crackedAetherheartRelic);
            this.addToBot(triggerAetherburnAction_warforged);
            //Else, trigger is immediately.
        } else {
            ((CrackedAetherheartRelic_Warforged) crackedAetherheartRelic).runawayAetherheart_Aetherburn();
        }
        this.isDone = true;
    }
}
