package com.ferox.game.content.items;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import com.ferox.util.Utils;

import java.util.Arrays;
import java.util.List;

import static com.ferox.game.content.collection_logs.LogType.MYSTERY_BOX;
import static com.ferox.game.world.entity.AttributeKey.MYSTERY_CHESTS_OPENED;
import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | March, 21, 2021, 23:49
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class MysteryChest extends PacketInteraction {

    //1/10
    private final List<Item> RARE_REWARDS = Arrays.asList(
        new Item(ELYSIAN_SPIRIT_SHIELD), new Item(TWISTED_BOW), new Item(SCYTHE_OF_VITUR), new Item(DARK_BANDOS_CHESTPLATE), new Item(DARK_BANDOS_TASSETS), new Item(ANCIENT_VESTAS_LONGSWORD), new Item(ANCIENT_STATIUSS_WARHAMMER),
        new Item(TWISTED_ANCESTRAL_ROBE_TOP), new Item(TWISTED_ANCESTRAL_ROBE_BOTTOM), new Item(DRAGON_HUNTER_CROSSBOW_T));

    //1/5
    private final List<Item> UNCOMMON_REWARDS = Arrays.asList(
        new Item(CORRUPTED_BOOTS), new Item(RING_OF_TRINITY), new Item(ANCIENT_WARRIOR_AXE_C), new Item(ANCIENT_WARRIOR_MAUL_C), new Item(ANCIENT_WARRIOR_SWORD_C),
        new Item(DARK_ARMADYL_CHESTPLATE), new Item(DARK_ARMADYL_CHAINSKIRT));

    //rolls under 5
    private final List<Item> COMMON_REWARDS = Arrays.asList(
        new Item(RING_OF_MANHUNTING), new Item(RING_OF_PRECISION), new Item(RING_OF_SORCERY), new Item(ANCIENT_WARRIOR_AXE), new Item(ANCIENT_WARRIOR_MAUL), new Item(ANCIENT_WARRIOR_SWORD), new Item(ANCIENT_WARRIOR_CLAMP), new Item(DARK_ARMADYL_HELMET), new Item(TWISTED_ANCESTRAL_HAT));


    private void rollForReward(Player player) {
        Item reward;
        //Roll for a random item
        reward = rewardRoll();
        MYSTERY_BOX.log(player, MYSTERY_CHEST, reward);
        player.inventory().addOrBank(reward);

        String chest = "Enchanted grand chest";

        if (reward != null) {
            Utils.sendDiscordInfoLog("Player " + player.getUsername() + " received a " + reward.name() + " from an Enchanted grand chest.", "box_and_tickets");
            var timesOpened = player.<Integer>getAttribOr(MYSTERY_CHESTS_OPENED, 0) + 1;
            player.putAttrib(MYSTERY_CHESTS_OPENED, timesOpened);
            //The user box test doesn't yell.
            if (player.getUsername().equalsIgnoreCase("Box test")) {
                return;
            }
            String worldMessage = "<img=1863><shad=0>[<col=" + Color.RED.getColorValue() + ">" + chest + "</col>]</shad=0>:<col=FF0000> " + player.getUsername() + " received a <shad=0>" + reward.name() + Color.BLACK.tag() + "!";
            World.getWorld().sendWorldMessage(worldMessage);
        }
    }

    private static final int RARE_ROLL = 10;
    private static final int UNCOMMON_ROLL = 5;

    private Item rewardRoll() {
        if (Utils.rollDie(RARE_ROLL, 1)) {
            return Utils.randomElement(RARE_REWARDS);
        } else if (Utils.rollDie(UNCOMMON_ROLL, 1)) {
            return Utils.randomElement(UNCOMMON_REWARDS);
        } else {
            return Utils.randomElement(COMMON_REWARDS);
        }
    }

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if (option == 1) {
            if (item.getId() == MYSTERY_CHEST) {
                //Safety make sure people aren't spoofing using cheat clients
                if (player.inventory().contains(MYSTERY_CHEST)) {
                    player.inventory().remove(new Item(MYSTERY_CHEST), true);
                    rollForReward(player);
                }
            }
            if(item.getId() == HWEEN_MYSTERY_CHEST) {
                //Safety make sure people aren't spoofing using cheat clients
                if (player.inventory().contains(HWEEN_MYSTERY_CHEST)) {
                    player.inventory().remove(new Item(HWEEN_MYSTERY_CHEST), true);
                    rollForReward(player);
                }
                return true;
            }
        }
        return false;
    }
}
