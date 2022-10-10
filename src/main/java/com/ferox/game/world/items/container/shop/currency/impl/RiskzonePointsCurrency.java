package com.ferox.game.world.items.container.shop.currency.impl;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.container.shop.currency.Currency;

public class RiskzonePointsCurrency implements Currency {

    @Override
    public boolean tangible() {
        return false;
    }

    @Override
    public boolean takeCurrency(Player player, int amount) {
        int riskzonePoints = player.getAttribOr(AttributeKey.RISKZONE_POINTS, 0);
        if (riskzonePoints >= amount) {
            player.putAttrib(AttributeKey.RISKZONE_POINTS, riskzonePoints - amount);
            return true;
        } else {
            player.message("You do not have enough riskzone points.");
            return false;
        }
    }

    @Override
    public void recieveCurrency(Player player, int amount) {
        //Empty can't receive currency from shops
    }

    @Override
    public int currencyAmount(Player player, int cost) {
        return player.getAttribOr(AttributeKey.RISKZONE_POINTS, 0);
    }

    @Override
    public boolean canRecieveCurrency(Player player) {
        return false;
    }

    @Override
    public String toString() {
        return "Riskzone points";
    }
}
