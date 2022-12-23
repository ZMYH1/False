package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public class Minemora extends SpeedMode {
    public Minemora(){
        super("Minemora");
    }
    int movetick = 0;

    @Override
    public void onMotion(){
        if (mc.thePlayer.isInWater()) return;
        if (MovementUtils.isMoving() || mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindBack.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed) {
            if (mc.thePlayer.onGround) {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                }
                movetick = 0;
            } else {
                movetick++;
                switch (movetick){
                    case 1:{
                        MovementUtils.strafe(MovementUtils.getSpeed() * 1.011f);
                        mc.timer.timerSpeed = 0.98f;
                        break;
                    }
                    case 2:{
                        MovementUtils.strafe(MovementUtils.getSpeed() * 1.01f);
                        mc.timer.timerSpeed = 1.01f;
                        break;
                    }
                    case 3:{
                        MovementUtils.strafe(MovementUtils.getSpeed() * 1.012f);
                        mc.timer.timerSpeed = 0.9f;
                        break;
                    }
                    case 4:{
                        MovementUtils.strafe(MovementUtils.getSpeed() * 1f);
                        mc.timer.timerSpeed = 1.2f;
                        break;
                    }
                    case 5:{
                        movetick = 0;
                        mc.timer.timerSpeed = 1.0f;
                    }
                }
            }
        } else {
            mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionZ = 0.0;
        }
    }

    @Override
    public void onEnable() {
        movetick = 0;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
        super.onDisable();
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onMove(MoveEvent event) {
    }
}
