package thewarforged.relics.starterRelics;

import thewarforged.relics.AbstractWarforgedRelic;

import static thewarforged.TheWarforgedMod.makeID;

public class DiagnosticsRelic_Warforged extends AbstractWarforgedRelic {
    private static final String NAME = DiagnosticsRelic_Warforged.class.getSimpleName();
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.STARTER;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    private static final int HP_PER_PART_CARD = 4;
    private static final int CARDS_TO_ACTIVATE_BONUS = 5;
    private static final int ENERGY_SHIELD_BONUS = 100;
    private static int numPartCardsAdded = 0;

    public DiagnosticsRelic_Warforged() {
        super(ID, NAME, RARITY, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0]
                + HP_PER_PART_CARD
                + this.DESCRIPTIONS[1]
                + CARDS_TO_ACTIVATE_BONUS
                + this.DESCRIPTIONS[2]
                + ENERGY_SHIELD_BONUS
                + this.DESCRIPTIONS[3];
    }
}
