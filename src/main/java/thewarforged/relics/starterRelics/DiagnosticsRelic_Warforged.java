package thewarforged.relics.starterRelics;

import com.evacipated.cardcrawl.mod.stslib.actions.common.GainCustomBlockAction;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModContainer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewarforged.cards.AbstractWarforgedCard;
import thewarforged.modifiers.EnergyShieldBlockModifier;
import thewarforged.relics.AbstractWarforgedRelic;
import thewarforged.util.GeneralUtils;

import java.util.ArrayList;

import static thewarforged.TheWarforgedMod.makeID;

public class DiagnosticsRelic_Warforged extends AbstractWarforgedRelic {
    private static final String NAME = DiagnosticsRelic_Warforged.class.getSimpleName();
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.STARTER;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    private int numPartCardsAdded = 0;
    private final int HP_PER_PART_CARD = 4;

    private final int CARDS_TO_ACTIVATE_BONUS = 5;
    private final int ENERGY_SHIELD_BONUS = 100;

    BlockModContainer blockModContainer;

    public DiagnosticsRelic_Warforged() {
        super(ID, NAME, RARITY, LANDING_SOUND);

        EnergyShieldBlockModifier energyShieldBlock = new EnergyShieldBlockModifier();
        this.blockModContainer = new BlockModContainer(this, energyShieldBlock);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0]
                + this.HP_PER_PART_CARD
                + this.DESCRIPTIONS[1]
                + this.CARDS_TO_ACTIVATE_BONUS
                + this.DESCRIPTIONS[2]
                + this.ENERGY_SHIELD_BONUS
                + this.DESCRIPTIONS[3];
    }

    @Override
    public void onEquip() {
        //Track the names of the replacement part cards already found in the deck.
        ArrayList<String> replacementCardNames = new ArrayList<>();
        //For each card in the character's deck,
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            // if this is a replacement part card and the first time we have seen it in the deck,
            if (this.isCardReplacementPart(card) && !(replacementCardNames.contains(card.name))) {
                // then track that we found the card and benefit the player accordingly.
                replacementCardNames.add(card.name);
                this.replacementPartCardObtained();
            }
        }
    }

    @Override
    public void onObtainCard(AbstractCard card) {
        String cardID = makeID(card.getClass().getSimpleName());
        //If this is a unique Replacement Part card (first added to the deck),
        if (this.isCardUniqueReplacementPart(card)) {
            // then gain max HP appropriately.
            this.replacementPartCardObtained();
        }
    }

    @Override
    public void atPreBattle() {
        if (this.numPartCardsAdded >= this.CARDS_TO_ACTIVATE_BONUS) {
            AbstractPlayer player = AbstractDungeon.player;
            GainCustomBlockAction blockAction =
                    new GainCustomBlockAction(this.blockModContainer, player, this.ENERGY_SHIELD_BONUS);
            this.addToBot(blockAction);
            this.flash();
        }
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.decreaseMaxHealth(this.numPartCardsAdded * this.HP_PER_PART_CARD);
        CardCrawlGame.sound.play("BLOOD_SWISH");
    }

    private boolean isCardReplacementPart(AbstractCard card) {
        return (card instanceof AbstractWarforgedCard && ((AbstractWarforgedCard) card).isReplacementCard);
    }

    /**
     * Determines if the card is both a replacement part card and would be unique if added to the deck.
     * @param card The card to check against the player's deck
     * @return True if this card would be a unique replacement part card once added to the deck; False otherwise
     */
    private boolean isCardUniqueReplacementPart(AbstractCard card) {
        if (this.isCardReplacementPart(card)) {
            return !(GeneralUtils.doesGroupContainSimilarCard(AbstractDungeon.player.masterDeck, card));
        }
        return false;
    }

    private void replacementPartCardObtained() {
        this.numPartCardsAdded++;
        AbstractDungeon.player.increaseMaxHp(this.HP_PER_PART_CARD, true);
        this.flash();
    }
}
