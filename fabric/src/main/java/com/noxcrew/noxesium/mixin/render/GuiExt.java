package com.noxcrew.noxesium.mixin.render;

import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Gui.class)
public interface GuiExt {

    @Accessor("title")
    Component getTitle();

    @Accessor("subtitle")
    Component getSubtitle();

    @Accessor("titleTime")
    int getTitleTime();

    @Accessor("titleFadeInTime")
    int getTitleFadeInTime();

    @Accessor("titleStayTime")
    int getTitleStayTime();

    @Accessor("titleFadeOutTime")
    int getTitleFadeOutTime();

    @Accessor("overlayMessageTime")
    int getOverlayMessageTime();

    @Accessor("animateOverlayMessageColor")
    boolean getShouldAnimateOverlayMessageColor();

    @Accessor("overlayMessageString")
    Component getOverlayMessage();
}
