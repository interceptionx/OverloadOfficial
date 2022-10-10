package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.util.Color;

public class VoteCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getPacketSender().sendURL("https://overload-pk.co/voting/");
        player.message("Opening https://overload-pk.co/voting/ in your web browser...");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
