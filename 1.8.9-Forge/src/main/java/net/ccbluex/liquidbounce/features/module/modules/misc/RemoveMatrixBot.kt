package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.MinecraftInstance.mc
import net.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.network.play.server.S38PacketPlayerListItem
import net.minecraft.network.play.server.S41PacketServerDifficulty
import net.minecraft.world.WorldSettings

@ModuleInfo(name = "RemoveMatrixBot", description = "Prevents KillAura from attacking AntiCheat Matrix bots", category = ModuleCategory.MISC)
class RemoveMatrixBot : Module(){
    private val removeValue = BoolValue("CzechMatrix", true)
    private val debugValue = BoolValue("Debug", true)
    private val czechHekPingCheckValue = BoolValue("PingCheck", true)
    private val czechHekGMCheckValue = BoolValue("GamemodeCheck", true)

    private var wasAdded = mc.thePlayer != null

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if (mc.thePlayer == null || mc.theWorld == null) return
        if (removeValue.get()) {

            val packet = event.packet

            if (packet is S41PacketServerDifficulty) wasAdded = false
            if (packet is S38PacketPlayerListItem) {
                val packetListItem = event.packet as S38PacketPlayerListItem
                val data = packetListItem.entries[0]
                if (data.profile != null && data.profile.name != null) {
                    if (!wasAdded) wasAdded =
                        data.profile.name == mc.thePlayer.name else if (!mc.thePlayer.isSpectator && !mc.thePlayer.capabilities.allowFlying && (!czechHekPingCheckValue.get() || data.ping != 0) && (!czechHekGMCheckValue.get() || data.gameMode != WorldSettings.GameType.NOT_SET)) {
                        event.cancelEvent()
                        if (debugValue.get()) ClientUtils.displayChatMessage("§7[§a§lRemove MatrixBot§7] §fRemove§r -> " + data.profile.name)
                    }
                }
            }
        }
    }
}