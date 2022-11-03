package net.mrunknown.mechaniummod.recipe;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.mrunknown.mechaniummod.MechaniumMod;

public class ModRecipes {
    public static void registerRecipes() {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MechaniumMod.MOD_ID, GemInfusingRecipe.Serializer.ID),
                GemInfusingRecipe.Serializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(MechaniumMod.MOD_ID, GemInfusingRecipe.Type.ID),
                GemInfusingRecipe.Type.INSTANCE);
    }
}
