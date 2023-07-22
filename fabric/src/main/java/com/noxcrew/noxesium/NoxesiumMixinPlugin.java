package com.noxcrew.noxesium;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/**
 * Enables certain mixins based on whether other mods are in use or not.
 */
public class NoxesiumMixinPlugin implements IMixinConfigPlugin {

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return switch (mixinClassName) {
            // Enable custom sodium compatibility for the beacon performance changes. Iris does already have
            // changes that are comparable in performance but we still enable the patch so it gets noticed
            // when it breaks. Otherwise, just non-Iris Sodium clients would be a very small group.
            case "com.noxcrew.noxesium.mixin.client.beacon.SodiumWorldRendererMixin" ->
                    FabricLoader.getInstance().isModLoaded("sodium");
            // We don't disable the other beacon patches as they simply get made useless by Sodium removing
            // all default global block entities.
            default -> true;
        };
    }

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
