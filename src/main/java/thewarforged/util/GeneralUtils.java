package thewarforged.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

import java.util.Objects;

public class GeneralUtils {
    public static String arrToString(Object[] arr) {
        if (arr == null)
            return null;
        if (arr.length == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length - 1; ++i) {
            sb.append(arr[i]).append(", ");
        }
        sb.append(arr[arr.length - 1]);
        return sb.toString();
    }

    public static String removePrefix(String ID) {
        return ID.substring(ID.indexOf(":") + 1);
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
}
