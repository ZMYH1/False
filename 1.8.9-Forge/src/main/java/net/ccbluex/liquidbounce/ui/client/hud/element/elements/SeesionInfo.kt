package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.SessionUtils
import net.ccbluex.liquidbounce.utils.StatisticsUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.FontValue
import net.ccbluex.liquidbounce.value.ListValue
import java.awt.Color
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

@ElementInfo(name = "SessionInfo")
class SessionInfo(x: Double = 10.0, y: Double = 10.0, scale: Float = 1F): Element(x, y, scale){
    private val mode = ListValue("Mode", arrayOf("False","Novoline"),"Novoline")
    private val fontRenderer = FontValue("Font", Fonts.minecraftFont)
    private val fontheight = fontRenderer.get().FONT_HEIGHT
    private val nameText = "Player:"+mc.thePlayer.name
    private val decimalFormat = DecimalFormat("##0.0", DecimalFormatSymbols(Locale.ENGLISH))
    private val DATE_FORMAT = SimpleDateFormat("HH:mm:ss")
    private val timeText = "Play Time: ${DATE_FORMAT.format(Date(System.currentTimeMillis() - SessionUtils.lastWorldTime - 8000L * 3600L))}"
    private val healthText = "Health:"+decimalFormat.format(mc.thePlayer.health)
    private val killText = "Kills:"+StatisticsUtils.getKills()

    /**
     * Draw element
     */
    override fun drawElement(): Border {
        when (mode.get().toLowerCase()){
            "false" -> {
                RenderUtils.drawFadeRect(
                    0f
                    ,0f
                    ,130f
                    ,1.5f
                    , Color(ClickGUI.colorRedValue.get(), ClickGUI.colorGreenValue.get(), ClickGUI.colorBlueValue.get(),255)
                    , 100)
                RenderUtils.drawRect(0f,0f,130f,4f + fontheight * 5, Color(0, 0, 0, 120).rgb)
                fontRenderer.get().drawString("SessionInfo", 2, 3,Color(255, 255, 255).rgb)
                fontRenderer.get().drawString(nameText, 2, 5 + fontheight,Color(255, 255, 255).rgb)
                fontRenderer.get().drawString(timeText, 2, 5 + fontheight * 2,Color(255, 255, 255).rgb)
                fontRenderer.get().drawString(healthText, 2, 5 + fontheight * 3,Color(255, 255, 255).rgb)
                fontRenderer.get().drawString(killText, 2, 5 + fontheight * 4,Color(255, 255, 255).rgb)
            }
            "novoline" -> {
                RenderUtils.drawShadow(0f,0f,130f,4f + fontheight * 5)
                RenderUtils.drawFadeRect(
                    1f
                    ,0f + 11f
                    ,129f
                    ,1.5f + 11f
                    , Color(ClickGUI.colorRedValue.get(), ClickGUI.colorGreenValue.get(), ClickGUI.colorBlueValue.get(),255)
                    , 100)
                RenderUtils.drawRect(0f,0f,130f,4f + fontheight * 5, Color(0, 0, 0, 60).rgb)
                fontRenderer.get().drawString("SessionInfo", 2, 3,Color(255, 255, 255).rgb)
                fontRenderer.get().drawString(nameText, 2, 5 + fontheight,Color(255, 255, 255).rgb)
                fontRenderer.get().drawString(timeText, 2, 5 + fontheight * 2,Color(255, 255, 255).rgb)
                fontRenderer.get().drawString(healthText, 2, 5 + fontheight * 3,Color(255, 255, 255).rgb)
                fontRenderer.get().drawString(killText, 2, 5 + fontheight * 4,Color(255, 255, 255).rgb)
            }
        }

        return Border(0f,0f,130f,0.7f + fontheight * 5)
    }
}