package thewarforged.cards;

import thewarforged.util.CardStats;

import static thewarforged.util.GeneralUtils.removePrefix;
import static thewarforged.util.TextureLoader.getCardTextureString;

public abstract class AbstractWarforgedCard extends BaseCard {

    public boolean isReplacementCard = false;

    public AbstractWarforgedCard(String ID, CardStats info) {
        super(ID, info, getCardTextureString(removePrefix(ID), info.cardType));
    }
    public AbstractWarforgedCard(String ID, CardStats info, String cardImage) {
        super(ID, info.baseCost, info.cardType, info.cardTarget, info.cardRarity, info.cardColor, cardImage);
    }
    public AbstractWarforgedCard(String ID, int cost, CardType cardType, CardTarget target, CardRarity rarity, CardColor color) {
        super(ID, cost, cardType, target, rarity, color, getCardTextureString(removePrefix(ID), cardType));
    }
}
