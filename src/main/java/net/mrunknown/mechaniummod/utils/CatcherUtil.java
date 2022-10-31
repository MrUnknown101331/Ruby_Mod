package net.mrunknown.mechaniummod.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class CatcherUtil {

    public static boolean saveEntityToStack(Entity entity, ItemStack stack){
        NbtCompound entityNbt = new NbtCompound();
        if(!entity.saveSelfNbt(entityNbt)){
            return false;
        }

        stack.getOrCreateNbt().put("entity_data", entityNbt);
        stack.getOrCreateNbt().putString("name", entity.getDisplayName().getString());
        entity.discard();

        return true;
    }

    public static void respawnEntity(ItemUsageContext context, ItemStack stack){
        ServerWorld serverWorld = (ServerWorld) context.getWorld();
        BlockPos pos = context.getBlockPos().offset(context.getSide());
        ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
        NbtCompound entityNbt = context.getStack().getSubNbt("entity_data");

        Optional<Entity> entity = EntityType.getEntityFromNbt(entityNbt, serverWorld);

        if(entity.isPresent()){
            Entity entity2 = entity.get();
            entity2.updatePositionAndAngles(pos.getX()+0.5F,pos.getY()+0.5F, pos.getZ()+0.5F, player.getYaw(), player.getPitch());
            serverWorld.spawnEntity(entity2);
        }

        stack.removeSubNbt("name");
        stack.removeSubNbt("entity_data");

        context.getPlayer().getStackInHand(context.getHand());
    }
}
