package net.mrunknown.mechaniummod.blocks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import net.mrunknown.mechaniummod.MechaniumMod;
import net.mrunknown.mechaniummod.blocks.Custom.*;
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

    public static final Block MECHANIUM_LAMP = registerBlock("mechanium_lamp",
            new MechaniumLampBlock(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool()
                    .luminance(state -> state.get(MechaniumLampBlock.LIT) ? 15 : 0)), ModItemGroup.MECHANIUM_ITEM_GROUP);

    private static Item registerBlockItem(String name, Block block, ItemGroup group) {
        return Registry.register(Registry.ITEM, new Identifier(MechaniumMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(group)));
    }

    public static final Block MECHAFRUIT_CROP = registerBlockWithoutItem("mechafruit_crop",
            new MechaFruitCropBlock(FabricBlockSettings.copy(Blocks.WHEAT)));

    public static final Block GEM_INFUSING_STATION = registerBlock("gem_infusing_station",
            new GemInfusingStationBlock(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool().nonOpaque()), ModItemGroup.MECHANIUM_ITEM_GROUP);

    public static final Block FRUIT_CRUSHER = registerBlock("fruit_crusher",
            new FruitCrusherBlock(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool().nonOpaque()), ModItemGroup.MECHANIUM_ITEM_GROUP);
    public static final Block FLUID_INJECTOR = registerBlock("fluid_injector",
            new FluidInjectorBlock(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool().nonOpaque()), ModItemGroup.MECHANIUM_ITEM_GROUP);

    private static Block registerBlock(String name, Block block, ItemGroup group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(MechaniumMod.MOD_ID, name), block);
    }

    private static Block registerBlockWithoutItem(String name, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(MechaniumMod.MOD_ID, name), block);
    }

    public static void registerModBlocks() {
        MechaniumMod.LOGGER.debug("Registering Mod Blocks for " + MechaniumMod.MOD_ID);
    }
}
