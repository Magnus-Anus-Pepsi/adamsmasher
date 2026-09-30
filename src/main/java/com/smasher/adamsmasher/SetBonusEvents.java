package com.smasher.adamsmasher;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Бонусы полного сета: размер x1.5, +20 HP (1 полоска), Скорость II, Регенерация II. */
@Mod.EventBusSubscriber(modid = SmasherMod.MODID)
public class SetBonusEvents {
    public static final float SCALE = 1.5F;
    public static final int SPEED_AMPLIFIER = 1;  // 1 = Скорость II
    public static final int REGEN_AMPLIFIER = 1;  // 1 = Регенерация II
    public static final double EXTRA_HEALTH = 20.0; // 20 HP = 10 сердец = 1 полная полоска

    private static final UUID HEALTH_UUID = UUID.fromString("a3b1c0de-5a11-4f2e-9d3c-7b1e0a5d1234");

    /** Игроки в полном сете (отдельно для клиента и сервера — разные объекты Player). */
    public static final Set<Player> SCALED =
            Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        Player p = e.player;
        boolean full = SmasherSet.hasFullSet(p);
        boolean was = SCALED.contains(p);

        if (full != was) {
            if (full) SCALED.add(p); else SCALED.remove(p);
            p.refreshDimensions(); // пересчитать хитбокс
            if (!full && !p.level().isClientSide) {
                p.removeEffect(MobEffects.MOVEMENT_SPEED);
                p.removeEffect(MobEffects.REGENERATION);
            }
        }

        if (p.level().isClientSide) return;

        AttributeInstance hp = p.getAttribute(Attributes.MAX_HEALTH);
        if (hp == null) return;

        if (full) {
            if (hp.getModifier(HEALTH_UUID) == null) {
                hp.addPermanentModifier(new AttributeModifier(
                        HEALTH_UUID, "Smasher set health", EXTRA_HEALTH, AttributeModifier.Operation.ADDITION));
            }
            refresh(p, MobEffects.MOVEMENT_SPEED, SPEED_AMPLIFIER);
            refresh(p, MobEffects.REGENERATION, REGEN_AMPLIFIER);
        } else if (hp.getModifier(HEALTH_UUID) != null) {
            hp.removeModifier(HEALTH_UUID);
            if (p.getHealth() > p.getMaxHealth()) p.setHealth(p.getMaxHealth());
        }
    }

    /** Не обновляем эффект каждый тик — иначе регенерация не успевает "тикнуть". */
    private static void refresh(Player p, net.minecraft.world.effect.MobEffect effect, int amp) {
        MobEffectInstance cur = p.getEffect(effect);
        if (cur == null || cur.getAmplifier() < amp || cur.getDuration() <= 20) {
            p.addEffect(new MobEffectInstance(effect, 100, amp, true, false, true));
        }
    }

    @SubscribeEvent
    public static void onSize(EntityEvent.Size e) {
        if (e.getEntity() instanceof Player p && SCALED.contains(p)) {
            e.setNewSize(e.getNewSize().scale(SCALE));
            e.setNewEyeHeight(e.getNewEyeHeight() * SCALE);
        }
    }
}
