package net.mrunknown.mechaniummod.screens;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.mrunknown.mechaniummod.MechaniumMod;

public class ModScreenHandlers {
    public static ScreenHandlerType<GemInfusingScreenHandler> GEM_INFUSING_SCREEN_HANDLER =
            new ExtendedScreenHandlerType<>(GemInfusingScreenHandler::new);

    public static ScreenHandlerType<FruitCrusherScreenHandler> FRUIT_CRUSHER_SCREEN_HANDLER =
            new ExtendedScreenHandlerType<>(FruitCrusherScreenHandler::new);

    public static ScreenHandlerType<FluidInjectorScreenHandler> FLUID_INJECTOR_SCREEN_HANDLER =
            new ExtendedScreenHandlerType<>(FluidInjectorScreenHandler::new);

    public static void registerAllScreenHandlers() {
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(MechaniumMod.MOD_ID, "gem_infusing"), GEM_INFUSING_SCREEN_HANDLER);
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(MechaniumMod.MOD_ID, "fruit_crushing"), FRUIT_CRUSHER_SCREEN_HANDLER);
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(MechaniumMod.MOD_ID, "fluid_crushing"), FLUID_INJECTOR_SCREEN_HANDLER);
    }
}
