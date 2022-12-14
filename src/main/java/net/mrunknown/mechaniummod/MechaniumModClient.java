package net.mrunknown.mechaniummod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.mrunknown.mechaniummod.blocks.ModBlocks;
import net.mrunknown.mechaniummod.fluid.ModFluids;
import net.mrunknown.mechaniummod.networking.ModMessages;
import net.mrunknown.mechaniummod.screens.FluidInjectorScreen;
import net.mrunknown.mechaniummod.screens.FruitCrusherScreen;
import net.mrunknown.mechaniummod.screens.GemInfusingScreen;
import net.mrunknown.mechaniummod.screens.ModScreenHandlers;

public class MechaniumModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MECHAFRUIT_CROP, RenderLayer.getCutout());

        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_MECHAFRUIT_JUICE, ModFluids.FLOWING_MECHAFRUIT_JUICE,
                new SimpleFluidRenderHandler(
                        new Identifier("minecraft:block/water_still"),
                        new Identifier("minecraft:block/water_flow"),
                        0xA1E038D0
                ));

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(),
                ModFluids.STILL_MECHAFRUIT_JUICE, ModFluids.FLOWING_MECHAFRUIT_JUICE);

        HandledScreens.register(ModScreenHandlers.GEM_INFUSING_SCREEN_HANDLER, GemInfusingScreen::new);
        HandledScreens.register(ModScreenHandlers.FRUIT_CRUSHER_SCREEN_HANDLER, FruitCrusherScreen::new);
        HandledScreens.register(ModScreenHandlers.FLUID_INJECTOR_SCREEN_HANDLER, FluidInjectorScreen::new);

        ModMessages.registerS2CPackets();

    }


}
