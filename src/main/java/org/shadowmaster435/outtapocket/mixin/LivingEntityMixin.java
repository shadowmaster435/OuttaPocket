package org.shadowmaster435.outtapocket.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.shadowmaster435.outtapocket.init.ModItems;
import org.shadowmaster435.outtapocket.item.IceAxeItem;
import org.shadowmaster435.outtapocket.mixin_helpers.LivingEntityHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "dropItem", at = @At("HEAD"))
    public void dropItem(ItemStack stack, boolean dropAtSelf, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        LivingEntityHelper.dropFlail(stack, (LivingEntity)(Object)this);
    }
    @Inject(method = "tickMovement", at = @At(value = "TAIL"))
    public void modifyMovement(CallbackInfo ci) {
        if (((LivingEntity)(Object)this) instanceof PlayerEntity playerEntity && playerEntity.getOffHandStack().isOf(ModItems.ICE_AXE)) {
            IceAxeItem.update(playerEntity);
        }
    }


}
