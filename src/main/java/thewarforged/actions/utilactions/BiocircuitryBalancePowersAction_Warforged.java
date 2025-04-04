package thewarforged.actions.utilactions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewarforged.relics.starterRelics.BiocircuitryRelic_Warforged;

public class BiocircuitryBalancePowersAction_Warforged extends AbstractGameAction {

    private static BiocircuitryRelic_Warforged _biocircuitryRelic;

    private static BiocircuitryRelic_Warforged biocircuitryRelic() {
        if (_biocircuitryRelic == null) {
            _biocircuitryRelic =
                    (BiocircuitryRelic_Warforged) AbstractDungeon.player.getRelic(BiocircuitryRelic_Warforged.ID);
        }
        return _biocircuitryRelic;
    }

    @Override
    public void update() {
        if (biocircuitryRelic() != null) {
            _biocircuitryRelic.powerFluctuations_BalancePowers();
        }

        this.isDone = true;
    }
}
