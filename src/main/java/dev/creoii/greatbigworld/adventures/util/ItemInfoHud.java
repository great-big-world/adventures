package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ItemInfoHud {
    private final Function<ClientPlayerEntity, Identifier> iconId;
    private final Item renderItem;
    private final Function<ClientPlayerEntity, Text> text;
    private boolean active = false;

    public ItemInfoHud(Function<ClientPlayerEntity, Identifier> iconId, Item renderItem, Function<ClientPlayerEntity, Text> text) {
        this.iconId = iconId;
        this.renderItem = renderItem;
        this.text = text;
    }

    public Identifier getIconId(ClientPlayerEntity clientPlayer) {
        return iconId.apply(clientPlayer);
    }

    public boolean canRender(ClientPlayerEntity clientPlayer) {
        return isActive() && (clientPlayer.getInventory().containsAny(stack -> stack.isOf(renderItem)) || clientPlayer.currentScreenHandler.getCursorStack().isOf(renderItem));
    }

    public Text getText(ClientPlayerEntity clientPlayer) {
        return text.apply(clientPlayer);
    }

    public boolean isActive() {
        return active;
    }

    public void invert() {
        active = !active;
    }
}
