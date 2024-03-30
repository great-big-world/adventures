package dev.creoii.greatbigworld.adventures.util;

import com.google.common.collect.ImmutableMap;
import dev.creoii.greatbigworld.adventures.Adventures;
import dev.creoii.greatbigworld.adventures.client.AdventuresClient;
import dev.creoii.greatbigworld.adventures.registry.AdventuresItems;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;

@FunctionalInterface
public interface ExtendedHudPlayer {
    Map<Item, ItemInfoHud> DEFAULT = ImmutableMap.<Item, ItemInfoHud>builder()
            .put(Items.COMPASS, new ItemInfoHud(clientPlayer -> new Identifier(Adventures.NAMESPACE, "compass"), inventory -> inventory.containsAny(stack -> stack.isOf(Items.COMPASS)), clientPlayer -> {
                return Text.literal(clientPlayer.getBlockX() + ", " + clientPlayer.getBlockY() + ", " + clientPlayer.getBlockZ());
            }))
            .put(Items.RECOVERY_COMPASS, new ItemInfoHud(clientPlayer -> new Identifier(Adventures.NAMESPACE, "recovery_compass"), inventory -> inventory.containsAny(stack -> stack.isOf(Items.RECOVERY_COMPASS)), clientPlayer -> {
                Optional<GlobalPos> deathPos = clientPlayer.getLastDeathPos();
                if (deathPos.isPresent()) {
                    BlockPos pos = deathPos.get().getPos();
                    return Text.literal(pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
                }
                return Text.empty();
            }))
            .put(Items.CLOCK, new ItemInfoHud(clientPlayer -> new Identifier(Adventures.NAMESPACE, "clock_day"), inventory -> inventory.containsAny(stack -> stack.isOf(Items.CLOCK)), clientPlayer -> {
                return Text.literal(StringUtils.capitalize(clientPlayer.getHorizontalFacing().getName()));
            }))
            .put(AdventuresItems.ASTROLABE, new ItemInfoHud(clientPlayer -> new Identifier(Adventures.NAMESPACE, "astrolabe"), inventory -> inventory.containsAny(stack -> stack.isOf(AdventuresItems.ASTROLABE)), clientPlayer -> {
                return Text.literal(AdventuresClient.getGameTime());
            }))
            .build();

    Map<Item, ItemInfoHud> gbw$getItemInfoHuds();
}
