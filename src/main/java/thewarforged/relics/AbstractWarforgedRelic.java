package thewarforged.relics;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewarforged.character.TheWarforged;

public abstract class AbstractWarforgedRelic extends BaseRelic {

    /**
     * This constructor is used for character-specific relics.
     * @param id The ID of this relic, made in each relic class with `makeID(NAME)`
     * @param imageName The name of the image to use for this relic (should be the same name as the relic)
     * @param tier The rarity of the relic
     * @param sfx The sound played when the relic is picked up/clicked
     */
    public AbstractWarforgedRelic(
            String id,
            String imageName,
            RelicTier tier,
            LandingSound sfx) {
        super(id, imageName, TheWarforged.Meta.CARD_COLOR, tier, sfx);
    }

    protected AbstractPlayer player() {
        return AbstractDungeon.player;
    }

    /**
     * This constructor is used for general relics.
     * @param id The ID of this relic, made in each relic class with `makeID(NAME)`
     * @param tier The rarity of the relic
     * @param sfx The sound played when the relic is picked up/clicked
     */
    public AbstractWarforgedRelic(
            String id,
            RelicTier tier,
            LandingSound sfx) {
        super(id, tier, sfx);
    }

    public void onEnergyChanged() {
    }
}
