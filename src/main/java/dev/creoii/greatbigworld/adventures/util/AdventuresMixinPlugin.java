package dev.creoii.greatbigworld.adventures.util;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdventuresMixinPlugin implements IMixinConfigPlugin {
    private static final Map<String, String> MOD_DEPENDENT_MIXINS = new ImmutableMap.Builder<String, String>()
            .put("dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.sodium.SodiumGameOptionPagesMixin", "sodium")
            .build();

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (MOD_DEPENDENT_MIXINS.containsKey(mixinClassName)) {
            return FabricLoader.getInstance().isModLoaded(MOD_DEPENDENT_MIXINS.get(mixinClassName));
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}