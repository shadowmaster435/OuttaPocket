package org.shadowmaster435.outtapocket.mixin;

import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ProjectileEntity.class)
public interface ProjEntLeftOwnerAccessor {
    @Accessor("leftOwner")
    boolean outtapocket$getLeftOwner();


}