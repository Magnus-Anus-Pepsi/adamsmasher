package com.smasher.adamsmasher;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SmasherMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModBus {
    public static final KeyMapping DASH_KEY = new KeyMapping(
            "key.adamsmasher.dash", KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, "key.categories.adamsmasher");

    public static SmasherPlayerRenderer renderer;

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent e) {
        e.register(DASH_KEY);
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers e) {
        renderer = new SmasherPlayerRenderer(e.getContext());
    }
}
