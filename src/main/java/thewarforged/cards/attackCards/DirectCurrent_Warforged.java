package thewarforged.cards.attackCards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewarforged.actions.powerActions.ApplyIonizedPowerAction_Warforged;
import thewarforged.cards.AbstractWarforgedCard;
import thewarforged.character.TheWarforged;
import thewarforged.util.CardStats;

public class DirectCurrent_Warforged extends AbstractWarforgedCard {
    public static final String ID = makeID(DirectCurrent_Warforged.class.getSimpleName());

    private static final CardStats info = new CardStats(
            TheWarforged.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.BASIC,
            CardTarget.ENEMY,
            CardStats.POSITIVE_COST(1)
    );

    private static final int ATTACK_DAMAGE = 4;
    private static final int UPGRADED_ATTACK_DAMAGE_INCREASE = 1;

    private static final int IONIZED_TO_APPLY = 1;
    private static final int UPGRADED_IONIZED_TO_APPLY_INCREASE = 1;

    public DirectCurrent_Warforged() {
        super(ID, info);

        this.setDamage(ATTACK_DAMAGE, UPGRADED_ATTACK_DAMAGE_INCREASE);
        this.setMagic(IONIZED_TO_APPLY, UPGRADED_IONIZED_TO_APPLY_INCREASE);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        DamageInfo damageInfo = new DamageInfo(
                player,
                this.damage,
                DamageInfo.DamageType.NORMAL);
        DamageAction damageAction = new DamageAction(
                //The target of this damage action (AbstractCreature).
                monster,
                //The damage info used to calculate and define the damage being inflicted.
                damageInfo,
                //The visual/sound effects to be played on the target creature.
                AbstractGameAction.AttackEffect.LIGHTNING
        );
        this.addToBot(damageAction);

        this.addToBot(new ApplyIonizedPowerAction_Warforged(monster, player, this.magicNumber));
//        IonizedPower_Warforged ionizedPower = new IonizedPower_Warforged(monster, this.magicNumber);
//        this.addToBot(new ApplyPowerAction(monster, player, ionizedPower, this.magicNumber));
    }
}
