package thewarforged.cards.skillCards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewarforged.cards.AbstractWarforgedCard;
import thewarforged.character.TheWarforged;
import thewarforged.util.CardStats;

public class Defend_Warforged extends AbstractWarforgedCard {
    public static final String ID = makeID(Defend_Warforged.class.getSimpleName());

    private static final CardStats info = new CardStats(
            TheWarforged.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.BASIC,
            CardTarget.SELF,
            CardStats.ZERO_COST()
    );

    //Not to be confused with the "block" public variable on AbstractCard, which will be initialized using this value.
    private static final int BLOCK_AMOUNT = 5;
    private static final int UPGRADED_BLOCK_AMOUNT_INCREASE = 3;

    // CONSTRUCTOR
    public Defend_Warforged() {
        super(ID, info);

        setBlock(BLOCK_AMOUNT, UPGRADED_BLOCK_AMOUNT_INCREASE);

        //There is apparently no card tag for defends/blocking except for the STARTER_DEFEND.
        tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        //Set up the block action that will be added to the action queue.
        GainBlockAction gainBlockAction = new GainBlockAction(
                //The target of this GainBlockAction (AbstractCreature).
                player,
                //The source of this block gain, which is the player (AbstractCreature).
                player,
                //The base block amount for this action (from AbstractCard).
                block,
                false
        );
        addToBot(gainBlockAction);
    }
}
