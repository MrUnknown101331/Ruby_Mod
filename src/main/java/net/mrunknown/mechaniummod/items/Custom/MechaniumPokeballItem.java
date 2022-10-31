package net.mrunknown.mechaniummod.items.Custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.mrunknown.mechaniummod.utils.CatcherUtil;

import java.util.List;
import java.util.Objects;


//

public class MechaniumPokeballItem extends Item {


    public MechaniumPokeballItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (!user.world.isClient) {
            if (stack.getOrCreateSubNbt("entity_data").isEmpty() && !(entity instanceof WitherEntity) && (entity instanceof AnimalEntity || entity instanceof TameableEntity || entity instanceof GolemEntity ||
                    entity instanceof SquidEntity || entity instanceof FishEntity || entity instanceof DolphinEntity || entity instanceof BatEntity || entity instanceof VillagerEntity ||
                    entity instanceof WanderingTraderEntity || entity instanceof HostileEntity || entity instanceof PhantomEntity ||
                    entity instanceof SlimeEntity || entity instanceof GhastEntity)) {
                if (CatcherUtil.saveEntityToStack(entity, stack)) {
                    user.setStackInHand(hand, stack);
                }
            }
        }
        user.getItemCooldownManager().set(this, 20);
        return ActionResult.SUCCESS;
    }


    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        if (!(context.getWorld() instanceof ServerWorld))
            return ActionResult.SUCCESS;
        if (!context.getWorld().isClient && stack.hasNbt() && stack.getNbt().contains("entity_data")) {
            CatcherUtil.respawnEntity(context, stack);
        }
        Objects.requireNonNull(context.getPlayer()).getItemCooldownManager().set(this, 20);
        return ActionResult.SUCCESS;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return stack.hasNbt() && !stack.getOrCreateSubNbt("entity_data").isEmpty();
    }

    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.literal("Captures and stores passive animals and villagers").formatted(Formatting.GREEN));

        if (stack.hasNbt() && !stack.getOrCreateSubNbt("entity_data").isEmpty()) {
            tooltip.add(Text.literal("Contents: " + stack.getNbt().getString("name")).formatted(Formatting.YELLOW));
        } else
            tooltip.add(Text.literal("Currently Empty").formatted(Formatting.YELLOW));
    }
}
