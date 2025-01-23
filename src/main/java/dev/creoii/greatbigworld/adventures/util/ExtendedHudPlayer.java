package dev.creoii.greatbigworld.adventures.util;

import com.google.common.collect.ImmutableMap;
import dev.creoii.greatbigworld.GreatBigWorld;
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
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@FunctionalInterface
public interface ExtendedHudPlayer {
    Map<Type, ItemInfoHud> DEFAULT = ImmutableMap.<Type, ItemInfoHud>builder()
            .put(Type.COMPASS, new ItemInfoHud(clientPlayer -> getCompassTexture(clientPlayer, getCompassTarget(clientPlayer.clientWorld, clientPlayer.getStackInHand(clientPlayer.getActiveHand())), ""), Items.COMPASS, clientPlayer -> {
                return Text.literal(StringUtils.capitalize(clientPlayer.getHorizontalFacing().getName()));
            }))
            .put(Type.RECOVERY_COMPASS, new ItemInfoHud(clientPlayer -> getCompassTexture(clientPlayer, clientPlayer.getLastDeathPos().orElse(null), "recovery_"), Items.RECOVERY_COMPASS, clientPlayer -> {
                return Text.literal(StringUtils.capitalize(clientPlayer.getHorizontalFacing().getName()));
            }))
            .put(Type.CLOCK, new ItemInfoHud(ExtendedHudPlayer::getClockTexture, Items.CLOCK, clientPlayer -> {
                return Text.literal(AdventuresClient.getDisplayTime(clientPlayer));
            }))
            // change texture based on world quadrant?
            .put(Type.ASTROLABE, new ItemInfoHud(clientPlayer -> Identifier.of(GreatBigWorld.NAMESPACE, "astrolabe"), AdventuresItems.ASTROLABE, clientPlayer -> {
                return Text.literal(clientPlayer.getBlockX() + ", " + clientPlayer.getBlockY() + ", " + clientPlayer.getBlockZ());
            }))
            .build();

    Map<Type, ItemInfoHud> gbw$getItemInfoHuds();

    private static Identifier getCompassTexture(ClientPlayerEntity clientPlayer, @Nullable GlobalPos pos, String prefix) {
        if (pos != null) {
/*
            float yaw = clientPlayer.getYaw() % 360f;
            if (yaw < 0f)
                yaw += 360f;
            return Identifier.of(GreatBigWorld.NAMESPACE, prefix + "compass_" + (int) (Math.round(yaw / 45f) % 8f));
*/
            Vec3d playerPos = clientPlayer.getPos();

            // Get the target position from GlobalPos
            BlockPos targetBlockPos = pos.pos();
            Vec3d targetPos = new Vec3d(targetBlockPos.getX() + 0.5, targetBlockPos.getY() + 0.5, targetBlockPos.getZ() + 0.5);

            // Calculate the direction vector from the player to the target
            Vec3d direction = targetPos.subtract(playerPos);

            // Calculate the yaw angle between the player's position and the target
            float targetYaw = (float) (MathHelper.atan2(direction.getZ(), direction.getX()) * (180.0 / Math.PI)) - 90.0f;

            // Normalize yaw to be between 0 and 360 degrees
            float playerYaw = clientPlayer.getYaw() % 360f;
            if (playerYaw < 0f) playerYaw += 360f;

            // Calculate relative yaw to determine the compass texture
            float relativeYaw = targetYaw - playerYaw;
            if (relativeYaw < 0f) relativeYaw += 360f;

            // Determine the compass segment (0-7) based on the relative yaw
            int segment = (int) Math.round(relativeYaw / 45f);
            segment = (segment % 8 + 8) % 8; // Normalize to 0–7

            return Identifier.of(GreatBigWorld.NAMESPACE, prefix + "compass_" + segment);
        }
        return Identifier.of(GreatBigWorld.NAMESPACE, prefix + "compass_0");
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
            return Identifier.of(GreatBigWorld.NAMESPACE, "clock_night");
        }
        return Identifier.of(GreatBigWorld.NAMESPACE, "clock_day");
    }

    enum Type {
        COMPASS,
        RECOVERY_COMPASS,
        CLOCK,
        ASTROLABE
    }
}
