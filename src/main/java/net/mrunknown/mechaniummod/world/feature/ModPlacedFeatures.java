package net.mrunknown.mechaniummod.world.feature;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public class ModPlacedFeatures {

    public static final RegistryEntry<PlacedFeature> MECHANIUM_ORE_PLACED = PlacedFeatures.register("mechanium_ore_placed",
            ModConfiguredFeatures.MECHANIUM_ORE, modifiersWithCount(3,
                    HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(-80), YOffset.aboveBottom(80))));

    public static final RegistryEntry<PlacedFeature> NETHER_MECHANIUM_ORE_PLACED = PlacedFeatures.register("nether_mechanium_ore_placed",
            ModConfiguredFeatures.NETHER_MECHANIUM_ORE, modifiersWithCount(4,
                    HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(-80), YOffset.aboveBottom(80))));

    public static final RegistryEntry<PlacedFeature> END_MECHANIUM_ORE_PLACED = PlacedFeatures.register("end_mechanium_ore_placed",
            ModConfiguredFeatures.END_MECHANIUM_ORE, modifiersWithCount(5,
                    HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(-80), YOffset.aboveBottom(80))));



    private static List<PlacementModifier> modifiers(PlacementModifier countModifier, PlacementModifier heightModifier) {
        return List.of(countModifier, SquarePlacementModifier.of(), heightModifier, BiomePlacementModifier.of());
    }

    private static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier heightModifier) {
        return modifiers(CountPlacementModifier.of(count), heightModifier);
    }

    private static List<PlacementModifier> modifiersWithRarity(int chance, PlacementModifier heightModifier) {
        return modifiers(RarityFilterPlacementModifier.of(chance), heightModifier);
    }
}
