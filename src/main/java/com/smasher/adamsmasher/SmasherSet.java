package com.smasher.adamsmasher;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public final class SmasherSet {
    private SmasherSet() {}

    public static boolean hasFullSet(LivingEntity e) {
        return e.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof SmasherArmorItem
                && e.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof SmasherArmorItem
                && e.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof SmasherArmorItem
                && e.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof SmasherArmorItem;
    }
}
