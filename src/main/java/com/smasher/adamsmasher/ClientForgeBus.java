package com.smasher.adamsmasher;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SmasherMod.MODID, value = Dist.CLIENT)
public class ClientForgeBus {
    private static boolean rendering = false;

    /** Клавиша C: направление зависит от зажатых W/A/S/D (без них — рывок вперёд). */
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        while (ClientModBus.DASH_KEY.consumeClick()) {
            if (mc.screen != null || !SmasherSet.hasFullSet(mc.player)) continue;
            Options o = mc.options;
            int dir = 0;
            if (o.keyDown.isDown()) dir = 1;
            else if (o.keyLeft.isDown()) dir = 2;
            else if (o.keyRight.isDown()) dir = 3;
            ModNetwork.CHANNEL.sendToServer(new DashPacket(dir));
        }
    }

    /** Подмена скина + масштаб x1.5 при полном сете. */
    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre e) {
        if (rendering || ClientModBus.renderer == null) return;
        if (!(e.getEntity() instanceof AbstractClientPlayer player) || !SmasherSet.hasFullSet(player)) return;

        e.setCanceled(true);
        rendering = true;
        try {
            PoseStack ps = e.getPoseStack();
            ps.pushPose();
            ps.scale(SetBonusEvents.SCALE, SetBonusEvents.SCALE, SetBonusEvents.SCALE);
            float yaw = Mth.lerp(e.getPartialTick(), player.yRotO, player.getYRot());
            ClientModBus.renderer.render(player, yaw, e.getPartialTick(), ps,
                    e.getMultiBufferSource(), e.getPackedLight());
            ps.popPose();
        } finally {
            rendering = false;
        }
    }

    /** Руки от первого лица тоже со скином Смэшера. */
    @SubscribeEvent
    public static void onRenderArm(RenderArmEvent e) {
        AbstractClientPlayer player = e.getPlayer();
        if (ClientModBus.renderer == null || !SmasherSet.hasFullSet(player)) return;
        e.setCanceled(true);
        if (e.getArm() == HumanoidArm.RIGHT) {
            ClientModBus.renderer.renderRightHand(e.getPoseStack(), e.getMultiBufferSource(), e.getPackedLight(), player);
        } else {
            ClientModBus.renderer.renderLeftHand(e.getPoseStack(), e.getMultiBufferSource(), e.getPackedLight(), player);
        }
    }
}
