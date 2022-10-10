package com.ferox.game.world.entity.combat.hit;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Entity;
import com.ferox.game.world.entity.HitOrigin;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.formula.AccuracyFormula;
import com.ferox.game.world.entity.combat.magic.CombatSpell;
import com.ferox.game.world.entity.combat.method.CombatMethod;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.ferox.game.world.entity.mob.Flag;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.PlayerStatus;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.util.NpcIdentifiers;

import java.security.SecureRandom;
import java.util.function.Consumer;

import static com.ferox.game.world.entity.combat.prayer.default_prayer.Prayers.*;
import static com.ferox.util.ItemIdentifiers.KILLERS_KNIFE;
import static com.ferox.util.NpcIdentifiers.KILLER;

/**
 * Represents a pending hit.
 *
 * @author Professor Oak
 */
public class Hit {

    /**
     * The random used for hits.
     */
    public static final SecureRandom secure_random = new SecureRandom();

    public boolean toremove;
    public boolean showSplat;

    private HitOrigin origin;

    /**
     * if its a veng/recoil ring type of hit, in this case. this stops infinite loops of vengeance hits/recoil ring
     * repeating on reflected damage.
     */
    public boolean reflected;

    public boolean forceShowSplashWhenMissMagic;
    public boolean pidded;

    public Hit forceShowSplashWhenMissMagic() {
        forceShowSplashWhenMissMagic = true;
        return this;
    }

    private CombatType style = CombatType.MELEE;

    /**
     * The attacker instance.
     */
    private final Mob attacker;

    public HitOrigin origin() {
        return origin;
    }

    /**
     * The victim instance.
     */
    private final Mob target;

    public CombatSpell spell;

    public Hit setSplatType(SplatType splatType) {
        this.splatType = splatType;
        return this;
    }

    public SplatType splatType;

    /**
     * The total damage this hit will deal
     **/
    private int damage;

    public int getDelay() {
        return delay;
    }

    /**
     * The delay of this hit
     **/
    private int delay;

    /**
     * Check accuracy of the hit?
     **/
    private boolean checkAccuracy;

    /**
     * Was the hit accurate?
     **/
    private boolean accurate;

    /**
     * Flag to alwaysQueue hits rather than instant-process if PID applies.
     * This is extremely important because weapons like Gmaul and Dragon thrownaxe are triggered during the Parse Packet
     * phase - by an Action Button. This means an instant-processed hit will kill the target in a PID senario before the
     * target's packets (such as eating food) are even processed! Non-instant special attacks are executed during the
     * Process Scripts phase - and are therefore not applicable to this circumstance.
     */
    private boolean queueAlways = false;

    /**
     * Cache the combat type
     */
    private CombatType combatType;

    /**
     * Constructs a QueueableHit with a total of {hitCountToGenerate} hits.
     **/
    public Hit(Mob attacker, Mob target, CombatMethod method, boolean checkAccuracy, int delay, int damage) {
        this.attacker = attacker;
        this.target = target;
        if (method instanceof CommonCombatMethod) { // should be the base class of all scripts now
            CommonCombatMethod commonCombatMethod = (CommonCombatMethod) method;
            combatType = commonCombatMethod.styleOf();
        }
        this.checkAccuracy = checkAccuracy;
        this.damage = damage;
        applyAccuracyToMiss();
        this.delay = delay;
        this.splatType = damage < 1 ? SplatType.BLOCK_HITSPLAT : SplatType.HITSPLAT;
    }

    /**
     * your own hit generator
     *
     * @param attacker
     * @param target
     * @param damage
     * @return
     */
    public static Hit builder(Mob attacker, Mob target, int damage) {
        return builder(attacker, target, damage, 1);
    }

    /**
     * your own hit generator
     *
     * @param attacker
     * @param target
     * @param damage
     * @return
     */
    public static Hit builder(Mob attacker, Mob target, int damage, int delay) {
        return builder(attacker, target, damage, delay, CombatType.MELEE);
    }

    public static Hit builder(Mob attacker, Mob target, int damage, int delay, CombatType type) {
        Hit hit = new Hit(attacker, target, null, false, delay, damage);
        hit.combatType = type;
        return hit;
    }

    /**
     * your own hit generator
     *
     * @param delay
     * @return
     */

    public Hit delay(int delay) {
        delay = Math.max(0, delay);
        return this;
    }

    public Mob getAttacker() {
        return attacker;
    }

    public Mob getTarget() {
        return target;
    }

    public int decrementAndGetDelay() {
        return --delay;
    }

    public int getDamage() {
        return damage;
    }

    public Hit setAccurate(boolean accurate) {
        this.accurate = accurate;
        return this;
    }

    public boolean isAccurate() {
        return accurate;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Hit setCombatType(CombatType type) {
        this.combatType = type;
        return this;
    }

    public boolean invalid() {
        if (target.locked() && !target.isDamageOkLocked() && !target.isDelayDamageLocked())
            return true;

        if (origin() != null && target != null && origin instanceof Entity) {
            Npc from = ((Npc) origin());

            System.out.printf("incoming hit on %s origin=%s state=%s%n", target, origin(), from.dead());

            if (origin() instanceof Player) {
                Tile myWildTile = target.tile().level < 6400 ? target.tile() : target.tile().transform(0, -6400, 0);
                Tile originWildTile = from.tile().level < 6400 ? from.tile() : from.tile().transform(0, -6400, 0);
                int wildDist = originWildTile.getChevDistance(myWildTile);

                if (wildDist >= (WildernessArea.inWilderness(from.tile()) ? 64 : 18)) {
                    System.out.print("Hit nullified: dist " + wildDist);
                    return true;
                }
            } else if (origin() instanceof Npc) {
                if (from.dead()) {
                    return true;
                }

                Tile attackerTile = ((Npc) origin()).tile();
                Tile myTile = target.tile();

                if (myTile.getChevDistance(attackerTile) >= 64) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * checks alwaysHit attrib and accuracy (depending on combat method+style). sets damage to 0 or maxhp or does no change at all, retaining existing {@link #damage} value set by {@link CombatFactory#calcDamageFromType(Mob, Mob, CombatType)}
     */
    private void applyAccuracyToMiss() {
        if (attacker == null || target == null) {
            return;
        }

        if (target.dead()) {
            //System.out.println(target.getMobName() + " is dead.");
            return;
        }

        if (attacker.isPlayer() && target.isPlayer()) {
            if (target.getAsPlayer().getStatus() == PlayerStatus.TRADING) {
                target.getAsPlayer().getTrading().abortTrading();
            }
        }

        if (target.isNpc() && target.getAsNpc().isCombatDummy()) {
            checkAccuracy = false;
        }

        var success = false;

        if (combatType != null) {
            success = AccuracyFormula.doesHit(attacker, target, combatType);
        }

        accurate = !checkAccuracy || success;

        int damage;

        final int alwaysHitDamage = attacker.getAttribOr(AttributeKey.ALWAYS_HIT, 0);
        final boolean alwaysHitActive = alwaysHitDamage > 0;
        final boolean oneHitActive = attacker.getAttribOr(AttributeKey.ONE_HIT_MOB, false);

        if (alwaysHitActive || oneHitActive)
            accurate = true;

        if (!accurate) {
            damage = 0;
        } else {
            if (oneHitActive) {
                damage = target.hp();
            } else if (alwaysHitActive) {
                damage = alwaysHitDamage;
            } else {
                damage = this.damage;
            }
        }
        this.damage = damage;
    }

    public CombatType getCombatType() {
        return combatType;
    }

    @Override
    public String toString() {
        return "PendingHit{" +
            "attacker=" + attacker +
            ", target=" + target +
            ", dmg=" + damage +
            ", delay=" + delay +
            ", checkAccuracy=" + checkAccuracy +
            ", accurate=" + accurate +
            ", combatType=" + combatType +
            '}';
    }

    public Hit setIsReflected() {
        reflected = true;
        return this;
    }

    public Hit checkAccuracy() {
        checkAccuracy = true;
        applyAccuracyToMiss();
        return this;
    }

    public Hit spell(CombatSpell spell) {
        this.spell = spell;
        return this;
    }

    /**
     * called after a hit has been executed and appears visually. will be finalized and damage cannot change.
     */
    public Consumer<Hit> postDamage;

    public Hit postDamage(Consumer<Hit> postDamage) {
        this.postDamage = postDamage;
        return this;
    }

    public Hit pidAdjust() {
            if (target != null && attacker != null && target.isPlayer() && attacker.isPlayer() && target != attacker && attacker.pidOrderIndex < target.pidOrderIndex) {
                delay(delay - 1);
                pidded = true;
            }

            if (style != CombatType.MELEE && delay < 1) {
                delay = 1;
            }

        return this;
    }

    public void submit() {
        if (target != null && !invalid()) {
                pidAdjust();
                CombatFactory.addPendingHit(this);
            }
        }

    public Hit queueAlways(boolean b) {
        queueAlways = b;
        return this;
    }

    public void playerSync() {
        if (target == null) return;
        if (target.splats.size() >= 4)
            return;
        target.splats.add(new Splat(getDamage(), splatType));
        target.getUpdateFlag().flag(Flag.FIRST_SPLAT);
    }
}
