package com.smasher.adamsmasher;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Суммарно по 4 предметам: броня 50 (10+15+13+12), твёрдость 20 (4x5), сопротивление отбрасыванию 10 (4x2.5).
 * ВАЖНО: ваниль ограничивает атрибуты: броня <= 30, твёрдость <= 20, сопротивление отбрасыванию <= 1.0.
 */
public enum SmasherArmorMaterial implements ArmorMaterial {
    INSTANCE;

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return switch (type) {
            case BOOTS -> 13 * 60;
            case LEGGINGS -> 15 * 60;
            case CHESTPLATE -> 16 * 60;
            case HELMET -> 11 * 60;
        };
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return switch (type) {
            case BOOTS -> 12;
            case LEGGINGS -> 13;
            case CHESTPLATE -> 15;
            case HELMET -> 10;
        };
    }

    @Override public int getEnchantmentValue() { return 15; }
    @Override public SoundEvent getEquipSound() { return SoundEvents.ARMOR_EQUIP_NETHERITE; }
    @Override public Ingredient getRepairIngredient() { return Ingredient.of(Items.NETHERITE_INGOT); }
    @Override public String getName() { return SmasherMod.MODID + ":smasher"; }
    @Override public float getToughness() { return 5.0F; }
    @Override public float getKnockbackResistance() { return 2.5F; }
}
