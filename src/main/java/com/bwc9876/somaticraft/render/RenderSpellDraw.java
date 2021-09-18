package com.bwc9876.somaticraft.render;

import com.bwc9876.somaticraft.entity.EntitySpellDraw;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.logging.Logger;

public class RenderSpellDraw extends Render<EntitySpellDraw> {

    public RenderSpellDraw(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntitySpellDraw entity, double x, double y, double z, float entityYaw, float partialTicks) {

        // Initialize rendering
        GlStateManager.pushMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();

        if (entity.pointsLength() >= 2) {

            final Vec3d posFromCamera = new Vec3d(x, y, z);

            final Color color = entity.getColor();
            final int red = color.getRed();
            final int green = color.getGreen();
            final int blue = color.getBlue();
            final int alpha = color.getAlpha();

            for (int i = 0; i + 1 < entity.pointsLength() - 1; i++) {

                // Begin Drawing
                bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);

                Vec3d this_point = posFromCamera.add(entity.getPoint(i));
                Vec3d next_point = posFromCamera.add(entity.getPoint(i + 1));

                bufferbuilder.pos(this_point.x, this_point.y, this_point.z).color(red, green, blue, alpha).endVertex();
                bufferbuilder.pos(next_point.x, next_point.y, next_point.z).color(red, green, blue, alpha).endVertex();

                // End Drawing
                tessellator.draw();

            }

        }

        if (entity.boundLength() == 4) {

            final Vec3d posFromCamera = new Vec3d(x, y, z);

            final Color color = entity.getBoundsColor();
            final int red = color.getRed();
            final int green = color.getGreen();
            final int blue = color.getBlue();
            final int alpha = color.getAlpha();

            for (int i = 0; i <= 3; i++) {

                // Begin Drawing
                bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);

                Vec3d this_point = posFromCamera.add(entity.getBound(i));
                Vec3d next_point = posFromCamera.add(entity.getBound(i == 3? 0 : i + 1));

                bufferbuilder.pos(this_point.x, this_point.y, this_point.z).color(red, green, blue, alpha).endVertex();
                bufferbuilder.pos(next_point.x, next_point.y, next_point.z).color(red, green, blue, alpha).endVertex();

                // End Drawing
                tessellator.draw();

            }

        }

        // De-initialize rendering
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntitySpellDraw entity) {
        return null;
    }

}
