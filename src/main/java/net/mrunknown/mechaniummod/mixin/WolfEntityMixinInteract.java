package net.mrunknown.mechaniummod.mixin;

import net.minecraft.entity.passive.WolfEntity;
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

@Mixin(WolfEntity.class)
public class WolfEntityMixinInteract {
    @Inject(method = "interactMob", at = @At(value = "HEAD"), cancellable = true)
    public void mobCatcherInteractMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = player.getStackInHand(hand);

        World world = player.getEntityWorld();

        if (!world.isClient) {
            if (stack.getItem() == ModItems.MOB_CATCHER_WAND) {
                player.swingHand(hand);

                cir.setReturnValue(ActionResult.PASS);
            }
        }
    }
}
