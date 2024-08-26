package dev.creoii.greatbigworld.adventures;

import dev.creoii.creoapi.api.item.CreoItemApi;
import dev.creoii.greatbigworld.adventures.component.JournalContentComponent;
import dev.creoii.greatbigworld.adventures.registry.AdventuresGameRules;
import dev.creoii.greatbigworld.adventures.registry.AdventuresItems;
import dev.creoii.greatbigworld.adventures.util.ExtendedHudPlayer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;

import java.util.ArrayList;

public class Adventures implements ModInitializer {
    public static final String NAMESPACE = "great_big_world";
    public static final DataComponentType<JournalContentComponent> JOURNAL_CONTENT = DataComponentType.<JournalContentComponent>builder().codec(JournalContentComponent.CODEC).packetCodec(JournalContentComponent.PACKET_CODEC).cache().build();

    @Override
    public void onInitialize() {
        AdventuresItems.register();
        AdventuresGameRules.register();
        Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier(CreoItemApi.NAMESPACE, "journal_content"), JOURNAL_CONTENT);

        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (world.isClient && !player.isSpectator() && player instanceof ExtendedHudPlayer extendedHudPlayer) {
                ItemStack stack = player.getStackInHand(hand);
                if (stack.isOf(Items.RECOVERY_COMPASS)) {
                    extendedHudPlayer.gbw$getItemInfoHuds().get(Items.RECOVERY_COMPASS).invert();
                    return TypedActionResult.success(stack);
                } else if (stack.isOf(Items.CLOCK)) {
                    extendedHudPlayer.gbw$getItemInfoHuds().get(Items.CLOCK).invert();
                    return TypedActionResult.success(stack);
                }
            }
            return TypedActionResult.pass(ItemStack.EMPTY);
        });

        DefaultItemComponentEvents.MODIFY.register(context -> {
            context.modify(AdventuresItems.JOURNAL, builder -> {
                builder.add(JOURNAL_CONTENT, new JournalContentComponent(new ArrayList<>()));
            });
        });
    }
}
