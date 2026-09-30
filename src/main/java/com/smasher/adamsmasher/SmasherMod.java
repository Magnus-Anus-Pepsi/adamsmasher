package com.smasher.adamsmasher;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(SmasherMod.MODID)
public class SmasherMod {
    public static final String MODID = "adamsmasher";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> HELMET =
            ITEMS.register("smasher_helmet", () -> new SmasherArmorItem(ArmorItem.Type.HELMET));
    public static final RegistryObject<Item> CHESTPLATE =
            ITEMS.register("smasher_chestplate", () -> new SmasherArmorItem(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<Item> LEGGINGS =
            ITEMS.register("smasher_leggings", () -> new SmasherArmorItem(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<Item> BOOTS =
            ITEMS.register("smasher_boots", () -> new SmasherArmorItem(ArmorItem.Type.BOOTS));

    public SmasherMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        bus.addListener(this::commonSetup);
        bus.addListener(this::addToCreativeTab);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModNetwork::register);
    }

    private void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(HELMET);
            event.accept(CHESTPLATE);
            event.accept(LEGGINGS);
            event.accept(BOOTS);
        }
    }
}
