package dev.creoii.greatbigworld.adventures.util;

import com.google.common.collect.ImmutableMap;
import dev.creoii.greatbigworld.adventures.Adventures;
import dev.creoii.greatbigworld.adventures.client.AdventuresClient;
import dev.creoii.greatbigworld.adventures.registry.AdventuresItems;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.item.CompassItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@FunctionalInterface
public interface ExtendedHudPlayer {
    Map<Type, ItemInfoHud> DEFAULT = ImmutableMap.<Type, ItemInfoHud>builder()
            .put(Type.COMPASS, new ItemInfoHud(clientPlayer -> getCompassTexture(clientPlayer, "compass_", getCompassTarget(clientPlayer.clientWorld, clientPlayer.getStackInHand(clientPlayer.getActiveHand()))), Items.COMPASS, clientPlayer -> {
                return Text.literal(StringUtils.capitalize(clientPlayer.getHorizontalFacing().getName()));
            }))
            .put(Type.RECOVERY_COMPASS, new ItemInfoHud(clientPlayer -> getCompassTexture(clientPlayer, "recovery_compass_", clientPlayer.getLastDeathPos().orElse(null)), Items.RECOVERY_COMPASS, clientPlayer -> {
                return Text.literal(StringUtils.capitalize(clientPlayer.getHorizontalFacing().getName()));
            }))
            .put(Type.CLOCK, new ItemInfoHud(ExtendedHudPlayer::getClockTexture, Items.CLOCK, clientPlayer -> {
                return Text.literal(AdventuresClient.getDisplayTime(clientPlayer));
            }))
            // change texture based on world quadrant?
            .put(Type.ASTROLABE, new ItemInfoHud(clientPlayer -> new Identifier(Adventures.NAMESPACE, "astrolabe"), AdventuresItems.ASTROLABE, clientPlayer -> {
                return Text.literal(clientPlayer.getBlockX() + ", " + clientPlayer.getBlockY() + ", " + clientPlayer.getBlockZ());
            }))
            .build();

    Map<Type, ItemInfoHud> gbw$getItemInfoHuds();

    private static Identifier getCompassTexture(ClientPlayerEntity clientPlayer, String prefix, @Nullable GlobalPos pos) {
        if (pos != null) {
            float yaw = clientPlayer.getYaw() % 360;
            if (yaw < 0)
                yaw += 360;
            return new Identifier(Adventures.NAMESPACE, prefix + (Math.round(yaw / 45) % 8));
        }
        return new Identifier(Adventures.NAMESPACE, prefix + "0");
    }

    private static GlobalPos getCompassTarget(World world, ItemStack stack) {
        LodestoneTrackerComponent component = stack.get(DataComponentTypes.LODESTONE_TRACKER);
        if (component != null && component.target().isPresent()) {
            return component.target().get();
        }
        return CompassItem.createSpawnPos(world);
    }

    private static Identifier getClockTexture(ClientPlayerEntity clientPlayer) {
        long time = clientPlayer.clientWorld.getTimeOfDay();
        if (time >= 13000L) {
            return new Identifier(Adventures.NAMESPACE, "clock_night");
        }
        return new Identifier(Adventures.NAMESPACE, "clock_day");
    }

    enum Type {
        COMPASS,
        RECOVERY_COMPASS,
        CLOCK,
        ASTROLABE
    }
}
