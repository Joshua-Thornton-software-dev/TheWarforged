package thewarforged.cards.attacks;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewarforged.actions.attackactions.AetherDischargeAction;
import thewarforged.cards.BaseCard;
import thewarforged.character.TheWarforged;
import thewarforged.util.CardStats;

public class AetherDischarge extends BaseCard {
    public static final String ID = makeID(AetherDischarge.class.getSimpleName());

    private static final CardStats info = new CardStats(
            TheWarforged.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.BASIC,
            CardTarget.ALL_ENEMY,
            CardStats.X_COST()
    );

    private static final int ATTACK_DAMAGE = 2;
    private static final int UPGRADED_ATTACK_DAMAGE_INCREASE = 1;

    // CONSTRUCTOR
    public AetherDischarge() {
        super(ID, info);

        setDamage(ATTACK_DAMAGE, UPGRADED_ATTACK_DAMAGE_INCREASE);
        //This is set because the attack will hit multiple enemies-- NOT because it hits multiple times.
        this.isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        AetherDischargeAction aetherDischargeAction = new AetherDischargeAction(
                //The source of this damage.
                player,
                //An array of integers representing the damage being applied to the multiple targets of this attack.
                // This is handled by AbstractCard for us.
                this.multiDamage,
                //The damage type that this attack will deal. Handled by AbstractCard. Will usually equate to the
                // damage type passed into this card's constructor.
                this.damageTypeForTurn,
                //Whether this card costs no energy for the first it's played.
                this.freeToPlayOnce,
                //I *think* this equates to the baseCost of the card. Can't determine for sure.
                this.energyOnUse,
                //Whether this card has been upgraded.
                this.upgraded
        );
        addToBot(aetherDischargeAction);
    }
}
