package net.mrunknown.mechaniummod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.mrunknown.mechaniummod.blocks.ModBlocks;

public class MechaniumModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MECHAFRUIT_CROP, RenderLayer.getCutout());
    }
}
