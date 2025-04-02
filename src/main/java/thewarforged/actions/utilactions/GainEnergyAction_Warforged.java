package thewarforged.actions.utilactions;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thewarforged.relics.starterRelics.CrackedAetherheartRelic_Warforged;

public class GainEnergyAction_Warforged extends GainEnergyAction {
    private final boolean wasTriggeredByCardPlay;

    public GainEnergyAction_Warforged(int amount) {
        super(amount);
        this.wasTriggeredByCardPlay = true;
    }

    public GainEnergyAction_Warforged(int amount, boolean wasTriggeredByCardPlay) {
        super(amount);
        this.wasTriggeredByCardPlay = wasTriggeredByCardPlay;
    }

    @Override
    public void update() {
        super.update();
        //If this Warforged-specific GainEnergyAction was triggered by a card being played by the Warforged,
        if (this.wasTriggeredByCardPlay) {
            // then find its Cracked Aetherheart relic and trigger Aetherburn.
            AbstractRelic crackedAetherheartRelic =
                    AbstractDungeon.player.getRelic("${modID}:CrackedAetherheartRelic_Warforged");
            if (crackedAetherheartRelic instanceof CrackedAetherheartRelic_Warforged) {
                ((CrackedAetherheartRelic_Warforged) crackedAetherheartRelic).runawayAetherheart_Aetherburn();
            }
        }
    }
}
