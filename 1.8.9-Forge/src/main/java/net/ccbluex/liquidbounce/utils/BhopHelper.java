package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;

public class BhopHelper extends MinecraftInstance{

    public static double getBaseMoveSpeed() {
        double speed = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            speed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return speed;
    }

    public static float getBaseSpeed() {
        float baseSpeed = mc.thePlayer.capabilities.getWalkSpeed() * 2.873f;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int ampl = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + (0.2 * ampl);
        }
        return baseSpeed;
    }

    public static void setSpeed(MoveEvent moveEvent, double moveSpeed, float yaw, double forward, double strafe) {
        if (forward != 0.0D) {
            if (strafe > 0.0D) {
                yaw += ((forward > 0.0D) ? -45 : 45);
            } else if (strafe < 0.0D) {
                yaw += ((forward > 0.0D) ? 45 : -45);
            }
            strafe = 0.0D;
            if (forward > 0.0D) {
                forward = 1.0D;
            } else if (forward < 0.0D) {
                forward = -1.0D;
            }
        }
        if (strafe > 0.0D) {
            strafe = 1.0D;
        } else if (strafe < 0.0D) {
            strafe = -1.0D;
        }
        double mx = Math.cos(Math.toRadians((yaw + 90.0F)));
        double mz = Math.sin(Math.toRadians((yaw + 90.0F)));
        moveEvent.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
        moveEvent.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
    }

    public static void setSpeed(double moveSpeed) {
        float rotationYaw = mc.thePlayer.rotationYaw;
        MovementInput movementInput = mc.thePlayer.movementInput;
        double strafe = movementInput.moveStrafe;
        MovementInput movementInput2 = mc.thePlayer.movementInput;
        setSpeed(moveSpeed, rotationYaw, strafe, movementInput.moveForward);
    }

    public static void setSpeed(double moveSpeed, float yaw, double strafe, double forward) {
        if (forward != 0.0D) {
            if (strafe > 0.0D) {
                yaw += ((forward > 0.0D) ? -45 : 45);
            } else if (strafe < 0.0D) {
                yaw += ((forward > 0.0D) ? 45 : -45);
            }
            strafe = 0.0D;
            if (forward > 0.0D) {
                forward = 1.0D;
            } else if (forward < 0.0D) {
                forward = -1.0D;
            }
        }
        if (strafe > 0.0D) {
            strafe = 1.0D;
        } else if (strafe < 0.0D) {
            strafe = -1.0D;
        }
        double mx = Math.cos(Math.toRadians((yaw + 90.0F)));
        double mz = Math.sin(Math.toRadians((yaw + 90.0F)));
        mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }
}
