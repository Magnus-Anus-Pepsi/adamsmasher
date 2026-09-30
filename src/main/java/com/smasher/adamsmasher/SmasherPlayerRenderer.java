package com.smasher.adamsmasher;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;

/** Рендерер игрока с подменённым скином Адама Смэшера. */
public class SmasherPlayerRenderer extends PlayerRenderer {
    public static final ResourceLocation SKIN =
            new ResourceLocation(SmasherMod.MODID, "textures/entity/adam_smasher.png");

    public SmasherPlayerRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, false); // false = классические руки (4 px); true = slim
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractClientPlayer player) {
        return SKIN;
    }
}
