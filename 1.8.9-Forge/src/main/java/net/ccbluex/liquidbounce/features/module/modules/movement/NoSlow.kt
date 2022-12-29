/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.PacketUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.Minecraft
import net.minecraft.item.*
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayServer
import net.minecraft.network.play.client.*
import net.minecraft.network.play.server.S30PacketWindowItems
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import java.util.*

@ModuleInfo(name = "NoSlow", description = "Cancels slowness effects caused by soulsand and using items.",
        category = ModuleCategory.MOVEMENT)
class NoSlow : Module() {
    private val modeValue = ListValue("PacketMode", arrayOf("Vanilla", "LiquidBounce", "Custom","Hypixel",
        "WatchDog", "Watchdog2", "NCP",
        "AAC(1)", "AAC(2)" ,"AAC4",
        "AAC5", "OldHypixel", "Blink","Hypixel20221112","Matrix","ZQAT"), "Vanilla")

    private val blockForwardMultiplier = FloatValue("BlockForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val blockStrafeMultiplier = FloatValue("BlockStrafeMultiplier", 1.0F, 0.2F, 1.0F)

    private val consumeForwardMultiplier = FloatValue("ConsumeForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val consumeStrafeMultiplier = FloatValue("ConsumeStrafeMultiplier", 1.0F, 0.2F, 1.0F)

    private val bowForwardMultiplier = FloatValue("BowForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val bowStrafeMultiplier = FloatValue("BowStrafeMultiplier", 1.0F, 0.2F, 1.0F)


    private val c07Value = BoolValue("AAC4-C07", true)
    private val c08Value = BoolValue("AAC4-C08", true)
    private val groundValue = BoolValue("AAC4-OnGround", true)

    private val customOnGround = BoolValue("CustomOnGround", false)
    private val customDelayValue = IntegerValue("CustomDelay", 60, 10, 200)

    private val debugValue = BoolValue("Debug", false)
    private val packetTriggerValue = ListValue("PacketTrigger", arrayOf("PreRelease", "PostRelease"), "PostRelease")

    // Blocks
    val soulsandValue = BoolValue("Soulsand", true)
    val liquidPushValue = BoolValue("LiquidPush", true)
    private val ciucValue = BoolValue("CheckInUseCount", true)

    //Bypass
    private var pendingFlagApplyPacket = false
    private var lastMotionX = 0.0
    private var lastMotionY = 0.0
    private var lastMotionZ = 0.0
    private val msTimer = MSTimer()
    private val alertTimer = MSTimer()
    private var sendBuf = false
    private var packetBuf = LinkedList<Packet<INetHandlerPlayServer>>()
    private var nextTemp = false
    private var waitC03 = false
    private var sendPacket = false
    private var lastBlockingStat = false
    private var tick = 0

    private val blinkPackets = mutableListOf<Packet<INetHandlerPlayServer>>()
    private var lastX = 0.0
    private var lastY = 0.0
    private var lastZ = 0.0
    private var lastOnGround = false

    override fun onDisable() {
        msTimer.reset()
        pendingFlagApplyPacket = false
        sendBuf = false
        packetBuf.clear()
        nextTemp = false
        waitC03 = false
    }

    @EventTarget
    fun onMotion(event: MotionEvent) {
        val heldItem = mc.thePlayer.heldItem
        if (heldItem == null || heldItem.item !is ItemSword || !MovementUtils.isMoving()) {
            return
        }

        if (alertTimer.hasTimePassed(10000) && debugValue.get() && (modeValue.equals("Matrix") || modeValue.equals("Vulcan"))) {
            alertTimer.reset()
            ClientUtils.displayChatMessage("§8[§c§lNoSlow§8]§aPlease notice that Vulcan/Matrix NoSlow §cDO NOT §asupport FakeLag Disabler!")
            ClientUtils.displayChatMessage("§8[§c§lNoSlow§8]§aType .noslow updateAlert1 to disable this notice!")
        }

        val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
        if (!mc.thePlayer.isBlocking && !killAura.blockingStatus) {
            return
        }

        if (modeValue.get().toLowerCase() == "aac5") {
            if (event.eventState == EventState.POST && (mc.thePlayer.isUsingItem || mc.thePlayer.isBlocking || killAura.blockingStatus)) {
                mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f))
            }
            return
        }

        if (modeValue.get().toLowerCase() != "aac5") {
            if (!mc.thePlayer!!.isBlocking && !killAura.blockingStatus) {
                return
            }
            when (modeValue.get().toLowerCase()) {
                "Vanilla" -> {}
                "liquidbounce" -> {
                    sendPacket(event, sendC07 = true, sendC08 = true, delay = false, delayValue = 0, onGround = false)
                }
                "hypixel20221112" -> {
                    if (mc.thePlayer.ticksExisted % 2 == 0){
                        sendPacket(event, sendC07 = true, sendC08 = false, delay = false, delayValue = 0, onGround = true, watchDog = true)
                    }else{
                        sendPacket(event, sendC07 = false, sendC08 = true, delay = false, delayValue = 0, onGround = true, watchDog = true)
                    }
                }
                "hypixel" -> {
                    if (event.eventState == EventState.PRE)
                        mc.netHandler.addToSendQueue(C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos(-1, -1, -1), EnumFacing.DOWN))
                    else
                        mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f))
                }

                "aac(1)" -> {
                    if (mc.thePlayer.ticksExisted % 3 == 0) {
                        sendPacket(event,
                            sendC07 = true,
                            sendC08 = false,
                            delay = false,
                            delayValue = 0,
                            onGround = false
                        )
                    } else if (mc.thePlayer.ticksExisted % 3 == 1) {
                        sendPacket(event,
                            sendC07 = false,
                            sendC08 = true,
                            delay = false,
                            delayValue = 0,
                            onGround = false
                        )
                    }
                }

                "aac(2)" -> {
                    if(mc.thePlayer.ticksExisted % 3 == 0) {
                        sendPacket(event,
                            sendC07 = true,
                            sendC08 = false,
                            delay = false,
                            delayValue = 0,
                            onGround = false
                        )
                    } else {
                        sendPacket(event,
                            sendC07 = false,
                            sendC08 = true,
                            delay = false,
                            delayValue = 0,
                            onGround = false
                        )
                    }
                }

                "aac4" -> {
                    sendPacket(event, c07Value.get(), c08Value.get(), true, 80, groundValue.get())
                }

                "custom" -> {
                    sendPacket(event,
                        sendC07 = true,
                        sendC08 = true,
                        delay = true,
                        delayValue = customDelayValue.get().toLong(),
                        onGround = customOnGround.get()
                    )
                }

                "ncp" -> {
                    sendPacket(event, sendC07 = true, sendC08 = true, delay = false, delayValue = 0, onGround = false)
                }

                "watchdog2" -> {
                    if (event.eventState == EventState.PRE) {
                        mc.netHandler.addToSendQueue(C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN))
                    } else {
                        mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f))
                    }
                }

                "watchdog" -> {
                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                        sendPacket(event,
                            sendC07 = true,
                            sendC08 = false,
                            delay = true,
                            delayValue = 50,
                            onGround = true
                        )
                    } else {
                        sendPacket(event,
                            sendC07 = false,
                            sendC08 = true,
                            delay = false,
                            delayValue = 0,
                            onGround = true,
                            watchDog = true
                        )
                    }
                }
                "zqat" -> {
                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                        sendPacket(
                            event,
                            sendC07 = true,
                            sendC08 = false,
                            delay = true,
                            delayValue = 50,
                            onGround = true
                        )
                    } else {
                        sendPacket(
                            event,
                            sendC07 = false,
                            sendC08 = true,
                            delay = false,
                            delayValue = 0,
                            onGround = true,
                            watchDog = true
                        )
                    }
                }

                "oldhypixel" -> {
                    if (event.eventState == EventState.PRE)
                        mc.netHandler.addToSendQueue(C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos(-1, -1, -1), EnumFacing.DOWN))
                    else
                        mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f))
                }

            }
        }

    }

    private val killaura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
    private val isBlocking: Boolean
        get() = (mc.thePlayer.isUsingItem || killaura.blockingStatus) && mc.thePlayer.heldItem != null && mc.thePlayer.heldItem.item is ItemSword

    @EventTarget
    fun onUpdate(event: UpdateEvent){
        if (mc.thePlayer == null || mc.theWorld == null)
            return
        if (modeValue.get() == "Matrix" && (lastBlockingStat || isBlocking)){
            if (msTimer.hasTimePassed(230) && nextTemp){
                nextTemp = false
                PacketUtils.sendPacketNoEvent(C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                    BlockPos(-1,-1,-1),EnumFacing.DOWN
                ))
                if (packetBuf.isNotEmpty()){
                    var canAttack = false
                    for (packet in packetBuf){
                        if (packet is C03PacketPlayer){
                            canAttack = true
                        }
                        if (!((packet is C02PacketUseEntity || packet is C0APacketAnimation) && !canAttack)) {
                            PacketUtils.sendPacketNoEvent(packet)
                        }
                    }
                    packetBuf.clear()
                }
            }
            if (!nextTemp) {
                lastBlockingStat = isBlocking
                if (!isBlocking){
                    return
                }
                PacketUtils.sendPacketNoEvent(C08PacketPlayerBlockPlacement(BlockPos(-1,-1,-1),255, mc.thePlayer.inventory.getCurrentItem(),0f,0f,0f))
                nextTemp = true
                msTimer.reset()
            }
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent){
        if (mc.thePlayer == null || mc.theWorld == null)
            return
        val packet = event.packet
        val killAura = LiquidBounce.moduleManager[KillAura::class.java]!! as KillAura

        if (modeValue.get().equals("watchdog", true) && packet is S30PacketWindowItems && (mc.thePlayer.isUsingItem || mc.thePlayer.isBlocking)) {
            event.cancelEvent()
            if (debugValue.get())
                ClientUtils.displayChatMessage("detected reset item packet")
        }

        if (modeValue.get().equals("zqat", true) && packet is S30PacketWindowItems && (mc.thePlayer.isUsingItem || mc.thePlayer.isBlocking)) {
            event.cancelEvent()
            if (debugValue.get())
                ClientUtils.displayChatMessage("zqat noslow")
        }

        if(modeValue.equals("Matrix") && nextTemp) {
            if((packet is C07PacketPlayerDigging || packet is C08PacketPlayerBlockPlacement) && isBlocking) {
                event.cancelEvent()
            }else if (packet is C03PacketPlayer || packet is C0APacketAnimation || packet is C0BPacketEntityAction || packet is C02PacketUseEntity || packet is C07PacketPlayerDigging || packet is C08PacketPlayerBlockPlacement) {
                if (modeValue.equals("Vulcan") && waitC03 && packet is C03PacketPlayer) {
                    waitC03 = false
                    return
                }
                packetBuf.add(packet as Packet<INetHandlerPlayServer>)
                event.cancelEvent()
            }
        }

        if (modeValue.get().equals("blink", true) && !(killAura.state && killAura.blockingStatus) && mc.thePlayer.itemInUse != null && mc.thePlayer.itemInUse.item != null) {
            val item = mc.thePlayer.itemInUse.item
            if (mc.thePlayer.isUsingItem && (item is ItemFood || item is ItemBucketMilk || item is ItemPotion) && (!ciucValue.get() || mc.thePlayer.itemInUseCount >= 1)) {
                if (packet is C03PacketPlayer.C04PacketPlayerPosition || packet is C03PacketPlayer.C06PacketPlayerPosLook) {
                    if (mc.thePlayer.positionUpdateTicks >= 20 && packetTriggerValue.get().equals("postrelease", true)) {
                        (packet as C03PacketPlayer).x = lastX
                        packet.y = lastY
                        packet.z = lastZ
                        packet.onGround = lastOnGround
                        if (debugValue.get())
                            ClientUtils.displayChatMessage("pos update reached 20")
                    } else {
                        event.cancelEvent()
                        if (packetTriggerValue.get().equals("postrelease", true))
                            PacketUtils.sendPacketNoEvent(C03PacketPlayer(lastOnGround))
                        blinkPackets.add(packet as Packet<INetHandlerPlayServer>)
                        if (debugValue.get())
                            ClientUtils.displayChatMessage("packet player (movement) added at ${blinkPackets.size - 1}")
                    }
                } else if (packet is C03PacketPlayer.C05PacketPlayerLook) {
                    event.cancelEvent()
                    if (packetTriggerValue.get().equals("postrelease", true))
                        PacketUtils.sendPacketNoEvent(C03PacketPlayer(lastOnGround))
                    blinkPackets.add(packet as Packet<INetHandlerPlayServer>)
                    if (debugValue.get())
                        ClientUtils.displayChatMessage("packet player (rotation) added at ${blinkPackets.size - 1}")
                } else if (packet is C03PacketPlayer) {
                    if (packetTriggerValue.get().equals("prerelease", true) || packet.onGround != lastOnGround) {
                        event.cancelEvent()
                        blinkPackets.add(packet as Packet<INetHandlerPlayServer>)
                        if (debugValue.get())
                            ClientUtils.displayChatMessage("packet player (idle) added at ${blinkPackets.size - 1}")
                    }
                }
                if (packet is C0BPacketEntityAction) {
                    event.cancelEvent()
                    blinkPackets.add(packet as Packet<INetHandlerPlayServer>)
                    if (debugValue.get())
                        ClientUtils.displayChatMessage("packet action added at ${blinkPackets.size - 1}")
                }
                if (packet is C07PacketPlayerDigging && packetTriggerValue.get().equals("prerelease", true)) {
                    if (blinkPackets.size > 0) {
                        blinkPackets.forEach {
                            PacketUtils.sendPacketNoEvent(it)
                        }
                        if (debugValue.get())
                            ClientUtils.displayChatMessage("sent ${blinkPackets.size} packets.")
                        blinkPackets.clear()
                    }
                }
            }
        }

    }

    @EventTarget
    fun onSlowDown(event: SlowDownEvent) {
        val heldItem = mc.thePlayer.heldItem?.item

        event.forward = getMultiplier(heldItem, true)
        event.strafe = getMultiplier(heldItem, false)
    }

    private fun getMultiplier(item: Item?, isForward: Boolean) = when (item) {
        is ItemFood, is ItemPotion, is ItemBucketMilk -> {
            if (isForward) this.consumeForwardMultiplier.get() else this.consumeStrafeMultiplier.get()
        }
        is ItemSword -> {
            if (isForward) this.blockForwardMultiplier.get() else this.blockStrafeMultiplier.get()
        }
        is ItemBow -> {
            if (isForward) this.bowForwardMultiplier.get() else this.bowStrafeMultiplier.get()
        }
        else -> 0.2F
    }

    private fun sendPacket(
        event: MotionEvent,
        sendC07: Boolean,
        sendC08: Boolean,
        delay: Boolean,
        delayValue: Long,
        onGround: Boolean,
        watchDog: Boolean = false
    ) {
        val digging = C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos(-1, -1, -1), EnumFacing.DOWN)
        val blockPlace = C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem())
        val blockMent = C08PacketPlayerBlockPlacement(BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f)
        if (onGround && !mc.thePlayer.onGround) {
            return
        }
        if (sendC07 && event.eventState == EventState.PRE) {
            if (delay && msTimer.hasTimePassed(delayValue)) {
                mc.netHandler.addToSendQueue(digging)
            } else if (!delay) {
                mc.netHandler.addToSendQueue(digging)
            }
        }
        if (sendC08 && event.eventState == EventState.POST) {
            if (delay && msTimer.hasTimePassed(delayValue) && !watchDog) {
                mc.netHandler.addToSendQueue(blockPlace)
                msTimer.reset()
            } else if (!delay && !watchDog) {
                mc.netHandler.addToSendQueue(blockPlace)
            } else if (watchDog) {
                mc.netHandler.addToSendQueue(blockMent)
            }
        }
    }

}
