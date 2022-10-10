package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

public class HydraCommand implements Command {
    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getPacketSender().sendURL("https://www.youtube.com/channel/UCwHV2oT9V7SFdnEXjqKR-FQ");
        player.message("Opening Astro's channel in your web browser...");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
