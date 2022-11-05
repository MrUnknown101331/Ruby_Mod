package net.mrunknown.mechaniummod.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import net.mrunknown.mechaniummod.MechaniumMod;
import net.mrunknown.mechaniummod.networking.packet.EnergySyncS2CPacket;
import net.mrunknown.mechaniummod.networking.packet.FluidSyncS2CPacket;

public class ModMessages {
    public static final Identifier ENERGY_SYNC = new Identifier(MechaniumMod.MOD_ID, "energy_sync");
    public static final Identifier FLUID_SYNC = new Identifier(MechaniumMod.MOD_ID, "fluid_sync");

    public static void registerC2SPackets() {

    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ENERGY_SYNC, EnergySyncS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(FLUID_SYNC, FluidSyncS2CPacket::receive);
    }

}
