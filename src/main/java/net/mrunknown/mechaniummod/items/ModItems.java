package net.mrunknown.mechaniummod.items;

import javafx.scene.effect.Effect;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.mrunknown.mechaniummod.MechaniumMod;
import net.mrunknown.mechaniummod.blocks.ModBlocks;
import net.mrunknown.mechaniummod.items.Custom.MechaniumPokeballItem;

public class ModItems {

    public static final Item RAW_MECHANIUM = registerItem("raw_mechanium",
            new Item(new FabricItemSettings().group(ModItemGroup.MECHANIUM_ITEM_GROUP)));
    public static final Item MECHANIUM = registerItem("mechanium",
            new Item(new FabricItemSettings().group(ModItemGroup.MECHANIUM_ITEM_GROUP)));

    public static final Item MOB_CATCHER_WAND = registerItem("mob_catcher_wand",
            new MechaniumPokeballItem(new FabricItemSettings().group(ModItemGroup.MECHANIUM_ITEM_GROUP).maxCount(1)));

    public static final Item MECHAFRUIT_SEED = registerItem("mechafruit_seed",
            new AliasedBlockItem(ModBlocks.MECHAFRUIT_CROP,
                    new FabricItemSettings().group(ModItemGroup.MECHANIUM_ITEM_GROUP)));

    public static final Item MECHAFRUIT = registerItem("mechafruit",
            new Item(new FabricItemSettings().group(ModItemGroup.MECHANIUM_ITEM_GROUP)
                    .food(new FoodComponent.Builder().hunger(4).saturationModifier(4f)
                            .statusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 200), 1f)
                            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400), 1f)
                            .statusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 600), 1f)
                            .statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 600),1f)
                            .build())));

    public static final Item INCOMPLETE_MECHAFRUIT_SEED = registerItem("incomplete_mechafruit_seed",
            new Item(new FabricItemSettings().group(ModItemGroup.MECHANIUM_ITEM_GROUP)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(MechaniumMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        MechaniumMod.LOGGER.debug("Registering mod items for " + MechaniumMod.MOD_ID);
    }
}
