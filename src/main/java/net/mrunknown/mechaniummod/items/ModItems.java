package net.mrunknown.mechaniummod.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.mrunknown.mechaniummod.MechaniumMod;

public class ModItems {

    public static final Item RAW_MECHANIUM = registerItem("raw_mechanium",
            new Item(new FabricItemSettings().group(ModItemGroup.MECHANIUM_ITEM_GROUP)));
    public static final Item MECHANIUM = registerItem("mechanium",
            new Item(new FabricItemSettings().group(ModItemGroup.MECHANIUM_ITEM_GROUP)));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registry.ITEM, new Identifier(MechaniumMod.MOD_ID, name), item);
    }
    public static void registerModItems() {
        MechaniumMod.LOGGER.debug("Registering mod items for "+ MechaniumMod.MOD_ID);
    }
}