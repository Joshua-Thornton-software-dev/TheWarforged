package thewarforged.relics.starterRelics;

import com.evacipated.cardcrawl.mod.stslib.actions.common.GainCustomBlockAction;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModContainer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewarforged.cards.AbstractWarforgedCard;
import thewarforged.modifiers.EnergyShieldBlockModifier;
import thewarforged.relics.AbstractWarforgedRelic;

import static thewarforged.TheWarforgedMod.makeID;

public class DiagnosticsRelic_Warforged extends AbstractWarforgedRelic {
    private static final String NAME = DiagnosticsRelic_Warforged.class.getSimpleName();
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.STARTER;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    private static int numPartCardsAdded = 0;
    private static final int HP_PER_PART_CARD = 4;

    private static final int CARDS_TO_ACTIVATE_BONUS = 5;
    private static final int ENERGY_SHIELD_BONUS = 100;

    BlockModContainer blockModContainer;

    public DiagnosticsRelic_Warforged() {
        super(ID, NAME, RARITY, LANDING_SOUND);

        EnergyShieldBlockModifier energyShieldBlock = new EnergyShieldBlockModifier();
        blockModContainer = new BlockModContainer(this, energyShieldBlock);
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

    @Override
    public void onObtainCard(AbstractCard card) {
        String cardID = makeID(card.getClass().getSimpleName());
        //If this is a Replacement Part card,
        if (card instanceof AbstractWarforgedCard && ((AbstractWarforgedCard) card).isReplacementCard) {
            // and if the player does not have this card in the deck yet,
            if (!AbstractDungeon.player.masterDeck.getCardNames().contains(cardID)) {
                // then gain max HP appropriately.
                numPartCardsAdded++;
                AbstractDungeon.player.increaseMaxHp(HP_PER_PART_CARD, true);
                this.flash();
            }
        }
    }

    @Override
    public void atPreBattle() {
        if (numPartCardsAdded >= CARDS_TO_ACTIVATE_BONUS) {
            AbstractPlayer player = AbstractDungeon.player;
            GainCustomBlockAction blockAction =
                    new GainCustomBlockAction(blockModContainer, player, ENERGY_SHIELD_BONUS);
            this.addToBot(blockAction);
            this.flash();
        }
    }
}
