package dev.creoii.greatbigworld.adventures.util;

import com.google.common.collect.ImmutableMap;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.adventures.client.AdventuresClient;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@FunctionalInterface
public interface ExtendedHudPlayer {
    Map<Type, ItemInfoHud> DEFAULT = ImmutableMap.<Type, ItemInfoHud>builder()
            .put(Type.COMPASS, new ItemInfoHud(clientPlayer -> getCompassTexture(clientPlayer, getCompassTarget(clientPlayer.level(), clientPlayer.getItemInHand(clientPlayer.getUsedItemHand())), ""), Items.COMPASS, clientPlayer -> {
                return Component.literal(StringUtils.capitalize(clientPlayer.getDirection().getName()));
            }))
            .put(Type.RECOVERY_COMPASS, new ItemInfoHud(clientPlayer -> getCompassTexture(clientPlayer, clientPlayer.getLastDeathLocation().orElse(null), "recovery_"), Items.RECOVERY_COMPASS, clientPlayer -> {
                return Component.literal(StringUtils.capitalize(clientPlayer.getDirection().getName()));
            }))
            .put(Type.CLOCK, new ItemInfoHud(ExtendedHudPlayer::getClockTexture, Items.CLOCK, clientPlayer -> {
                return Component.literal(AdventuresClient.getDisplayTime(clientPlayer));
            }))
            .build();

    Map<Type, ItemInfoHud> gbw$getItemInfoHuds();

    private static Identifier getCompassTexture(LocalPlayer clientPlayer, @Nullable GlobalPos pos, String prefix) {
        if (pos != null) {
/*
            float yaw = clientPlayer.getYaw() % 360f;
            if (yaw < 0f)
                yaw += 360f;
            return Identifier.of(GreatBigWorld.NAMESPACE, prefix + "compass_" + (int) (Math.round(yaw / 45f) % 8f));
*/
            Vec3 playerPos = clientPlayer.position();

            BlockPos targetBlockPos = pos.pos();
            Vec3 targetPos = new Vec3(targetBlockPos.getX() + .5d, targetBlockPos.getY() + .5d, targetBlockPos.getZ() + .5d);

            Vec3 direction = targetPos.subtract(playerPos);

            int segment = getSegment(clientPlayer, direction);

            return Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, prefix + "compass_" + segment);
        }
        return Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, prefix + "compass_0");
    }

    private static int getSegment(LocalPlayer clientPlayer, Vec3 direction) {
        float targetYaw = (float) (Mth.atan2(direction.z(), direction.x()) * (180f / Math.PI)) - 90f;

        float playerYaw = clientPlayer.getYRot() % 360f;
        if (playerYaw < 0f) playerYaw += 360f;

        float relativeYaw = targetYaw - playerYaw;
        if (relativeYaw < 0f) relativeYaw += 360f;

        int segment = Math.round(relativeYaw / 45f);
        segment = (segment % 8 + 8) % 8;
        return segment;
    }

    private static GlobalPos getCompassTarget(Level world, ItemStack stack) {
        LodestoneTracker component = stack.get(DataComponents.LODESTONE_TRACKER);
        if (component != null && component.target().isPresent()) {
            return component.target().get();
        }
        return GlobalPos.of(world.dimension(), world.getRespawnData().pos());
    }

    private static Identifier getClockTexture(LocalPlayer clientPlayer) {
        long time = clientPlayer.level().getDayTime();
        if (time >= 13000L) {
            return Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "clock_night");
        }
        return Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "clock_day");
    }

    enum Type {
        COMPASS,
        RECOVERY_COMPASS,
        CLOCK,
        ASTROLABE
    }
}
