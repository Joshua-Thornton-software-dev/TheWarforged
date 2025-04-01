package thewarforged.cards.attackCards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewarforged.cards.AbstractWarforgedCard;
import thewarforged.character.TheWarforged;
import thewarforged.util.CardStats;

public class Strike_Warforged extends AbstractWarforgedCard {
    //Using makeID adds the mod ID, so the final ID will be something like "thewarforged:Strike_Warforged".
    // Using class.getSimpleName() instead of a String literal allows for easy class renaming down the road.
    public static final String ID = makeID(Strike_Warforged.class.getSimpleName());

    private static final CardStats info = new CardStats(
            //The card's "color", which is really just a type categorization. Set up in TheWarforfed.java.
            TheWarforged.Meta.CARD_COLOR,
            //The card type. Can be ATTACK, SKILL, POWER, CURSE, or STATUS.
            CardType.ATTACK,
            //The card's rarity. Can be BASIC, COMMON, UNCOMMON, RARE, SPECIAL, or CURSE.
            // SPECIAL is for cards you can only get from events.
            // CURSE is of course for curses, but some special curses are exceptions,
            // such as Curse of the Bell and Necronomicurse.
            CardRarity.BASIC,
            //The card's target(s), if any. Can be ENEMY, ALL_ENEMY, SELF, NONE, SELF_AND_ENEMY, or ALL.
            CardTarget.ENEMY,
            //The card's baseCost, before modifiers. Can be -2 or higher. Values of 0 or higher result in playable cards
            // this that exact base cost. -1 results in an X-cost card. -2 results in an unplayable card, like status
            // cards. CardStats contains readable static methods for these values.
            CardStats.ZERO_COST()
    );

    //Not to be confused with the "damage" public int variable on AbstractCard. This is damage the card would deal
    // without any modifiers before being upgraded.
    private static final int ATTACK_DAMAGE = 6;
    //This is the number that will be added to the damage set from ATTACK_DAMAGE above when
    // this card is upgraded (ie, 6 + 3).
    private static final int UPGRADED_ATTACK_DAMAGE_INCREASE = 3;

    // CONSTRUCTOR
    public Strike_Warforged() {
        //This information is required for BaseCard's constructor.
        super(ID, info);

        //This sets the damage for the card and how much that damage should increase when upgraded.
        // This method is provided via BaseCard (the parent class).
        this.setDamage(ATTACK_DAMAGE, UPGRADED_ATTACK_DAMAGE_INCREASE);

        //Add these tags so that the game knows that this is a Strike_Warforged card, and specifically a Starter Strike_Warforged. "tags"
        // comes from AbstractCard, the parent class to BaseCard (this class's grandparent class).
        tags.add(CardTags.STARTER_STRIKE);
        tags.add(CardTags.STRIKE);
    }

    /**
     * Overrides the use method from AbstractCard.
     * @param player Will always reference the player (never a monster).
     * @param monster If an enemy is targeted, monster will always reference that monster (never the player).
     *                For cards that do not target an enemy, monster will be null.
     */
    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        //Set up the info for the damage being dealt in this attack.
        DamageInfo damageInfo = new DamageInfo(
                //The source of this damage (AbstractCreature).
                player,
                //The base damage for this damage (before modifiers).
                this.damage,
                //The type of damage being inflicted. Can be NORMAL, THORNS, or HP_LOSS.
                DamageInfo.DamageType.NORMAL);
        // Set up the damage action which will be added to the action queue.
        DamageAction damageAction = new DamageAction(
                //The target of this damage action (AbstractCreature).
                monster,
                //The damage info used to calculate and define the damage being inflicted (DamageInfo).
                damageInfo,
                //The visual/sound effects to be played on the target creature (AbstractGameAction.AttackEffect).
                AbstractGameAction.AttackEffect.SLASH_VERTICAL
        );
        //Add that damage action to the bottom of the queue. It will happen once all actions before it finish.
        this.addToBot(damageAction);
    }
}
