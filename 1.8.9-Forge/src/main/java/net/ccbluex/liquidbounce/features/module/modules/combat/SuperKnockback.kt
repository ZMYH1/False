/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.C0BPacketEntityAction

@ModuleInfo(name = "SuperKnockback", description = "Increases knockback dealt to other entities.", category = ModuleCategory.COMBAT)
class SuperKnockback : Module() {

    private val mode = ListValue("Mode", arrayOf("Liquidbounce","Wtap"),"Liquidbounce")
    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 10)
    private val debug = BoolValue("Debug",true)

    @EventTarget
    fun onAttack(event: AttackEvent) {
        if (event.targetEntity is EntityLivingBase) {
            if (event.targetEntity.hurtTime > hurtTimeValue.get())
                return

            when (mode.get().toLowerCase()){
                // Normal
                "liquidbounce" -> {
                    if (mc.thePlayer.isSprinting) {
                        mc.netHandler.addToSendQueue(
                            C0BPacketEntityAction(
                                mc.thePlayer,
                                C0BPacketEntityAction.Action.STOP_SPRINTING
                            )
                        )
                        if (debug.get())
                            debugText(this.name,"Start Stop sprinting")
                    }

                    mc.netHandler.addToSendQueue(C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING))
                    if (debug.get())
                        debugText(this.name,"C0BPacketEntityAction.START_SPRINTING")
                    mc.netHandler.addToSendQueue(C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING))
                    if (debug.get())
                        debugText(this.name,"C0BPacketEntityAction.STOP_SPRINTING")
                    mc.netHandler.addToSendQueue(C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING))
                    if (debug.get())
                        debugText(this.name,"C0BPacketEntityAction.STOP_SPRINTING")
                    mc.thePlayer.isSprinting = true
                    mc.thePlayer.serverSprintState = true
                }
                // CustomBuild
                "wtap" -> {
                    // Start
                    if (mc.thePlayer.isSprinting) {
                        mc.thePlayer.isSprinting = false
                        if (debug.get())
                            debugText(this.name,"mc.thePlayer.Sprinting = false")
                    }
                    mc.netHandler.addToSendQueue(C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING))
                    if (debug.get())
                        debugText(this.name,"C0BPacketEntityAction.Action.START_SPRINTING")
                    mc.thePlayer.serverSprintState = true
                }
            }
        }
    }

}