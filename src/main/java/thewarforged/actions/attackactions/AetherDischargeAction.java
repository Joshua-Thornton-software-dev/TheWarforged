package thewarforged.actions.attackactions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import thewarforged.util.CardStats;

public class AetherDischargeAction extends AbstractGameAction {
    public int[] multiDamage;

    private final boolean freeToPlayOnce;
    private final DamageInfo.DamageType damageType;
    private final AbstractPlayer player;
    //This seems to come from AbstractCard, meaning that it would represent the cost of that card, but I can't
    // figure out how it is set with this setup from basicMod. Leaving the way it is (pretty much).
    private final int energyOnUse;
    //Whether the AetherDischarge card that triggered this action was upgraded.
    private final boolean isUpgraded;

    // CONSTRUCTOR
    public AetherDischargeAction(
            AbstractPlayer player,
            int[] multiDamage,
            DamageInfo.DamageType damageType,
            boolean freeToPlayOnce,
            int energyOnUse,
            boolean isUpgraded) {
        this.multiDamage = multiDamage;
        this.damageType = damageType;
        this.player = player;
        this.freeToPlayOnce = freeToPlayOnce;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.isUpgraded = isUpgraded;
    }

    //For actions, update() is called once per frame while isDone is false. Most actions set this to true immediately,
    // but some do not in order to show animations or do something else over time.
    public void update() {
        //The number of times this action should add the intended effect to the queue (damage, in this case).
        int numEffects = EnergyPanel.totalCount;

        //I cannot for the life of me figure out how/why this is a thing since we always take all of their energy
        // anyway when the card gets played.
        if (this.energyOnUse != CardStats.X_COST()) {
            numEffects = this.energyOnUse;
        }

        //If the card that triggered this action is upgraded,
        if (isUpgraded) {
            // then the attack happens one extra times.
            numEffects++;
        }

        //If the player has the Chemical X relic, which adds 2 to X_COST cards,
        if (this.player.hasRelic("Chemical X")) {
            // then do so,and have the relic flash so the player can see that it is being used.
            numEffects += 2;
            this.player.getRelic("Chemical X").flash();
        }

        if (numEffects > 0) {
            for (int i = 0; i < numEffects; ++i) {
                //Right before the last hit, add a different sound effect and visual effect.
                if (i == 0) {
                    this.addToBot(new SFXAction("ATTACK_WHIRLWIND"));
                    this.addToBot(new VFXAction(new WhirlwindEffect(), 0.0F));
                }

                //For each attack, add the audio/visual effects,
                this.addToBot(new SFXAction("ATTACK_HEAVY"));
                this.addToBot(new VFXAction(this.player, new CleaveEffect(), 0.0F));

                // and create/add an action for damaging all enemies.
                DamageAllEnemiesAction damageAllEnemiesAction = new DamageAllEnemiesAction(
                        //The damage's source.
                        this.player,
                        //The damage being dealt. multiDamage is an array of integers when hitting multiple targets.
                        this.multiDamage,
                        //The type of damage being dealt by this attack.
                        this.damageType,
                        //Add no attack effect to these, since those were handled above.
                        AttackEffect.NONE,
                        true
                );
                addToBot(damageAllEnemiesAction);
            }

            //If NOT free to play once,
            if (!this.freeToPlayOnce) {
                // then reduce the player's energy appropriately.
                this.player.energy.use(EnergyPanel.totalCount);
            }
        }

        //This action is done adding effects.
        this.isDone = true;
    }
}
