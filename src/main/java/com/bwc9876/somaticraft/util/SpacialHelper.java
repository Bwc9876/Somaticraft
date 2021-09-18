package com.bwc9876.somaticraft.util;

import com.sun.javafx.geom.Vec2d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;

public class SpacialHelper {

    private static final Logger LOGGER = LogManager.getLogger("Somaticraft.Spatial_Helper");

    public static Point3d applyVectorToPoint(Point3d pointIn, Vec3d vecIn) {

        return new Point3d(pointIn.x + vecIn.x, pointIn.y + vecIn.y, pointIn.z + vecIn.z);

    }

    public static Point2d applyVectorToPoint(Point2d pointIn, Vec2d vecIn) {

        return new Point2d(pointIn.x + vecIn.x, pointIn.y + vecIn.y);

    }

    public static Vec2d scaleVec(Vec2d vecIn, double factor) {

        return new Vec2d(vecIn.x * factor, vecIn.y * factor);

    }

    public static Vec3d distanceBetweenPoints(Point3d from, Point3d to) {

        return new Vec3d(to.x - from.x, to.y - from.y, to.z - from.z);

    }

    public static void setEntPosToPoint3d(Entity entityIn, Point3d pointIn) {

        entityIn.setPosition(pointIn.x, pointIn.y, pointIn.z);

    }

    public static Point3d entPosAsPoint3d(Entity entityIn) {

        return new Point3d(entityIn.posX, entityIn.posY, entityIn.posZ);

    }

    public static Vec3d playerFacingAsVector(EntityPlayer player) {

        final float v1x = -MathHelper.sin(player.rotationYaw * 0.017453292F) * MathHelper.cos(player.rotationPitch * 0.017453292F);
        final float v1y = -MathHelper.sin(player.rotationPitch * 0.017453292F);
        final float v1z = MathHelper.cos(player.rotationYaw * 0.017453292F) * MathHelper.cos(player.rotationPitch * 0.017453292F);
        return new Vec3d(v1x, v1y, v1z).normalize();

    }

    public static Point3d playerHeadPos(EntityPlayer player) {

        return new Point3d(player.posX, player.posY + (double)player.getEyeHeight() - 0.10000000149011612D, player.posZ);

    }

    public static void debugLog(Point3d pointIn) {

        LOGGER.debug("(" + pointIn.x + ", " + pointIn.y + ", " + pointIn.z + ")");

    }

    public static void debugLog(Vec3d vecIn) {

        LOGGER.debug("(" + vecIn.x + ", " + vecIn.y + ", " + vecIn.z + ")");

    }

    public static Vec2d normalize(Vec2d vecIn) {

        Vec3d interVec = new Vec3d(vecIn.x, vecIn.y, 0).normalize();
        return new Vec2d(interVec.x, interVec.y);

    }

    public static Vec2d xzAs2D(Vec3d vecIn){
        return new Vec2d(vecIn.x, vecIn.z);
    }

    public static Point2d xzAs2D(Point3d pointIn) {
        return new Point2d(pointIn.x, pointIn.z);
    }

    public static double crunchXZ(Vec3d vecIn) {
        return Math.sqrt(Math.pow(vecIn.x, 2) + Math.pow(vecIn.z, 2));
    }

    public static Vec2d reScale(Vec2d vecIn, double factor) {

        return scaleVec(normalize(vecIn), factor);

    }

    public static Vec2d rotate(Vec2d vecIn, double angle) {

        final double sin = Math.sin(angle);
        final double cos = Math.cos(angle);
        return new Vec2d(vecIn.x * cos + vecIn.y * sin, -vecIn.x * sin + vecIn.y * cos);

    }

}
