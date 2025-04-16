package thewarforged.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Objects;

public class WarforgedUtils {
    private static final ArrayList<AbstractMonster.Intent> attackIntents;

    static {
        attackIntents = new ArrayList<>();
        attackIntents.add(AbstractMonster.Intent.ATTACK);
        attackIntents.add(AbstractMonster.Intent.ATTACK_BUFF);
        attackIntents.add(AbstractMonster.Intent.ATTACK_DEBUFF);
        attackIntents.add(AbstractMonster.Intent.ATTACK_DEFEND);
    }

    public WarforgedUtils() {
    }

    public static void easyPrint(String msg) {
        easyPrint(msg, "+++++");
    }

    public static void easyPrint(String msg, String divider) {
        System.out.println(divider);
        System.out.println(msg);
        System.out.println(divider);
    }

    /**
     * Determines if the card group already contains at least one card with the same name
     * as this card.
     * @param group The card group to check (such as the player's master deck)
     * @param card The card to check against the group
     * @return True if the card group already contains at least one card with the same name
     *      as this card; False otherwise
     */
    public static boolean doesGroupContainSimilarCard(CardGroup group, AbstractCard card) {
        for (AbstractCard cardInGroup : group.group) {
            if (Objects.equals(cardInGroup.name, card.name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMonsterAttacking(AbstractMonster monster) {
        if (monster == null) return false;
        return (attackIntents.contains(monster.intent));
    }
}
