package org.shadowmaster435.outtapocket;

import net.minecraft.entity.projectile.ProjectileEntity;
import org.shadowmaster435.outtapocket.mixin.ProjEntLeftOwnerAccessor;

import java.util.EventObject;

public class MixinHelper {
    private MixinHelper() {}




    public static boolean leftOwner(ProjectileEntity entity) {
        return ((ProjEntLeftOwnerAccessor) entity).outtapocket$getLeftOwner();
    }


}
