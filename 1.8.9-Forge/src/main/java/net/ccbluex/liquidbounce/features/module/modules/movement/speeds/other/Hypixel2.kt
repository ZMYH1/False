package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.MovementUtils.isMoving

class Hypixel2 : SpeedMode("Hypixel2") {
    override fun onDisable() {
        mc.timer.timerSpeed = 1f
        mc.thePlayer!!.speedInAir = 0.02f
    }
    override fun onTick() {}
    override fun onMotion() {}
    override fun onUpdate() {
        val thePlayer = mc.thePlayer ?: return


        if (thePlayer.onGround) {
            if(MovementUtils.isMoving())
                thePlayer.jump()
        }
    }
    override fun onMove(event: MoveEvent) {}
    override fun onEnable() {}
}