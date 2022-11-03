package net.mrunknown.mechaniummod.fluid;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.mrunknown.mechaniummod.MechaniumMod;
import net.mrunknown.mechaniummod.items.ModItemGroup;

public class ModFluids {
    public static FlowableFluid STILL_MECHAFRUIT_JUICE;
    public static FlowableFluid FLOWING_MECHAFRUIT_JUICE;
    public static Block MECHAFRUIT_JUICE_BLOCK;
    public static Item MECHAFRUIT_JUICE_BUCKET;

    public static void registerFluids() {
        STILL_MECHAFRUIT_JUICE = Registry.register(Registry.FLUID,
                new Identifier(MechaniumMod.MOD_ID, "mechafruit_juice"), new MechaFruitJuiceFluid.Still());

        FLOWING_MECHAFRUIT_JUICE = Registry.register(Registry.FLUID,
                new Identifier(MechaniumMod.MOD_ID, "flowing_mechafruit_juice"), new MechaFruitJuiceFluid.Flowing());

        MECHAFRUIT_JUICE_BLOCK = Registry.register(Registry.BLOCK, new Identifier(MechaniumMod.MOD_ID, "mechafruit_juice_block"),
                new FluidBlock(ModFluids.STILL_MECHAFRUIT_JUICE, FabricBlockSettings.copyOf(Blocks.WATER)) {
                });

        MECHAFRUIT_JUICE_BUCKET = Registry.register(Registry.ITEM, new Identifier(MechaniumMod.MOD_ID, "mechafruit_juice_bucket"),
                new BucketItem(ModFluids.STILL_MECHAFRUIT_JUICE, new FabricItemSettings().group(ModItemGroup.MECHANIUM_ITEM_GROUP).recipeRemainder(Items.BUCKET).maxCount(1)));
    }
}
