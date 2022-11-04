package net.mrunknown.mechaniummod.screens;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.mrunknown.mechaniummod.MechaniumMod;

public class ModScreenHandlers {
    public static ScreenHandlerType<GemInfusingScreenHandler> GEM_INFUSING_SCREEN_HANDLER =
            new ExtendedScreenHandlerType<>(GemInfusingScreenHandler::new);

    public static void registerAllScreenHandlers() {
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(MechaniumMod.MOD_ID, "gem_infusing"), GEM_INFUSING_SCREEN_HANDLER);
    }
}
