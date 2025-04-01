package thewarforged.relics.starterRelics;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import thewarforged.relics.AbstractWarforgedRelic;

import static thewarforged.TheWarforgedMod.makeID;

public class CrackedAetherheartRelic_Warforged extends AbstractWarforgedRelic {
    private static final String NAME = "CrackedAetherheart_Warforged";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.STARTER;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    private static final int ENERGY_ON_CARD_PLAY = 1;

    public CrackedAetherheartRelic_Warforged() {
        //The second argument, NAME, is the name of both this relic and the image for this relic.
        super(ID, NAME, RARITY, LANDING_SOUND);
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        GainEnergyAction gainEnergyAction = new GainEnergyAction(ENERGY_ON_CARD_PLAY);
    }
}
