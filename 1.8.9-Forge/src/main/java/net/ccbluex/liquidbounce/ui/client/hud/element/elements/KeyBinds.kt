package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import org.lwjgl.input.Keyboard
import java.awt.Color

@ElementInfo(name = "keyBinds")
class KeyBinds(x: Double = 15.0, y: Double = 10.0, scale: Float = 1F,
               side: Side = Side(Side.Horizontal.LEFT, Side.Vertical.UP)
) : Element(x, y, scale, side) {
    var onlyState = BoolValue("onlyModule", true)
    private val bgredValue = IntegerValue("Bg-R", 255, 0, 255)
    private val bggreenValue = IntegerValue("Bg-G", 255, 0, 255)
    private val bgblueValue = IntegerValue("Bg-B", 255, 0, 255)
    private val bgalphaValue = IntegerValue("Bg-Alpha", 150, 0, 255)
    override fun drawElement(): Border? {
        var y2 = 0

        //绘制背景
        RenderUtils.drawBorderedRect(0f, 0f, 84f, (17 + moduleley()).toFloat(), 0f, Color(bgredValue.get(), bggreenValue.get(), bgblueValue.get(), bgalphaValue.get()).rgb,Color(bgredValue.get(), bggreenValue.get(), bgblueValue.get(), bgalphaValue.get()).rgb)

        //绘制标题
        Fonts.minecraftFont.drawString("Binds", 23f, 8f, -1, true)

        //绘制功能列表
        for (module in LiquidBounce.moduleManager.modules) {
            if (module.keyBind == 0) continue
            if (onlyState.get()) {
                if (!module.state) continue
            }
            Fonts.font35.drawString(module.name, 3f, y2 + 21f, -1, true)
            Fonts.font35.drawString(
                Keyboard.getKeyName(module.keyBind),
                78f - Fonts.font35.getStringWidth(Keyboard.getKeyName(module.keyBind)),
                y2 + 21f,
                if (module.state) Color(255, 255, 255).rgb else Color(255, 255, 255).rgb,
                true
            )
            y2 += 12
        }
        return Border(0f, 0f, 84f, (17 + moduleley()).toFloat())
    }

    fun moduleley(): Int {
        var y = 0
        for (module in LiquidBounce.moduleManager.modules) {
            if (module.keyBind == 0) continue
            if (onlyState.get()) {
                if (!module.state) continue
            }
            y += 12
        }
        return y
    }

}