package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.BhopHelper;
import net.ccbluex.liquidbounce.utils.MovementUtils;

import static net.ccbluex.liquidbounce.utils.BhopHelper.getBaseSpeed;

public class HypixelFast extends SpeedMode {

    public HypixelFast(){
        super("HypixelFast");
    }

    private double speed;
    private int ticks;

    @Override
    public void onEnable() {
        speed = 0;
        ticks = 0;
    }

    @Override
    public void onDisable() {
        speed = 0;
        ticks = 0;
    }

    @Override
    public void onMotion() {
        if (mc.thePlayer.onGround) {
            if (MovementUtils.isMoving()) {
                if (mc.gameSettings.keyBindJump.isKeyDown()) return;
                mc.thePlayer.jump();
                BhopHelper.setSpeed(getBaseSpeed() * 1.5);
                if (ticks > 0) BhopHelper.setSpeed(getBaseSpeed() * 1.77);
                ticks++;
            }
        } else if (MovementUtils.isMoving()) {
            if (mc.thePlayer.motionY > 0.05 && mc.thePlayer.motionY < 0.15) mc.thePlayer.motionY = (float) -0.01;
            if (mc.thePlayer.motionY > -0.07 && mc.thePlayer.motionY < 0.) mc.thePlayer.motionY = (float) -0.09;
        }
        if (!MovementUtils.isMoving()) speed = 0;
    }

    @Override
    public void onMove(MoveEvent event) {
        if (!MovementUtils.isMoving() || mc.thePlayer.isCollidedHorizontally) speed = 0;
        BhopHelper.setSpeed(MovementUtils.isMoving() ? Math.max(BhopHelper.getBaseSpeed(), MovementUtils.getSpeed()) : 0);
    }

    @Override
    public void onUpdate(){}
}
