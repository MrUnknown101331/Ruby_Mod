package net.mrunknown.mechaniummod.world.feature;

import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.mrunknown.mechaniummod.MechaniumMod;
import net.mrunknown.mechaniummod.blocks.ModBlocks;

import java.util.List;

public class ModConfiguredFeatures {

    public static final List<OreFeatureConfig.Target> OVERWORLD_MECHANIUM_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, ModBlocks.MECHANIUM_ORE.getDefaultState()),
            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_MECHANIUM_ORE.getDefaultState())
    );

    public static final List<OreFeatureConfig.Target> NETHER_MECHANIUM_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.BASE_STONE_NETHER, ModBlocks.NETHERRACK_MECHANIUM_ORE.getDefaultState())
    );

    public static final List<OreFeatureConfig.Target> END_MECHANIUM_ORES = List.of(
            OreFeatureConfig.createTarget(new BlockMatchRuleTest(Blocks.END_STONE), ModBlocks.ENDSTONE_MECHANIUM_ORE.getDefaultState())
    );


    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> MECHANIUM_ORE =
            ConfiguredFeatures.register("mechanium_ore", Feature.ORE, new OreFeatureConfig(OVERWORLD_MECHANIUM_ORES, 9));

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> NETHER_MECHANIUM_ORE =
            ConfiguredFeatures.register("nether_mechanium_ore", Feature.ORE, new OreFeatureConfig(NETHER_MECHANIUM_ORES, 9));

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> END_MECHANIUM_ORE =
            ConfiguredFeatures.register("end_mechanium_ore", Feature.ORE, new OreFeatureConfig(END_MECHANIUM_ORES, 10));

    public static void registerConfiguredFeatures() {
        MechaniumMod.LOGGER.debug("Registering the ModConfiguredFeatures for " + MechaniumMod.MOD_ID);
    }
}
