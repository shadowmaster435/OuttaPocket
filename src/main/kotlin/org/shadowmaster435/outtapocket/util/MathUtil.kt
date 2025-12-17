
package org.shadowmaster435.outtapocket.util

import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import org.joml.Vector2d
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun Vector2d.eulerToVector(): Vec3d {
    val x = -sin(this.x * (PI / 180.0)) * cos(this.x * (PI / 180.0))
    val y = -sin((this.x ) * (PI / 180.0))
    val z = cos(this.y * (PI / 180.0)) * cos(this.x * (PI / 180.0))
    return Vec3d(x, y, z)
}
object MathUtil {
    fun randomVec3d(randomScale: Double): Vec3d {
        val random = Random.createLocal()
        return Vec3d(
            ((0.5 + random.nextDouble() * 0.5) * 2.0) * randomScale,
            ((0.5 + random.nextDouble() * 0.5) * 2.0) * randomScale,
            ((0.5 + random.nextDouble() * 0.5) * 2.0) * randomScale
        )
    }


}