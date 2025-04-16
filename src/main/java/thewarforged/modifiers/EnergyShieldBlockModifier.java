package thewarforged.modifiers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.KeywordStrings;
import thewarforged.powers.StableShieldsPower_Warforged;

import static thewarforged.TheWarforgedMod.makeID;

public class EnergyShieldBlockModifier extends AbstractBlockModifier {
    public static final String ID = makeID("EnergyShieldBlockModifier");
    public final CardStrings energyShieldStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public final KeywordStrings blockKeywordStrings = CardCrawlGame.languagePack.getKeywordString("Block");

    public EnergyShieldBlockModifier() {}

    @Override
    public Priority priority() { return Priority.TOP; }

    @Override
    public int amountLostAtStartOfTurn() {
        if (this.owner.getPower(StableShieldsPower_Warforged.POWER_ID) != null) return 0;
        return ((this.getCurrentAmount() + 1) / 2);
    }

    @Override
    public String getName() {
        return energyShieldStrings.NAME;
    }

    @Override
    public String getDescription() {
        return energyShieldStrings.DESCRIPTION;
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new EnergyShieldBlockModifier();
    }

    @Override
    public boolean isInherent() {
        return true;
    }

    @Override
    public Color blockImageColor() {
        return Color.PURPLE;
    }
}
