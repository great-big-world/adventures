package dev.creoii.greatbigworld.adventures.util;

import com.google.common.collect.ImmutableMap;
import dev.creoii.greatbigworld.adventures.Adventures;
import dev.creoii.greatbigworld.adventures.client.AdventuresClient;
import dev.creoii.greatbigworld.adventures.registry.AdventuresItems;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@FunctionalInterface
public interface ExtendedHudPlayer {
    Map<Item, ItemInfoHud> DEFAULT = ImmutableMap.<Item, ItemInfoHud>builder()
            .put(Items.COMPASS, new ItemInfoHud(clientPlayer -> getCompassTexture(clientPlayer, "compass_", getCompassTarget(clientPlayer.clientWorld, clientPlayer.getStackInHand(clientPlayer.getActiveHand()))), inventory -> inventory.containsAny(stack -> stack.isOf(Items.COMPASS)), clientPlayer -> {
                return Text.literal(StringUtils.capitalize(clientPlayer.getHorizontalFacing().getName()));
            }))
            .put(Items.RECOVERY_COMPASS, new ItemInfoHud(clientPlayer -> getCompassTexture(clientPlayer, "recovery_compass_", clientPlayer.getLastDeathPos().orElse(null)), inventory -> inventory.containsAny(stack -> stack.isOf(Items.RECOVERY_COMPASS)), clientPlayer -> {
                return Text.literal(StringUtils.capitalize(clientPlayer.getHorizontalFacing().getName()));
            }))
            .put(Items.CLOCK, new ItemInfoHud(ExtendedHudPlayer::getClockTexture, inventory -> inventory.containsAny(stack -> stack.isOf(Items.CLOCK)), clientPlayer -> {
                return Text.literal(AdventuresClient.getGameTime());
            }))
            // change texture based on world quadrant?
            .put(AdventuresItems.ASTROLABE, new ItemInfoHud(clientPlayer -> new Identifier(Adventures.NAMESPACE, "astrolabe"), inventory -> inventory.containsAny(stack -> stack.isOf(AdventuresItems.ASTROLABE)), clientPlayer -> {
                return Text.literal(clientPlayer.getBlockX() + ", " + clientPlayer.getBlockY() + ", " + clientPlayer.getBlockZ());
            }))
            .build();

    Map<Item, ItemInfoHud> gbw$getItemInfoHuds();

    private static Identifier getCompassTexture(ClientPlayerEntity clientPlayer, String prefix, @Nullable GlobalPos pos) {
        if (pos != null) {
            BlockPos direction = pos.getPos().subtract(clientPlayer.getBlockPos());

            double yawRad = Math.toRadians(clientPlayer.getBodyYaw());
            double angle = Math.atan2(direction.getZ(), direction.getX()) - yawRad;

            int index = (int) Math.round(Math.toDegrees(angle) / 45) % 8;
            if (index < 0) {
                index += 8;
            }
            return new Identifier(Adventures.NAMESPACE, prefix + index);
        }
        return new Identifier(Adventures.NAMESPACE, prefix + "0");
    }

    private static GlobalPos getCompassTarget(World world, ItemStack stack) {
        if (CompassItem.hasLodestone(stack)) {
            return CompassItem.createLodestonePos(stack.getOrCreateNbt());
        }
        return CompassItem.createSpawnPos(world);
    }

    private static Identifier getClockTexture(ClientPlayerEntity clientPlayer) {
        long time = clientPlayer.clientWorld.getTimeOfDay();
        if (time >= 13000L && time <= 24000L) {
            return new Identifier(Adventures.NAMESPACE, "clock_night");
        }
        return new Identifier(Adventures.NAMESPACE, "clock_day");
    }
}
