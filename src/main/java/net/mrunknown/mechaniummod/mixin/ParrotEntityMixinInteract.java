package net.mrunknown.mechaniummod.mixin;

import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.mrunknown.mechaniummod.items.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParrotEntity.class)
public class ParrotEntityMixinInteract {
    @Inject(method = "interactMob", at = @At(value = "HEAD"), cancellable = true)
    public void mobCatcherInteractMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = player.getStackInHand(hand);

        World world = player.getEntityWorld();

        if (!world.isClient) {
            if (stack.getItem() == ModItems.MECHANIUM_POKEBALL) {
                player.swingHand(hand);

                cir.setReturnValue(ActionResult.PASS);
            }
        }

    }
}
