package net.ccbluex.liquidbounce.features.special

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.Listenable
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner
import net.ccbluex.liquidbounce.features.module.modules.world.ChestStealer

object AutoDisable : Listenable {
    fun handleGameEnd() {
        LiquidBounce.moduleManager.getModule(KillAura::class.java)!!.state = false
        LiquidBounce.moduleManager.getModule(InventoryCleaner::class.java)!!.state = false
        LiquidBounce.moduleManager.getModule(ChestStealer::class.java)!!.state = false
    }

    override fun handleEvents() = true
}
