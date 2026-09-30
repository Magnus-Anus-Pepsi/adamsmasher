package com.smasher.adamsmasher;

import java.util.function.Supplier;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

/** dir: 0 = вперёд, 1 = назад, 2 = влево, 3 = вправо. */
public record DashPacket(int dir) {
    public static final double DISTANCE = 5.0;
    public static final int COOLDOWN_TICKS = 20;

    public static void encode(DashPacket msg, FriendlyByteBuf buf) {
        buf.writeByte(msg.dir);
    }

    public static DashPacket decode(FriendlyByteBuf buf) {
        return new DashPacket(buf.readByte());
    }

    public static void handle(DashPacket msg, Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = ctxSup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) perform(player, msg.dir);
        });
        ctx.setPacketHandled(true);
    }

    private static void perform(ServerPlayer p, int dir) {
        if (!SmasherSet.hasFullSet(p)) return;
        if (p.getCooldowns().isOnCooldown(SmasherMod.CHESTPLATE.get())) return;

        float yaw = p.getYRot() * Mth.DEG_TO_RAD;
        Vec3 fwd = new Vec3(-Mth.sin(yaw), 0, Mth.cos(yaw));
        Vec3 right = new Vec3(-fwd.z, 0, fwd.x);
        Vec3 d = switch (dir) {
            case 1 -> fwd.scale(-1);
            case 2 -> right.scale(-1);
            case 3 -> right;
            default -> fwd;
        };

        // идём шагами по 0.25 блока, пока нет столкновения (максимум 5 блоков)
        AABB box = p.getBoundingBox();
        double travelled = 0;
        for (double t = 0.25; t <= DISTANCE + 1.0E-6; t += 0.25) {
            if (!p.level().noCollision(p, box.move(d.scale(t)))) break;
            travelled = t;
        }
        if (travelled <= 0) return;

        ServerLevel level = p.serverLevel();
        // след из частиц
        for (double t = 0; t <= travelled; t += 0.5) {
            level.sendParticles(ParticleTypes.CLOUD,
                    p.getX() + d.x * t, p.getY() + p.getBbHeight() * 0.5, p.getZ() + d.z * t,
                    3, 0.2, 0.4, 0.2, 0.01);
        }
        level.playSound(null, p.blockPosition(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 0.8F, 1.4F);

        p.connection.teleport(p.getX() + d.x * travelled, p.getY(), p.getZ() + d.z * travelled,
                p.getYRot(), p.getXRot());
        p.resetFallDistance();
        p.getCooldowns().addCooldown(SmasherMod.CHESTPLATE.get(), COOLDOWN_TICKS);
    }
}
