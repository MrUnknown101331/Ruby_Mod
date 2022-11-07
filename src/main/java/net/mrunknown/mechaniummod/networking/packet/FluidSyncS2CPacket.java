package net.mrunknown.mechaniummod.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.mrunknown.mechaniummod.blocks.entity.FluidInjectorBlockEntity;
import net.mrunknown.mechaniummod.blocks.entity.FruitCrusherBlockEntity;
import net.mrunknown.mechaniummod.screens.FluidInjectorScreenHandler;
import net.mrunknown.mechaniummod.screens.FruitCrusherScreenHandler;
import net.mrunknown.mechaniummod.utils.FluidStack;

public class FluidSyncS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        FluidVariant variant = FluidVariant.fromPacket(buf);
        long fluidLevel = buf.readLong();
        BlockPos position = buf.readBlockPos();

        if (client.world != null) {
            if (client.world.getBlockEntity(position) instanceof FruitCrusherBlockEntity blockEntity) {
                blockEntity.setFluidLevel(variant, fluidLevel);

                if (client.player.currentScreenHandler instanceof FruitCrusherScreenHandler screenHandler &&
                        screenHandler.blockEntity.getPos().equals(position)) {
                    blockEntity.setFluidLevel(variant, fluidLevel);
                    screenHandler.setFluid(new FluidStack(variant, fluidLevel));
                }
            }

            if (client.world.getBlockEntity(position) instanceof FluidInjectorBlockEntity blockEntity) {
                blockEntity.setFluidLevel(variant, fluidLevel);

                if (client.player.currentScreenHandler instanceof FluidInjectorScreenHandler screenHandler &&
                        screenHandler.blockEntity.getPos().equals(position)) {
                    blockEntity.setFluidLevel(variant, fluidLevel);
                    screenHandler.setFluid(new FluidStack(variant, fluidLevel));
                }
            }
        }
    }
}