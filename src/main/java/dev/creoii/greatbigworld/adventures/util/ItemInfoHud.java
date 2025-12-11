package dev.creoii.greatbigworld.adventures.util;

import java.util.function.Function;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class ItemInfoHud {
    private final Function<LocalPlayer, Identifier> iconId;
    private final Item renderItem;
    private final Function<LocalPlayer, Component> text;
    private boolean active = false;

    public ItemInfoHud(Function<LocalPlayer, Identifier> iconId, Item renderItem, Function<LocalPlayer, Component> text) {
        this.iconId = iconId;
        this.renderItem = renderItem;
        this.text = text;
    }

    public Identifier getIconId(LocalPlayer clientPlayer) {
        return iconId.apply(clientPlayer);
    }

    public boolean canRender(LocalPlayer clientPlayer) {
        return isActive() && (clientPlayer.getInventory().hasAnyMatching(stack -> stack.is(renderItem)) || clientPlayer.containerMenu.getCarried().is(renderItem));
    }

    public Component getText(LocalPlayer clientPlayer) {
        return text.apply(clientPlayer);
    }

    public boolean isActive() {
        return active;
    }

    public void invert() {
        active = !active;
    }
}
