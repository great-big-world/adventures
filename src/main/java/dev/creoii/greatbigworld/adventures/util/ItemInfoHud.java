package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Function;
import java.util.function.Predicate;

public class ItemInfoHud {
    private final Function<ClientPlayerEntity, Identifier> iconId;
    private final Predicate<PlayerInventory> canRender;
    private final Function<ClientPlayerEntity, Text> text;
    private boolean active = false;

    public ItemInfoHud(Function<ClientPlayerEntity, Identifier> iconId, Predicate<PlayerInventory> canRender, Function<ClientPlayerEntity, Text> text) {
        this.iconId = iconId;
        this.canRender = canRender;
        this.text = text;
    }

    public Identifier getIconId(ClientPlayerEntity clientPlayer) {
        return iconId.apply(clientPlayer);
    }

    public boolean canRender(PlayerInventory inventory) {
        return isActive() && canRender.test(inventory);
    }

    public Text getText(ClientPlayerEntity clientPlayer) {
        return text.apply(clientPlayer);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void invert() {
        active = !active;
    }
}
