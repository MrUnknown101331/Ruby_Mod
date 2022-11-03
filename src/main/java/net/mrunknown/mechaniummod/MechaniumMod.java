package net.mrunknown.mechaniummod;

import net.fabricmc.api.ModInitializer;
import net.mrunknown.mechaniummod.blocks.ModBlocks;
import net.mrunknown.mechaniummod.blocks.entity.ModBlockEntities;
import net.mrunknown.mechaniummod.fluid.ModFluids;
import net.mrunknown.mechaniummod.items.ModItems;
import net.mrunknown.mechaniummod.screens.ModScreenHandlers;
import net.mrunknown.mechaniummod.utils.ModLootTableModifiers;
import net.mrunknown.mechaniummod.world.feature.ModConfiguredFeatures;
import net.mrunknown.mechaniummod.world.gen.ModOreGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MechaniumMod implements ModInitializer {
    public static final String MOD_ID = "mechaniummod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModConfiguredFeatures.registerConfiguredFeatures();

        ModItems.registerModItems();
        ModBlocks.registerModBlocks();

        ModOreGeneration.generateOres();

        ModLootTableModifiers.modifyLootTables();

        ModBlockEntities.registerBlockEntities();
        ModScreenHandlers.registerAllScreenHandlers();

        ModFluids.registerFluids();
    }
}
