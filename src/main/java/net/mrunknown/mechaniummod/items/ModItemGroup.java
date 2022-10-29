package net.mrunknown.mechaniummod.items;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.mrunknown.mechaniummod.MechaniumMod;

public class ModItemGroup {
    public static final ItemGroup MECHANIUM_ITEM_GROUP = FabricItemGroupBuilder.build(
            new Identifier(MechaniumMod.MOD_ID, "mechanium_item_group"), () -> new ItemStack(ModItems.MECHANIUM));
}
