package com.ferox.game.content.raids;

import com.ferox.game.content.raids.party.Party;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;

/**
 * An abstract handling all the logic we need for raids.
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 28, 2021
 */
public abstract class Raids {

    public abstract void startup(Player player);

    public abstract void exit(Player player);

    public abstract void complete(Party party);

    public abstract void clearParty(Player player);

    public abstract boolean death(Player player);

    public abstract Tile respawnTile(Party party, int level);

    public boolean raiding(Player player) {
        return player.raidsParty != null && player.getRaids() != null;
    }

    public abstract void addPoints(Player player, int points);

    public abstract void addDamagePoints(Player player, Npc target, int points);
}
