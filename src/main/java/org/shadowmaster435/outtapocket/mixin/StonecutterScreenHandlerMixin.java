package org.shadowmaster435.outtapocket.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.StonecutterScreenHandler;
import org.shadowmaster435.outtapocket.init.ModBlocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(StonecutterScreenHandler.class)
public class StonecutterScreenHandlerMixin {


    @Shadow
    @Final
    private ScreenHandlerContext context;

    @ModifyReturnValue(method = "canUse", at = @At("RETURN"))
    public boolean allowOldStonecutter(boolean original) {
        AtomicBoolean bool = new AtomicBoolean(false);
        if (context != null) {
            context.get((world, pos) -> {
                bool.set(world.getBlockState(pos).isOf(ModBlocks.STONECUTTER));
                return Optional.of(true);
            });
        }

        return original || bool.get();
    }


}
