package net.mrunknown.mechaniummod.blocks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import net.mrunknown.mechaniummod.MechaniumMod;
import net.mrunknown.mechaniummod.items.ModItemGroup;

public class ModBlocks {

    public static final Block MECHANIUM_BLOCK = registerBlock("mechanium_block",
            new Block(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool().luminance(7)), ModItemGroup.MECHANIUM_ITEM_GROUP);

    public static final Block MECHANIUM_ORE = registerBlock("mechanium_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(3f).requiresTool(),
                    UniformIntProvider.create(3, 7)), ModItemGroup.MECHANIUM_ITEM_GROUP);

    public static final Block DEEPSLATE_MECHANIUM_ORE = registerBlock("deepslate_mechanium_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool(),
                    UniformIntProvider.create(3, 7)), ModItemGroup.MECHANIUM_ITEM_GROUP);

    public static final Block ENDSTONE_MECHANIUM_ORE = registerBlock("endstone_mechanium_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool(),
                    UniformIntProvider.create(3, 7)), ModItemGroup.MECHANIUM_ITEM_GROUP);

    public static final Block NETHERRACK_MECHANIUM_ORE = registerBlock("netherrack_mechanium_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f).requiresTool(),
                    UniformIntProvider.create(3, 7)), ModItemGroup.MECHANIUM_ITEM_GROUP);

    private static Item registerBlockItem(String name, Block block, ItemGroup group) {
        return Registry.register(Registry.ITEM, new Identifier(MechaniumMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(group)));
    }

    private static Block registerBlock(String name, Block block, ItemGroup group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(MechaniumMod.MOD_ID, name), block);
    }

    public static void registerModBlocks() {
        MechaniumMod.LOGGER.debug("Registering Mod Blocks for " + MechaniumMod.MOD_ID);
    }
}
