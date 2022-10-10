package com.ferox.game.content;

import com.ferox.game.content.achievements.Achievements;
import com.ferox.game.content.achievements.AchievementsManager;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.combat.skull.SkullType;
import com.ferox.game.world.entity.combat.skull.Skulling;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;

import static com.ferox.game.content.collection_logs.LogType.KEYS;
import static com.ferox.util.CustomItemIdentifiers.MOLTEN_KEY;

public class MoltenKey extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if (object.getId() == 170 && option == 1) {
            if (player.inventory().contains(MOLTEN_KEY)){
                player.teleblock( 250, true);
                Skulling.assignSkullState(player, SkullType.RED_SKULL);
                if (player.hasPetOut("Deranged archaeologist")) {
                    Skulling.assignSkullState(player, SkullType.NO_SKULL);
                }
                player.inventory().remove(new Item(MOLTEN_KEY));
                player.message("You unlock the chest with your key.");
                player.sound(51);
                player.animate(536);
                Chain.bound(null).runFn(1, () -> {
                    GameObject old = new GameObject(170, object.tile(), object.getType(), object.getRotation());
                    GameObject spawned = new GameObject(171, object.tile(), object.getType(), object.getRotation());
                    ObjectManager.replace(old, spawned, 2);
                    int roll = Utils.percentageChance(player.extraItemRollChance()) ? 2 : 1;
                    for (int i = 0; i < roll; i++) {
                        reward(player);
                    }
                });
            } else {
                player.message("You need a molten key to open this chest.");
            }
            return true;
        }
        return false;
    }

    public boolean handleObjectInteraction(Player player, GameObject object) {
        if (object.getId() == 170) {
            if (player.inventory().contains(MOLTEN_KEY)){
                player.teleblock( 250, true);
                Skulling.assignSkullState(player, SkullType.RED_SKULL);
                if (player.hasPetOut("Deranged archaeologist")) {
                    Skulling.assignSkullState(player, SkullType.NO_SKULL);
                }
                player.inventory().remove(new Item(MOLTEN_KEY));
                player.message("You unlock the chest with your key.");
                player.sound(51);
                player.animate(536);
                Chain.bound(null).runFn(1, () -> {
                    GameObject old = new GameObject(170, object.tile(), object.getType(), object.getRotation());
                    GameObject spawned = new GameObject(171, object.tile(), object.getType(), object.getRotation());
                    ObjectManager.replace(old, spawned, 2);
                    int roll = Utils.percentageChance(player.extraItemRollChance()) ? 2 : 1;
                    for (int i = 0; i < roll; i++) {
                        reward(player);
                    }
                });
            } else {
                player.message("You need a molten key to open this chest.");
            }
            return true;
        }
        return false;
    }
    private enum PvpRewards {
        FIRST(80, new Item[]{new Item(ItemIdentifiers.CRYSTAL_KEY)}),
        SECOND(75, new Item[]{new Item(CustomItemIdentifiers.BLOOD_MONEY_CASKET)}),
        THIRD(70, new Item[]{new Item(ItemIdentifiers.DRAGON_BOOTS)}),
        FOURTH(60, new Item[]{new Item(ItemIdentifiers.DRAGONFIRE_SHIELD)}),
        FIFTH(50, new Item[]{new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX)}),
        SIXTH(40, new Item[]{new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX)}),
        EIGHT(30, new Item[]{new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX)}),
        NINTH(20, new Item[]{new Item(ItemIdentifiers.DRAGON_CROSSBOW)}),
        TENTH(10, new Item[]{new Item(ItemIdentifiers.DRAGON_SCIMITAR_OR)}),
        ELEVENTH(2, new Item[]{new Item(CustomItemIdentifiers.PET_MYSTERY_BOX)}),
        TWELFTH(2, new Item[]{new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX)}),
        THIRTEENTH(2, new Item[]{new Item(ItemIdentifiers.INFERNAL_CAPE)}),
        FOURTEENTH(2, new Item[]{new Item(CustomItemIdentifiers.MOLTEN_DEFENDER)});
        private final int chance;
        private final Item[] rewards;

        PvpRewards(int chance, Item[] rewards) {
            this.chance = chance;
            this.rewards = rewards;
        }
    }
    private static void reward(Player player) {
        Item[] rewards = generateReward();

        Item drop = null;

        for (Item item : rewards) {
            player.getInventory().addOrDrop(item);
            KEYS.log(player, MOLTEN_KEY, item);
            drop = item;
        }

        if(drop != null) {
            //The user box test doesn't yell.
            if(player.getUsername().equalsIgnoreCase("Box test")) {
                return;
            }
            System.out.println(drop.toString());
            World.getWorld().sendWorldMessage(player.getUsername() + " received <shad><col=FF0000>" + drop.name() + " </shad></col>from a Molten key.");
        }
        int keysUsed = player.<Integer>getAttribOr(AttributeKey.MOLTEN_KEYS_OPENED, 0) + 1;
        player.putAttrib(AttributeKey.MOLTEN_KEYS_OPENED, keysUsed);

        player.message("You find some treasure in the chest!");
        AchievementsManager.activate(player, Achievements.MOLTEN_LOOTER_I, 1);
        AchievementsManager.activate(player, Achievements.MOLTEN_LOOTER_II, 1);
        AchievementsManager.activate(player, Achievements.MOLTEN_LOOTER_III, 1);
    }
    private static Item[] generateReward() {
        int randomReward = Utils.random(80);
        Item[] rewardItem;
        for (MoltenKey.PvpRewards reward : MoltenKey.PvpRewards.values()) {
            if (reward.chance <= randomReward) {
                return reward.rewards;
            }
        }
        rewardItem = MoltenKey.PvpRewards.FIRST.rewards;
        return rewardItem;
    }
}



