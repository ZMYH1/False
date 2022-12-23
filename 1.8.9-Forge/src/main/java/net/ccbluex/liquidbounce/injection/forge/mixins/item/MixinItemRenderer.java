/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.item;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.render.Animations;
import net.ccbluex.liquidbounce.features.module.modules.render.AntiBlind;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.*;
import net.minecraft.init.Items;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
@SideOnly(Side.CLIENT)
public abstract class MixinItemRenderer {

    float delay = 0.0F;
    MSTimer rotateTimer = new MSTimer();

    @Shadow
    private float prevEquippedProgress;

    @Shadow
    private float equippedProgress;


    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    protected abstract void rotateArroundXAndY(float angle, float angleY);

    @Shadow
    protected abstract void setLightMapFromPlayer(AbstractClientPlayer clientPlayer);

    @Shadow
    protected abstract void rotateWithPlayerRotations(EntityPlayerSP entityplayerspIn, float partialTicks);

    @Shadow
    private ItemStack itemToRender;

    @Shadow
    protected abstract void renderItemMap(AbstractClientPlayer clientPlayer, float pitch, float equipmentProgress, float swingProgress);

    /**
     * @author Lo1qSk1dder
     * @reason
     */

    @Overwrite
    private void transformFirstPersonItem(float equipProgress, float swingProgress) {
        doItemRenderGLTranslate();
        GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927F);
        float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
        GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f1 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        doItemRenderGLScale();
    }

    @Shadow
    protected abstract void performDrinking(AbstractClientPlayer clientPlayer, float partialTicks);

    @Shadow
    protected abstract void doBlockTransformations();

    @Shadow
    protected abstract void doBowTransformations(float partialTicks, AbstractClientPlayer clientPlayer);

    @Shadow
    protected abstract void doItemUsedTransformations(float swingProgress);

    @Shadow
    public abstract void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform);

    @Shadow
    protected abstract void renderPlayerArm(AbstractClientPlayer clientPlayer, float equipProgress, float swingProgress);


    private void func_178096_b(float p_178096_1_, float p_178096_2_) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, p_178096_1_ * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * (float) Math.PI);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(p_178096_2_) * (float) Math.PI);
        GlStateManager.rotate(var3 * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var4 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(var4 * -80.0F, 1.0F, 0.0F, 0.0F);
        doItemRenderGLScale();
    }

    private void test(float i, float i2) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, i * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(i2 * i2 * (float) Math.PI);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(i2) * (float) Math.PI);
        float var5 = MathHelper.ceiling_float_int(MathHelper.floor_double(i2) * (float) Math.PI);
        GlStateManager.rotate(var3 * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var4 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(var5 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void tap2(final float var2, final float swing) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        final float var3 = MathHelper.sin(swing * swing * 3.1415927f);
        final float var4 = MathHelper.sin(MathHelper.sqrt_float(swing) * 3.1415927f);
        GlStateManager.translate(0.56f, -0.42f, -0.71999997f);
        GlStateManager.translate(0.1f * var4, -0.0f, -0.21999997f * var4);
        GlStateManager.translate(0.0f, var2 * -0.15f, 0.0f);
        GlStateManager.rotate(var3 * 45.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void avatar(final float equipProgress, final float swingProgress) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        final float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927f);
        final float f2 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927f);
        GlStateManager.rotate(f * -20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f2 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f2 * -40.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void tap1(float tap1, float tap2) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, tap1 * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(tap2 * tap2 * (float) Math.PI);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(tap2) * (float) Math.PI);
        GlStateManager.rotate(var3 * -40.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var4 * 0.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(var4 * 0.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void stab(float var10, float var9) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float var11 = MathHelper.sin(var9 * var9 * (float) Math.PI);
        float var12 = MathHelper.sin(MathHelper.sqrt_float(var9) * (float) Math.PI);
        GlStateManager.rotate(var11 * 20.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(var12 * 0.0F, 0.0F, 0.0f, 0.0F);
        GlStateManager.rotate(var12 * -10.0F, 1.0F, 0.0F, -4.0F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void slide(float var10, float var9) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float var11 = MathHelper.sin(var9 * var9 * (float) Math.PI);
        float var12 = MathHelper.sin(MathHelper.sqrt_float(var9) * (float) Math.PI);
        GlStateManager.rotate(var11 * 0.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var12 * 0.0F, 0.0F, 0.0f, 1.0F);
        GlStateManager.rotate(var12 * -40.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void slide2(float var10, float var9) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float var11 = MathHelper.sin(var9 * var9 * (float) Math.PI);
        float var12 = MathHelper.sin(MathHelper.sqrt_float(var9) * (float) Math.PI);
        GlStateManager.rotate(var11 * 0.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var12 * 0.0F, 0.0F, 0.0f, 1.0F);
        GlStateManager.rotate(var12 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void jello(float var11, float var12) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.rotate(48.57f, 0f, 0.24f, 0.14f);
        float var13 = MathHelper.sin(var12 * var12 * (float) Math.PI);
        float var14 = MathHelper.sin(MathHelper.sqrt_float(var12) * (float) Math.PI);
        GlStateManager.rotate(var13 * -35.0F, 0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(var14 * 0.0F, 0.0F, 0.0f, 0.0F);
        GlStateManager.rotate(var14 * 20.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void continuity(float var11, float var10) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float var12 = -MathHelper.sin(var10 * var10 * (float) Math.PI);
        float var13 = MathHelper.cos(MathHelper.sqrt_float(var10) * (float) Math.PI);
        float var14 = MathHelper.abs(MathHelper.sqrt_float(var11) * (float) Math.PI);
        GlStateManager.rotate(var12 * var14 * 30.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var13 * 0.0F, 0.0F, 0.0f, 1.0F);
        GlStateManager.rotate(var13 * 20.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void poke(final float var5, final float var6) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        final float var7 = MathHelper.sin(var6 * var6 * 3.1415927f);
        final float var8 = MathHelper.sin(MathHelper.sqrt_float(var6) * 3.1415927f);
        GlStateManager.translate(0.56f, -0.42f, -0.71999997f);
        GlStateManager.translate(0.1f * var8, -0.0f, -0.21999997f * var8);
        GlStateManager.translate(0.0f, var5 * -0.15f, 0.0f);
        GlStateManager.rotate(var7 * 0.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void Zoom(float p_178096_1_, float p_178096_2_) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, p_178096_1_ * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * (float) Math.PI);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(p_178096_2_) * (float) Math.PI);
        GlStateManager.rotate(var3 * -20.0F, 0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(var4 * -20.0F, 0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(var4 * -20.0F, 0.0F, 0.0F, 0.0F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void strange(float lul, float lol) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float var26 = MathHelper.sin(lol * lul * 3.1415927f);
        float var27 = MathHelper.cos(MathHelper.sqrt_double(lul) * (float) Math.PI);
        float var28 = MathHelper.abs(MathHelper.sqrt_float(lul) * (float) Math.PI);
        GlStateManager.rotate(var26 * var27, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var28 * 15.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(var27 * 10.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void move(final float test1, final float test2) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float var30 = MathHelper.sin(test2 * MathHelper.sqrt_float(test1) * 3.1415927f);
        float var31 = MathHelper.cos(MathHelper.sqrt_float(test2) * (float) Math.PI);
        float var29 = -MathHelper.abs(MathHelper.sqrt_float(test1) * test2 * (float) Math.PI);
        GlStateManager.rotate(var30 * var29 * -90.0f, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var29 * var31 * 5.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(var31 * 5.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void ETB(float equipProgress, float swingProgress) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(swingProgress * swingProgress * 3.1415927F);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
        GlStateManager.rotate(var3 * -34.0F, 0.0F, 1.0F, 0.2F);
        GlStateManager.rotate(var4 * -20.7F, 0.2F, 0.1F, 1.0F);
        GlStateManager.rotate(var4 * -68.6F, 1.3F, 0.1F, 0.2F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void sigmaold(float p_178096_1_, float p_178096_2_) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, p_178096_1_ * -0.6F, 0.0F);
        GlStateManager.rotate(25F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * 3.1415927F);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(p_178096_2_) * 3.1415927F);
        GlStateManager.rotate(var3 * -15F, 0.0F, 1.0F, 0.2F);
        GlStateManager.rotate(var4 * -10F, 0.2F, 0.1F, 1.0F);
        GlStateManager.rotate(var4 * -30F, 1.3F, 0.1F, 0.2F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void push1(float idk, float idc) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, idk * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(idc * idc * (float) Math.PI);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(idc) * (float) Math.PI);
        GlStateManager.rotate(var3 * -10.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(var4 * -10.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(var4 * -10.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void push2(float idk, float idc) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, idk * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(idc * idc * (float) Math.PI);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(idc) * (float) Math.PI);
        GlStateManager.rotate(var3 * -10.0F, 2.0F, 2.0F, 2.0F);
        GlStateManager.rotate(var4 * -10.0F, 2.0F, 2.0F, 0.0F);
        GlStateManager.rotate(var4 * -10.0F, 2.0F, 2.0F, 0.0F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void up(float idk, float idc) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, idk * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(idc * idc * (float) Math.PI);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(idc) * (float) Math.PI);
        GlStateManager.rotate(var3 * -20.0F, 0.0F, 1.0F, 1.0F);
        GlStateManager.rotate(var4 * -10.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(var3 * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var4 * -10.0F, 1.0F, 0.0F, 1.0F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void func_178103_d() {
        GlStateManager.translate(-0.5F, 0.2F, 0.0F);
        GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
    }

    /**
     * @author CCBlueX
     * @reason Null
     */
    @Overwrite
    public void renderItemInFirstPerson(float partialTicks){
        float f = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
        float f1 = abstractclientplayer.getSwingProgress(partialTicks);
        float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        if (LiquidBounce.moduleManager.getModule(Animations.class).getState()) {
            GL11.glTranslated(Animations.itemPosX.get().doubleValue(), Animations.itemPosY.get().doubleValue(), Animations.itemPosZ.get().doubleValue());
        }
        this.rotateArroundXAndY(f2, f3);
        this.setLightMapFromPlayer(abstractclientplayer);
        this.rotateWithPlayerRotations((EntityPlayerSP) abstractclientplayer, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();

        if (LiquidBounce.moduleManager.getModule(Animations.class).getState()) {
            GL11.glTranslated(Animations.itemPosX.get().doubleValue(), Animations.itemPosY.get().doubleValue(), Animations.itemPosZ.get().doubleValue());
        }

        if (this.itemToRender != null) {
            final KillAura killAura = (KillAura) LiquidBounce.moduleManager.getModule(KillAura.class);

            boolean canBlockEverything = LiquidBounce.moduleManager.getModule(Animations.class).getState() && Animations.blockEverything.get() && killAura.getTarget() != null 
                            && (itemToRender.getItem() instanceof ItemBucketMilk || itemToRender.getItem() instanceof ItemFood 
                                || itemToRender.getItem() instanceof ItemPotion || itemToRender.getItem() instanceof ItemAxe || itemToRender.getItem().equals(Items.stick));

            if (this.itemToRender.getItem() instanceof net.minecraft.item.ItemMap) {
                this.renderItemMap(abstractclientplayer, f2, f, f1);
            } else if (abstractclientplayer.getItemInUseCount() > 0 
                        || (itemToRender.getItem() instanceof ItemSword && (killAura.getBlockingStatus()))
                        || (itemToRender.getItem() instanceof ItemSword && LiquidBounce.moduleManager.getModule(Animations.class).getState() 
                            && Animations.fakeBlock.get() && killAura.getTarget() != null) || canBlockEverything) {

                EnumAction enumaction = (killAura.getBlockingStatus() || canBlockEverything) ? EnumAction.BLOCK : this.itemToRender.getItemUseAction();

                switch (enumaction) {
                    case NONE:
                        this.func_178096_b(f, 0.0F);
                        break;
                    case EAT:
                    case DRINK:
                        if (LiquidBounce.moduleManager.getModule(Animations.class).getState()){
                            if(Animations.swingMethod.get().equalsIgnoreCase("Swing") ||
                            Animations.swingMethod.get().equalsIgnoreCase("Default")){
                                this.performDrinking(abstractclientplayer, partialTicks);
                            }
                            this.transformFirstPersonItem(f, f1);
                            if (Animations.RotateItems.get()) {
                                ItemRotate();
                            }
                        }else{
                            this.performDrinking(abstractclientplayer, partialTicks);
                            this.transformFirstPersonItem(f, f1);
                        }
                        break;
                    case BLOCK:
                        if (LiquidBounce.moduleManager.getModule(Animations.class).getState()) {
                            GL11.glTranslated(Animations.blockPosX.get().doubleValue(), Animations.blockPosY.get().doubleValue(), Animations.blockPosZ.get().doubleValue());
                            final String z = Animations.Sword.get();
                            switch (z) {
                                case "Normal": {
                                    this.transformFirstPersonItem(f + 0.1F, f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.doBlockTransformations();
                                    GlStateManager.translate(-0.5F, 0.2F, 0.0F);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "SlideDown1": {
                                    this.func_178096_b(0.2f, f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "SlideDown2": {
                                    this.slide2(0.1f, f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Minecraft": {
                                    this.func_178096_b(f, Animations.mcSwordPos.get());
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Avatar": {
                                    this.avatar(f, f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Tap2": {
                                    this.tap2(0.0f, f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    GlStateManager.scale(2f, 2f, 2f);
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Poke": {
                                    this.poke(0.1f, f1);
                                    GlStateManager.scale(2.5f, 2.5f, 2.5f);
                                    GL11.glTranslated(1.2, -0.5, 0.5);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Slide": {
                                    this.slide(0.1f, f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Push1": {
                                    this.push1(0.1f, f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Up": {
                                    this.up(f, f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Shield": {
                                    this.jello(0.0f, f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Akrien": {
                                    this.func_178096_b(f1, 0.0F);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "VisionFX": {
                                    this.continuity(0.1f, f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Strange": {
                                    this.strange(f1 + 0.2f, 0.1f);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Lucky": {
                                    this.move(-0.3f, f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "ETB": {
                                    this.ETB(f, f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Swong": {
                                    this.transformFirstPersonItem(f / 2.0F, 0.0F);
                                    GlStateManager.rotate(-MathHelper.sin(MathHelper.sqrt_float(this.mc.thePlayer.getSwingProgress(partialTicks)) * 3.1415927F) * 40.0F / 2.0F, MathHelper.sqrt_float(this.mc.thePlayer.getSwingProgress(partialTicks)) / 2.0F, -0.0F, 9.0F);
                                    GlStateManager.rotate(-MathHelper.sqrt_float(this.mc.thePlayer.getSwingProgress(partialTicks)) * 30.0F, 1.0F, MathHelper.sqrt_float(this.mc.thePlayer.getSwingProgress(partialTicks)) / 2.0F, -0.0F);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "SigmaOld": {
                                    float var15 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927f);
                                    this.sigmaold(f * 0.5f, 0);
                                    GlStateManager.rotate(-var15 * 55 / 2.0F, -8.0F, -0.0F, 9.0F);
                                    GlStateManager.rotate(-var15 * 45, 1.0F, var15 / 2, -0.0F);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    GL11.glTranslated(1.2, 0.3, 0.5);
                                    GL11.glTranslatef(-1, this.mc.thePlayer.isSneaking() ? -0.1F : -0.2F, 0.2F);
                                    GlStateManager.scale(1.2f, 1.2f, 1.2f);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                //i will add more custom animation if i have time
                                case "SmoothFloat": {
                                    this.func_178096_b(0.0f, 0.95f);
                                    GlStateManager.rotate(this.delay, 1.0F, 0.0F, 2.0F);
                                    if (this.rotateTimer.hasTimePassed(1)) {
                                        ++this.delay;
                                        this.delay = this.delay + Animations.SpeedRotate.get();
                                        this.rotateTimer.reset();
                                    }
                                    if (this.delay > 360.0F) {
                                        this.delay = 0.0F;
                                    }
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    GlStateManager.rotate(this.delay, 0.0F, 1.0F, 0.0F);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                //don't mind this mode. i just make it for fun
                                case "Rotate360": {
                                    this.func_178096_b(0.0f, 0.95f);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    GlStateManager.rotate(this.delay, 1.0F, 0.0F, 2.0F);
                                    if (this.rotateTimer.hasTimePassed(1)) {
                                        ++this.delay;
                                        this.delay = this.delay + Animations.SpeedRotate.get();
                                        this.rotateTimer.reset();
                                    }
                                    if (this.delay > 360.0F) {
                                        this.delay = 0.0F;
                                    }
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Reverse": {
                                    this.func_178096_b(f1, f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Zoom": {
                                    this.Zoom(0.0f, f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Move": {
                                    this.test(f, f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Tap1": {
                                    this.tap1(f, f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Stab": {
                                    this.stab(0.1f, f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Push2": {
                                    this.push2(0.1F, f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                //...........................
                                case "Jello": {
                                    this.func_178096_b(0, 0.0F);
                                    this.func_178103_d();
                                    int alpha = (int) Math.min(255, ((System.currentTimeMillis() % 255) > 255/2 ? (Math.abs(Math.abs(System.currentTimeMillis()) % 255 - 255)) : System.currentTimeMillis() % 255)*2);
                                    float f5 = (f1 > 0.5 ? 1-f1 : f1);
                                    GlStateManager.translate(0.3f, -0.0f, 0.40f);
                                    GlStateManager.rotate(0.0f, 0.0f, 0.0f, 1.0f);
                                    GlStateManager.translate(0, 0.5f, 0);

                                    GlStateManager.rotate(90, 1.0f, 0.0f, -1.0f);
                                    GlStateManager.translate(0.6f, 0.5f, 0);
                                    GlStateManager.rotate(-90, 1.0f, 0.0f, -1.0f);


                                    GlStateManager.rotate(-10, 1.0f, 0.0f, -1.0f);
                                    GlStateManager.rotate((- f5) * 10.0f, 10.0f, 10.0f, -9.0f);
                                    GlStateManager.rotate(10.0f, -1.0f, 0.0f, 0.0f);

                                    GlStateManager.translate(0, 0, -0.5);
                                    GlStateManager.rotate(mc.thePlayer.isSwingInProgress ? -alpha/5f : 1, 1.0f, -0.0f, 1.0f);
                                    GlStateManager.translate(0, 0, 0.5);
                                    break;
                                }

                            }
                        } else {
                            this.transformFirstPersonItem(f + 0.1F, f1);
                            this.doBlockTransformations();
                            GlStateManager.translate(-0.5F, 0.2F, 0.0F);
                        }
                        break;
                    case BOW:
                        if(LiquidBounce.moduleManager.getModule(Animations.class).getState()){
                            this.transformFirstPersonItem(f, f1);
                            if (Animations.RotateItems.get()) {
                                ItemRotate();
                            }
                            if(Animations.swingMethod.get().equalsIgnoreCase("Swing") ||
                            Animations.swingMethod.get().equalsIgnoreCase("Cancel")){
                                this.doBowTransformations(partialTicks, abstractclientplayer);
                            }
                            if (Animations.RotateItems.get()) {
                                ItemRotate1();
                            }
                        }

                }
            } else {
                if (!Animations.swingMethod.get().equalsIgnoreCase("Swing")) {
                    this.doItemUsedTransformations(f1);
                    this.transformFirstPersonItem(f, f1);
                }else if (Animations.RotateItems.get()) {
                    this.transformFirstPersonItem(f, f1);
                    ItemRotate();
                } else if (Animations.RotateItems.get() && mc.gameSettings.keyBindUseItem.pressed) {
                    this.doBlockTransformations();
                    ItemRotate1();
                }else{
                    this.transformFirstPersonItem(f, f1);
                }
            }

            this.renderItem(abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        } else if (!abstractclientplayer.isInvisible()) {
            this.renderPlayerArm(abstractclientplayer, f, f1);
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();

        if (LiquidBounce.moduleManager.getModule(Animations.class).getState()) {
            GL11.glTranslated(-Animations.itemPosX.get().doubleValue(), -Animations.itemPosY.get().doubleValue(), -Animations.itemPosZ.get().doubleValue());
        }
    }

    //undone
    /*private void customTest(float p_doItemUsedTransformations_1_) {
        float f = -0.4F + Animations.swingPosX.get() * MathHelper.sin(MathHelper.sqrt_float(p_doItemUsedTransformations_1_) * 3.1415927F);
        float f1 = 0.2F + Animations.swingPosY.get()  * MathHelper.sin(MathHelper.sqrt_float(p_doItemUsedTransformations_1_) * 3.1415927F * 2.0F);
        float f2 = -0.2F + Animations.swingPosZ.get()  * MathHelper.sin(p_doItemUsedTransformations_1_ * 3.1415927F);
        GlStateManager.translate(f, f1, f2);
    }*/


    private void ItemRotate() {
        if (Animations.transformFirstPersonRotate.get().equalsIgnoreCase("Rotate1")) {
            GlStateManager.rotate(this.delay, 0.0F, 1.0F, 0.0F);
        }
        if (Animations.transformFirstPersonRotate.get().equalsIgnoreCase("Rotate2")) {
            GlStateManager.rotate(this.delay, 1.0F, 1.0F, 0.0F);
        }

        if (Animations.transformFirstPersonRotate.get().equalsIgnoreCase("Custom")) {
            GlStateManager.rotate(this.delay, Animations.customRotate1.get().floatValue(), Animations.customRotate2.get().floatValue(), Animations.customRotate3.get().floatValue());
        }

        if (this.rotateTimer.hasTimePassed(1)) {
            ++this.delay;
            this.delay = this.delay + Animations.SpeedRotate.get();
            this.rotateTimer.reset();
        }
        if (this.delay > 360.0F) {
            this.delay = 0.0F;
        }
    }

    private void ItemRotate1() {
        if (Animations.doBlockTransformationsRotate.get().equalsIgnoreCase("Rotate1")) {
            GlStateManager.rotate(this.delay, 0.0F, 1.0F, 0.0F);
        }
        if (Animations.doBlockTransformationsRotate.get().equalsIgnoreCase("Rotate2")) {
            GlStateManager.rotate(this.delay, 1.0F, 1.0F, 0.0F);
        }
        if (Animations.transformFirstPersonRotate.get().equalsIgnoreCase("Custom")) {
            GlStateManager.rotate(this.delay, Animations.customRotate1.get().floatValue(), Animations.customRotate2.get().floatValue(), Animations.customRotate3.get().floatValue());
        }

        if (this.rotateTimer.hasTimePassed(1)) {
            ++this.delay;
            this.delay = this.delay + Animations.SpeedRotate.get();
            this.rotateTimer.reset();
        }
        if (this.delay > 360.0F) {
            this.delay = 0.0F;
        }
    }

    private void doItemRenderGLTranslate(){
        final Animations animations = (Animations) LiquidBounce.moduleManager.getModule(Animations.class);
        GlStateManager.translate(animations.itemPosX.get(), animations.itemPosY.get(), animations.itemPosZ.get());
    }

    private void doItemRenderGLScale(){
        final Animations animations = (Animations) LiquidBounce.moduleManager.getModule(Animations.class);
        if (animations.getState()) {
            GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
        }else {
            GlStateManager.scale(0.4f, 0.4f, 0.4f);
        }
    }

    @Inject(method = "renderFireInFirstPerson", at = @At("HEAD"), cancellable = true)
    private void renderFireInFirstPerson(final CallbackInfo callbackInfo) {
        final AntiBlind antiBlind = (AntiBlind) LiquidBounce.moduleManager.getModule(AntiBlind.class);

        if (antiBlind.getState() && antiBlind.getFireEffect().get()) {
            //vanilla's method
            GlStateManager.color(1.0F, 1.0F, 1.0F, 0.9F);
            GlStateManager.depthFunc(519);
            GlStateManager.depthMask(false);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            GlStateManager.depthFunc(515);
            callbackInfo.cancel();
        }
    }
}