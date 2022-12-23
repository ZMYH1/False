
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.GLUtils
import net.ccbluex.liquidbounce.utils.render.BlurUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.FontValue
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color

/**
 * CustomHUD Armor element
 *
 * Shows a horizontal display of current armor
 */
@ElementInfo(name = "Inventory")
class Inventory(x: Double = 10.0, y: Double = 10.0, scale: Float = 1F) : Element(x, y, scale) {
    private val fontRenderer = FontValue("Font",Fonts.minecraftFont)
    private val blurValue = BoolValue("Blur",false)
    private val blurstrength = FloatValue("BlurStrength",20f,0f,50f)
    private val blurx = FloatValue("BlurX",20F,0F,50F)
    private val blury = FloatValue("BlurY",20F,0F,50F)

    /**
     * Draw element
     */
    override fun drawElement(): Border {
        if(blurValue.get()) {
            BlurUtils.blurArea(8f+blurx.get(), 25f+blury.get(), 8f + 165f, 26f, blurstrength.get())}
        RenderUtils.drawRect(8f, 25f, 8f + 165f, 30f + 65f ,Color(0, 0, 0, 120).rgb)
        RenderUtils.drawFadeRect(8f, 25f, 8f + 165f, 26.5f, Color(ClickGUI.colorRedValue.get(),ClickGUI.colorGreenValue.get(),ClickGUI.colorBlueValue.get(),255),100)
//        RenderUtils.drawRect(8f, 25f, 8f + 165f, 26f, Color(ClickGUI.colorRedValue.get(),ClickGUI.colorGreenValue.get(),ClickGUI.colorBlueValue.get(),255).rgb)
        fontRenderer.get().drawString("Inventory", 11, 29, Color(0xFFFFFF).rgb)

        var itemX: Int = 10
        var itemY: Int = 40
        var airs = 0
        for (i in mc.thePlayer.inventory.mainInventory.indices) {
            if (i < 9) continue
            val stack = mc.thePlayer.inventory.mainInventory[i]
            if (stack == null) {
                airs++
            }
            val res = ScaledResolution(mc)
            GL11.glPushMatrix()
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
            if (mc.theWorld != null) GLUtils.enableGUIStandardItemLighting()
            GlStateManager.pushMatrix()
            GlStateManager.disableAlpha()
            GlStateManager.clear(256)
            mc.renderItem.zLevel = -150.0f
            mc.renderItem.renderItemAndEffectIntoGUI(stack, itemX, itemY)
            mc.renderItem.renderItemOverlays(mc.fontRendererObj, stack, itemX, itemY)
            mc.renderItem.zLevel = 0.0f
            GlStateManager.disableBlend()
            GlStateManager.scale(0.5, 0.5, 0.5)
            GlStateManager.disableDepth()
            GlStateManager.disableLighting()
            GlStateManager.enableDepth()
            GlStateManager.scale(2.0f, 2.0f, 2.0f)
            GlStateManager.enableAlpha()
            GlStateManager.popMatrix()
            GL11.glPopMatrix()
            if (itemX < 152) {
                itemX += 18
            } else {
                itemX = 10
                itemY += 18
            }
        }

        if (airs == 27) {
            fontRenderer.get().drawString("Your inventory is empty...", 28, 56, Color(255, 255, 255).rgb)
        }
        return Border(8f, 30f + 10f, 8f + 163f, 30f + 65f)
    }

}
