package com.smasher.adamsmasher;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class SmasherArmorItem extends ArmorItem {
    public SmasherArmorItem(ArmorItem.Type type) {
        super(SmasherArmorMaterial.INSTANCE, type, new Item.Properties().fireResistant().rarity(Rarity.EPIC));
    }
}
