package net.mrunknown.rubymod.items;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.mrunknown.rubymod.RubyMod;

public class ModItemGroup {
    public static final ItemGroup RUBY_ITEM_GROUP = FabricItemGroupBuilder.build(
            new Identifier(RubyMod.MOD_ID, "ruby_item_group"), () -> new ItemStack(ModItems.RUBY));
}
