package net.mrunknown.mechaniummod.blocks.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.mrunknown.mechaniummod.MechaniumMod;
import net.mrunknown.mechaniummod.blocks.ModBlocks;
import team.reborn.energy.api.EnergyStorage;

public class ModBlockEntities {
    public static BlockEntityType<GemInfusingBlockEntity> GEM_INFUSING_STATION;

    public static void registerBlockEntities() {
        GEM_INFUSING_STATION = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MechaniumMod.MOD_ID, "gem_infusing_station"),
                FabricBlockEntityTypeBuilder.create(GemInfusingBlockEntity::new, ModBlocks.GEM_INFUSING_STATION).build(null));

        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyStorage, GEM_INFUSING_STATION);
    }
}
