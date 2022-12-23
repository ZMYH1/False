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
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.ccbluex.liquidbounce.utils.extensions.getDistanceToEntityBox
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.cos
import kotlin.math.sin

@ModuleInfo(name = "TargetStrafe", description = "Targetstrafe.", category = ModuleCategory.MOVEMENT)
class TargetStrafe : Module() {

    private val godModValue = BoolValue("AutoThirdPersonView", false)
    private val render = ListValue("Render", arrayOf("Circle","Novoline","Pentagon","Pentagram","Heptagonal","Peaceful","Exhi","Off"),"Other")
    private val thicknessValue = FloatValue("Thickness", 2F, 1F, 5F)
    private val radiusValue = FloatValue("Radius", 2.0F, 1.0F, 5.0F)
    private val modeValue = ListValue("KeyMode", arrayOf("Jump", "None"), "None")
    private val radiusMode = ListValue("radiusMode", arrayOf("TrueRadius", "Simple"), "Simple")
    private val side = IntegerValue("ExhiSide",3,3,20)
    private val exhiRect = BoolValue("ExhiRect",false)
    private val killAura = LiquidBounce.moduleManager.getModule(KillAura::class.java) as KillAura
    private val speed = LiquidBounce.moduleManager.getModule(Speed::class.java) as Speed
    private val fly = LiquidBounce.moduleManager.getModule(Fly::class.java) as Fly
    var consts = 0
    var lastDist = 0.0
    var silent = false


    @EventTarget
    fun onRender3D(event: Render3DEvent) {

        if (render.get()=="Circle") {
            if (killAura.target == null) return
            val target = (LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target!!
            if(speed.state) {
                GL11.glPushMatrix()
                GL11.glDisable(3553)
                GL11.glEnable(2848)
                GL11.glEnable(2881)
                GL11.glEnable(2832)
                GL11.glEnable(3042)
                GL11.glBlendFunc(770, 771)
                GL11.glHint(3154, 4354)
                GL11.glHint(3155, 4354)
                GL11.glHint(3153, 4354)
                GL11.glDisable(2929)
                GL11.glDepthMask(false)
                GL11.glLineWidth(1.0f)
                GL11.glBegin(3)
                val x =
                    target.lastTickPosX + (target.posX - target.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX
                val y =
                    target.lastTickPosY + (target.posY - target.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY
                val z =
                    target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
                for (i in 0..359) {
                    val rainbow = Color(
                        Color.HSBtoRGB(
                            ((mc.thePlayer.ticksExisted / 70.0 + Math.sin(i / 50.0 * 1.75)) % 1.0f).toFloat(),
                            0.7f,
                            1.0f
                        )
                    )
                    GL11.glColor3f(rainbow.red / 255.0f, rainbow.green / 255.0f, rainbow.blue / 255.0f)
                    GL11.glVertex3d(
                        x + radiusValue.get() * Math.cos(i * 6.283185307179586 / 45.0),
                        y,
                        z + radiusValue.get() * Math.sin(i * 6.283185307179586 / 45.0)
                    )
                }
                GL11.glEnd()
                GL11.glDepthMask(true)
                GL11.glEnable(2929)
                GL11.glDisable(2848)
                GL11.glDisable(2881)
                GL11.glEnable(2832)
                GL11.glEnable(3553)
                GL11.glPopMatrix()
            }
        }else if(render.get()=="Pentagon") {
            if (killAura.target == null) return
            val target = (LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target!!
            GL11.glPushMatrix()
            GL11.glDisable(3553)
            GL11.glEnable(2848)
            GL11.glEnable(2881)
            GL11.glEnable(2832)
            GL11.glEnable(3042)
            GL11.glBlendFunc(770, 771)
            GL11.glHint(3154, 4354)
            GL11.glHint(3155, 4354)
            GL11.glHint(3153, 4354)
            GL11.glDisable(2929)
            GL11.glDepthMask(false)
            GL11.glBegin(3)
            val x = target.lastTickPosX + (target.posX - target.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX
            val y = target.lastTickPosY + (target.posY - target.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY
            val z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
            GL11.glLineWidth(3.5f)
            for (i in 0..360 step 36) {
                GL11.glColor3f(0f,0f,0f)
                GL11.glVertex3d(
                    x + (radiusValue.get() - 0.0075f) * cos(i * 6.283185307179586 / 45.0),
                    y,
                    z + (radiusValue.get() - 0.0075f) * sin(i * 6.283185307179586 / 45.0)
                )
            }
            for (i in 0..360 step 36) {
                GL11.glColor3f(0f,0f,0f)
                GL11.glVertex3d(
                    x + (radiusValue.get() + 0.0075f) * cos(i * 6.283185307179586 / 45.0),
                    y,
                    z + (radiusValue.get() + 0.0075f) * sin(i * 6.283185307179586 / 45.0)
                )
            }
            for (i in 0..360 step 36) {
                GL11.glColor3f(1f,1f,1f)
                GL11.glVertex3d(
                    x + radiusValue.get() * cos(i * 6.283185307179586 / 45.0),
                    y,
                    z + radiusValue.get() * sin(i * 6.283185307179586 / 45.0)
                )
            }
            GL11.glEnd()
            GL11.glDepthMask(true)
            GL11.glEnable(2929)
            GL11.glDisable(2848)
            GL11.glDisable(2881)
            GL11.glEnable(2832)
            GL11.glEnable(3553)
            GL11.glPopMatrix()
        }else if(render.get()=="Exhi") {
            if (killAura.target == null) return
            val target = (LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target!!
            GL11.glPushMatrix()
            GL11.glDisable(3553)
            GL11.glEnable(2848)
            GL11.glEnable(2881)
            GL11.glEnable(2832)
            GL11.glEnable(3042)
            GL11.glBlendFunc(770, 771)
            GL11.glHint(3154, 4354)
            GL11.glHint(3155, 4354)
            GL11.glHint(3153, 4354)
            GL11.glDisable(2929)
            GL11.glDepthMask(false)
            GL11.glBegin(3)
            val x = target.lastTickPosX + (target.posX - target.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX
            val y = target.lastTickPosY + (target.posY - target.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY
            val z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ

            if (exhiRect.get()) {
                GL11.glLineWidth(1f)
                for (i in 0..360 step 360 / side.get()) {
                    GL11.glColor3f(0f, 0f, 0f)
                    GL11.glVertex3d(
                        x + (radiusValue.get() + 0.00075) * cos(i * 3.141592653589793 / 180.0),
                        y,
                        z + (radiusValue.get() + 0.00075) * sin(i * 3.141592653589793 / 180.0)
                    )
                }

                for (i in 0..360 step 360 / side.get()) {
                    GL11.glColor3f(0f, 0f, 0f)
                    GL11.glVertex3d(
                        x + (radiusValue.get() - 0.00075) * cos(i * 3.141592653589793 / 180.0),
                        y,
                        z + (radiusValue.get() - 0.00075) * sin(i * 3.141592653589793 / 180.0)
                    )
                }
            }

            GL11.glLineWidth(thicknessValue.get())

            for (i in 0..360 step 360/side.get()) {
                if (canStrafe){
                    GL11.glColor3f(Color.YELLOW.red.toFloat(),Color.YELLOW.green.toFloat(),Color.YELLOW.blue.toFloat())
                }else{
                    GL11.glColor3f(1f,1f,1f)
                }
                GL11.glVertex3d(
                    x + radiusValue.get() * cos(i * 3.141592653589793 / 180.0),
                    y,
                    z + radiusValue.get() * sin(i * 3.141592653589793 / 180.0)
                )
            }

            GL11.glEnd()
            GL11.glDepthMask(true)
            GL11.glEnable(2929)
            GL11.glDisable(2848)
            GL11.glDisable(2881)
            GL11.glEnable(2832)
            GL11.glEnable(3553)
            GL11.glPopMatrix()
        }else if (render.get()=="Pentagram"){
            if (killAura.target == null) return
            val target = (LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target!!
            GL11.glPushMatrix()
            GL11.glDisable(3553)
            GL11.glEnable(2848)
            GL11.glEnable(2881)
            GL11.glEnable(2832)
            GL11.glEnable(3042)
            GL11.glBlendFunc(770, 771)
            GL11.glHint(3154, 4354)
            GL11.glHint(3155, 4354)
            GL11.glHint(3153, 4354)
            GL11.glDisable(2929)
            GL11.glDepthMask(false)
            GL11.glBegin(3)
            val x = target.lastTickPosX + (target.posX - target.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX
            val y = target.lastTickPosY + (target.posY - target.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY
            val z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
            GL11.glLineWidth(3.5f)
            for (i in 0..360 step 360/5) {
                GL11.glColor3f(0f,0f,0f)
                GL11.glVertex3d(
                    x + (radiusValue.get() - 0.0075f) * cos(i * 6.283185307179586 / 45.0),
                    y,
                    z + (radiusValue.get() - 0.0075f) * sin(i * 6.283185307179586 / 45.0)
                )
            }
            for (i in 0..360 step 360/5) {
                GL11.glColor3f(0f,0f,0f)
                GL11.glVertex3d(
                    x + (radiusValue.get() + 0.0075f) * cos(i * 6.283185307179586 / 45.0),
                    y,
                    z + (radiusValue.get() + 0.0075f) * sin(i * 6.283185307179586 / 45.0)
                )
            }
            for (i in 0..360 step 360/5) {
                GL11.glColor3f(1f,1f,1f)
                GL11.glVertex3d(
                    x + radiusValue.get() * cos(i * 6.283185307179586 / 45.0),
                    y,
                    z + radiusValue.get() * sin(i * 6.283185307179586 / 45.0)
                )
            }
            GL11.glEnd()
            GL11.glDepthMask(true)
            GL11.glEnable(2929)
            GL11.glDisable(2848)
            GL11.glDisable(2881)
            GL11.glEnable(2832)
            GL11.glEnable(3553)
            GL11.glPopMatrix()
        }else if(render.get()=="Pentagon") {
            if (killAura.target == null) return
            val target = (LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target!!
            GL11.glPushMatrix()
            GL11.glDisable(3553)
            GL11.glEnable(2848)
            GL11.glEnable(2881)
            GL11.glEnable(2832)
            GL11.glEnable(3042)
            GL11.glBlendFunc(770, 771)
            GL11.glHint(3154, 4354)
            GL11.glHint(3155, 4354)
            GL11.glHint(3153, 4354)
            GL11.glDisable(2929)
            GL11.glDepthMask(false)
            GL11.glBegin(3)
            val x = target.lastTickPosX + (target.posX - target.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX
            val y = target.lastTickPosY + (target.posY - target.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY
            val z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
            GL11.glLineWidth(3.5f)
            for (i in 0..360 step 36) {
                GL11.glColor3f(0f,0f,0f)
                GL11.glVertex3d(
                    x + (radiusValue.get() - 0.0075f) * cos(i * 6.283185307179586 / 45.0),
                    y,
                    z + (radiusValue.get() - 0.0075f) * sin(i * 6.283185307179586 / 45.0)
                )
            }
            for (i in 0..360 step 36) {
                GL11.glColor3f(0f,0f,0f)
                GL11.glVertex3d(
                    x + (radiusValue.get() + 0.0075f) * cos(i * 6.283185307179586 / 45.0),
                    y,
                    z + (radiusValue.get() + 0.0075f) * sin(i * 6.283185307179586 / 45.0)
                )
            }
            for (i in 0..360 step 36) {
                GL11.glColor3f(1f,1f,1f)
                GL11.glVertex3d(
                    x + radiusValue.get() * cos(i * 6.283185307179586 / 45.0),
                    y,
                    z + radiusValue.get() * sin(i * 6.283185307179586 / 45.0)
                )
            }
            GL11.glEnd()
            GL11.glDepthMask(true)
            GL11.glEnable(2929)
            GL11.glDisable(2848)
            GL11.glDisable(2881)
            GL11.glEnable(2832)
            GL11.glEnable(3553)
            GL11.glPopMatrix()
        }else if(render.get()=="Heptagonal") {
            if (killAura.target == null) return
            val target = (LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target!!
            GL11.glPushMatrix()
            GL11.glDisable(3553)
            GL11.glEnable(2848)
            GL11.glEnable(2881)
            GL11.glEnable(2832)
            GL11.glEnable(3042)
            GL11.glBlendFunc(770, 771)
            GL11.glHint(3154, 4354)
            GL11.glHint(3155, 4354)
            GL11.glHint(3153, 4354)
            GL11.glDisable(2929)
            GL11.glDepthMask(false)
            GL11.glBegin(3)
            val x = target.lastTickPosX + (target.posX - target.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX
            val y = target.lastTickPosY + (target.posY - target.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY
            val z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
            GL11.glLineWidth(3.5f)
            for (i in 0..360 step 360/7) {
                GL11.glColor3f(0f,0f,0f)
                GL11.glVertex3d(
                    x + (radiusValue.get() - 0.0075f) * cos(i * 6.283185307179586 / 45.0),
                    y,
                    z + (radiusValue.get() - 0.0075f) * sin(i * 6.283185307179586 / 45.0)
                )
            }
            for (i in 0..360 step 360/7) {
                GL11.glColor3f(0f,0f,0f)
                GL11.glVertex3d(
                    x + (radiusValue.get() + 0.0075f) * cos(i * 6.283185307179586 / 45.0),
                    y,
                    z + (radiusValue.get() + 0.0075f) * sin(i * 6.283185307179586 / 45.0)
                )
            }
            for (i in 0..360 step 360/7) {
                GL11.glColor3f(1f,1f,1f)
                GL11.glVertex3d(
                    x + radiusValue.get() * cos(i * 6.283185307179586 / 45.0),
                    y,
                    z + radiusValue.get() * sin(i * 6.283185307179586 / 45.0)
                )
            }
            GL11.glEnd()
            GL11.glDepthMask(true)
            GL11.glEnable(2929)
            GL11.glDisable(2848)
            GL11.glDisable(2881)
            GL11.glEnable(2832)
            GL11.glEnable(3553)
            GL11.glPopMatrix()
        }else if(render.get()=="Novoline") {
            if (killAura.target == null) return
            val target = (LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target!!
            GL11.glPushMatrix()
            GL11.glDisable(3553)
            GL11.glEnable(2848)
            GL11.glEnable(2881)
            GL11.glEnable(2832)
            GL11.glEnable(3042)
            GL11.glBlendFunc(770, 771)
            GL11.glHint(3154, 4354)
            GL11.glHint(3155, 4354)
            GL11.glHint(3153, 4354)
            GL11.glDisable(2929)
            GL11.glDepthMask(false)
            GL11.glBegin(3)
            val x = target.lastTickPosX + (target.posX - target.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX
            val y = target.lastTickPosY + (target.posY - target.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY
            val z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
            GL11.glLineWidth(3.5f)
            for (i in 0..360) {
                GL11.glColor3f(0f,0f,0f)
                GL11.glVertex3d(
                    x + (radiusValue.get() - 0.0075f) * cos(i * 6.283185307179586 / 45.0),
                    y,
                    z + (radiusValue.get() - 0.0075f) * sin(i * 6.283185307179586 / 45.0)
                )
            }
            for (i in 0..360) {
                GL11.glColor3f(0f,0f,0f)
                GL11.glVertex3d(
                    x + (radiusValue.get() + 0.0075f) * cos(i * 6.283185307179586 / 45.0),
                    y,
                    z + (radiusValue.get() + 0.0075f) * sin(i * 6.283185307179586 / 45.0)
                )
            }
            for (i in 0..360) {
                GL11.glColor3f(1f,1f,1f)
                GL11.glVertex3d(
                    x + radiusValue.get() * cos(i * 6.283185307179586 / 45.0),
                    y,
                    z + radiusValue.get() * sin(i * 6.283185307179586 / 45.0)
                )
            }
            GL11.glEnd()
            GL11.glDepthMask(true)
            GL11.glEnable(2929)
            GL11.glDisable(2848)
            GL11.glDisable(2881)
            GL11.glEnable(2832)
            GL11.glEnable(3553)
            GL11.glPopMatrix()
        }else if(render.get()=="Peaceful") {
            if (killAura.target == null) return
            val target = (LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target!!
            GL11.glPushMatrix()
            GL11.glDisable(3553)
            GL11.glEnable(2848)
            GL11.glEnable(2881)
            GL11.glEnable(2832)
            GL11.glEnable(3042)
            GL11.glBlendFunc(770, 771)
            GL11.glHint(3154, 4354)
            GL11.glHint(3155, 4354)
            GL11.glHint(3153, 4354)
            GL11.glDisable(2929)
            GL11.glDepthMask(false)
            GL11.glBegin(3)
            val x = target.lastTickPosX + (target.posX - target.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX
            val y = target.lastTickPosY + (target.posY - target.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY
            val z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
            GL11.glLineWidth(1f)
            for (i in 0..360 step 90) {
                val rainbow = Color(
                    Color.HSBtoRGB(
                        ((mc.thePlayer.ticksExisted / 70.0 + Math.sin(i / 50.0 * 1.75)) % 1.0f).toFloat(),
                        0.7f,
                        1.0f
                    )
                )
                GL11.glColor3f(rainbow.red / 255.0f, rainbow.green / 255.0f, rainbow.blue / 255.0f)
                GL11.glVertex3d(
                    x + radiusValue.get() * cos(i * 3.141592653589793 / 180.0),
                    y,
                    z + radiusValue.get() * sin(i * 3.141592653589793 / 180.0)
                )
            }

            GL11.glEnd()
            GL11.glDepthMask(true)
            GL11.glEnable(2929)
            GL11.glDisable(2848)
            GL11.glDisable(2881)
            GL11.glEnable(2832)
            GL11.glEnable(3553)
            GL11.glPopMatrix()
        }

        GL11.glColor3f(1f,1f,1f)
    }
    var direction = 0

    @EventTarget
    fun movestrafe(event: MoveEvent) {
        if (mc.thePlayer.isCollidedHorizontally || checkVoid()) direction = if (direction == 1) -1 else 1
        if (mc.gameSettings.keyBindLeft.isKeyDown) {
            direction = 1
        }
        if (mc.gameSettings.keyBindRight.isKeyDown) {
            direction = -1
        }
        if (!isVoid(0, 0) && canStrafe) {
            silent = true
            val strafe = RotationUtils.getRotations(killAura.target)
            setSpeed(event,Math.sqrt(Math.pow(event.x, 2.0) + Math.pow(event.z, 2.0)), strafe[0], radiusValue.get(), 1.0)
        }else{
            silent = false
        }

        if (!killAura.state)
            silent = false

        if (!godModValue.get())
            return
        mc.gameSettings.thirdPersonView = if (canStrafe) 3 else 0
    }

    @EventTarget
    fun onStrafe(event: StrafeEvent){
        if (silent && killAura.state) {
            silent(event)
        }
    }

    val keyMode: Boolean
        get() = when (modeValue.get().toLowerCase()) {
            "jump" -> mc.gameSettings.keyBindJump.isKeyDown && mc.thePlayer.movementInput.moveStrafe == 0f
            "none" -> mc.thePlayer.movementInput.moveStrafe == 0f || mc.thePlayer.movementInput.moveForward == 0f
            else -> false
        }

    val canStrafe: Boolean
        get() = (killAura.state && (speed.state || fly.state) && killAura.target != null && !mc.thePlayer.isSneaking
                && keyMode)

    val cansize: Float
        get() = when {
            radiusMode.get().toLowerCase() == "simple" ->
                45f / mc.thePlayer!!.getDistance(killAura.target!!.posX, mc.thePlayer!!.posY, killAura.target!!.posZ).toFloat()
            else -> 45f
        }
    val Enemydistance: Double
        get() = mc.thePlayer!!.getDistance(killAura.target!!.posX, mc.thePlayer!!.posY, killAura.target!!.posZ)

    val algorithm: Float
        get() = Math.max(Enemydistance - radiusValue.get(), Enemydistance - (Enemydistance - radiusValue.get() / (radiusValue.get() * 2))).toFloat()


    fun setSpeed(moveEvent: MoveEvent, moveSpeed: Double, pseudoYaw: Float, pseudoStrafe: Float,
                 pseudoForward: Double) {
        var yaw = pseudoYaw
        var forward = pseudoForward
        var strafe = pseudoStrafe
        var strafe2 = 0f

        check()

        when {
            modeValue.get().toLowerCase() == "jump" ->
                strafe = consts.toFloat()//pseudoStrafe * Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe * consts
            modeValue.get().toLowerCase() == "none" ->
                strafe = consts.toFloat()
        }

        if (forward != 0.0) {
            if (strafe > 0.0) {
                if (radiusMode.get().toLowerCase() == "trueradius")
                    yaw += (if (forward > 0.0) -cansize else cansize)
                strafe2 += (if (forward > 0.0) -45 / algorithm else 45 / algorithm)
            } else if (strafe < 0.0) {
                if (radiusMode.get().toLowerCase() == "trueradius")
                    yaw += (if (forward > 0.0) cansize else -cansize)
                strafe2 += (if (forward > 0.0) 45 / algorithm else -45 / algorithm)
            }
            strafe = 0.0f
            if (forward > 0.0)
                forward = 1.0
            else if (forward < 0.0)
                forward = -1.0

        }
        if (strafe > 0.0)
            strafe = 1.0f
        else if (strafe < 0.0)
            strafe = -1.0f


        val mx = Math.cos(Math.toRadians(yaw + 90.0 + strafe2))
        val mz = Math.sin(Math.toRadians(yaw + 90.0 + strafe2))
        moveEvent.x = forward * moveSpeed * mx + strafe * moveSpeed * mz
        moveEvent.z = forward * moveSpeed * mz - strafe * moveSpeed * mx
    }

    private fun check() {
        if (mc.thePlayer!!.isCollidedHorizontally || checkVoid()) {
            if (consts < 2) consts += 1
            else {
                consts = -1
            }
        }
        when (consts) {
            0 -> {
                consts = 1
            }
            2 -> {
                consts = -1
            }
        }
    }

    private fun checkVoid(): Boolean {
        for (x in -1..0) {
            for (z in -1..0) {
                if (isVoid(x, z)) {
                    return true
                }
            }
        }
        return false
    }

    private fun isVoid(X: Int, Z: Int): Boolean {
        val fly = LiquidBounce.moduleManager.getModule(Fly::class.java) as Fly
        if (fly.state) {
            return false
        }
        if (mc.thePlayer!!.posY < 0.0) {
            return true
        }
        var off = 0
        while (off < mc.thePlayer!!.posY.toInt() + 2) {
            val bb: AxisAlignedBB = mc.thePlayer!!.entityBoundingBox.offset(X.toDouble(), (-off).toDouble(), Z.toDouble())
            if (mc.theWorld!!.getCollidingBoundingBoxes(mc.thePlayer as Entity, bb).isEmpty()) {
                off += 2
                continue
            }
            return false
            off += 2
        }
        return true
    }

    private fun silent(event: StrafeEvent){
        killAura.update()
    }

}