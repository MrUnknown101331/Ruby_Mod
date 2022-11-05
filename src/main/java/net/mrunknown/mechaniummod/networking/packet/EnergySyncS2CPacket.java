package net.mrunknown.mechaniummod.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.mrunknown.mechaniummod.blocks.entity.FruitCrusherBlockEntity;
import net.mrunknown.mechaniummod.blocks.entity.GemInfusingBlockEntity;
import net.mrunknown.mechaniummod.screens.FruitCrusherScreenHandler;
import net.mrunknown.mechaniummod.screens.GemInfusingScreenHandler;

public class EnergySyncS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        long energy = buf.readLong();
        BlockPos position = buf.readBlockPos();

        if (client != null) {

            if (client.world.getBlockEntity(position) instanceof GemInfusingBlockEntity blockEntity) {
                blockEntity.setEnergyLevel(energy);

                if (client.player.currentScreenHandler instanceof GemInfusingScreenHandler screenHandler &&
                        screenHandler.blockEntity.getPos().equals(position)) {
                    blockEntity.setEnergyLevel(energy);
                }
            }

            if (client.world.getBlockEntity(position) instanceof FruitCrusherBlockEntity blockEntity) {
                blockEntity.setEnergyLevel(energy);

                if (client.player.currentScreenHandler instanceof FruitCrusherScreenHandler screenHandler &&
                        screenHandler.blockEntity.getPos().equals(position)) {
                    blockEntity.setEnergyLevel(energy);
                }
            }
        }
    }
}
