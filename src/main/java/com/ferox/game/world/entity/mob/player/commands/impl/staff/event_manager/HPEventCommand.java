package com.ferox.game.world.entity.mob.player.commands.impl.staff.event_manager;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.util.Color;
import com.ferox.util.Utils;

public class HPEventCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (parts.length < 2) {
            player.message("Invalid syntax. Please use: ::hpevent [location]");
            player.message("Example: ::hpevent mage bank");
            return;
        }

        StringBuilder msg = new StringBuilder();
        for (int i = 1; i < parts.length; i++) {
            msg.append(parts[i]).append(" ");
        }

        player.skills().hpEventLevel(10_000);
        player.putAttrib(AttributeKey.HP_EVENT_ACTIVE,true);
        World.getWorld().sendWorldMessage("<img=1081> "+ Color.PURPLE.wrap(player.getUsername())+" "+Color.RED.wrap("is starting a HP Event at "+msg));
        World.getWorld().sendBroadcast("<img=1081> "+ Utils.capitalizeFirst(player.getUsername())+" is starting a HP Event at "+msg);
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isEventManager(player) || player.getPlayerRights().isYoutuber(player));
    }

}
