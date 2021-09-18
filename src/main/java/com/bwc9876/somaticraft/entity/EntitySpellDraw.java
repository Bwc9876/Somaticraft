package com.bwc9876.somaticraft.entity;

import com.bwc9876.somaticraft.util.SpacialHelper;
import com.sun.javafx.geom.Vec2d;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import java.awt.*;

public class EntitySpellDraw extends Entity {

    private static final double DISAPPEAR_TIME = 0.5D; // (in seconds)
    private static final int BOUND_COLOR_STEP = 5; // (in hue)
    private static final double DRAW_BOUNDS = 1;
    private static final Logger LOGGER = LogManager.getLogger("Somaticraft.SpellDraw");

    private static final int disappearStep = (int)(255D / (DISAPPEAR_TIME * 20D));

    private Color color = new Color(255, 255, 255, 255);
    private Color boundColor = Color.RED;
    private int current_hue = 0;
    private boolean beganDisappearing = false;

    final private ObjectArrayList<Vec3d> points;
    final private ObjectArrayList<Vec3d> boundPoints;

    public EntitySpellDraw(World worldIn) {
        super(worldIn);
        points = new ObjectArrayList<>();
        boundPoints = new ObjectArrayList<>();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (beganDisappearing) {
            final int new_val = Math.max(color.getAlpha() - disappearStep, 0);
            setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), new_val));
            if (new_val == 0) {
                setDead();
            }
        } else {
            int new_hue = current_hue + BOUND_COLOR_STEP;
            new_hue = new_hue > 360 ? new_hue - 360 : new_hue;
            boundColor = Color.getHSBColor((float) new_hue / 360F, 1, 1);
            current_hue = new_hue;
        }
    }

    public void addPoint(Point3d pointIn) {
        points.add(SpacialHelper.distanceBetweenPoints(SpacialHelper.entPosAsPoint3d(this), pointIn));
    }

    public int pointsLength() {
        return points.size();
    }

    public Vec3d getPoint(int index) {
        return points.get(index);
    }

    public void beginDisappearing() {
        beganDisappearing = true;
        boundPoints.clear();
    }

    public void setColor(Color newColor) {
        color = newColor;
    }

    public Color getColor() {
        return color;
    }

    public Color getBoundsColor() {
        return boundColor;
    }

    public int boundLength() {
        return boundPoints.size();
    }

    public Vec3d getBound(int index) {
        return boundPoints.get(index);
    }

    public void genBounds(EntityPlayer player, double vecFactor) {

        final Point3d playerHeadPos = SpacialHelper.playerHeadPos(player);
        final Point2d playerHeadXZ = SpacialHelper.xzAs2D(playerHeadPos);
        final Point3d thisPos = SpacialHelper.entPosAsPoint3d(this);

        final double magnitude = Math.sqrt(Math.pow(vecFactor, 2) + Math.pow(DRAW_BOUNDS, 2));
        final double angle = Math.acos(vecFactor / magnitude);
        final Vec2d source_vec = SpacialHelper.xzAs2D(SpacialHelper.distanceBetweenPoints(playerHeadPos, thisPos).normalize());
        final Vec2d new_vec = SpacialHelper.reScale(SpacialHelper.rotate(source_vec, angle), magnitude);
        final Vec2d new_vec_negative = SpacialHelper.reScale(SpacialHelper.rotate(source_vec, -angle), magnitude);

        final double new_y = playerHeadPos.y + DRAW_BOUNDS;
        final double new_y_negative = playerHeadPos.y - DRAW_BOUNDS;

        final Point2d new_point = SpacialHelper.applyVectorToPoint(playerHeadXZ, new_vec);
        final Point2d new_negative = SpacialHelper.applyVectorToPoint(playerHeadXZ, new_vec_negative);

        final Vec3d quadrant2 = SpacialHelper.distanceBetweenPoints(thisPos, new Point3d(new_point.x, new_y, new_point.y));
        final Vec3d quadrant1 = SpacialHelper.distanceBetweenPoints(thisPos, new Point3d(new_negative.x, new_y, new_negative.y));
        final Vec3d quadrant4 = SpacialHelper.distanceBetweenPoints(thisPos, new Point3d(new_negative.x, new_y_negative, new_negative.y));
        final Vec3d quadrant3 = SpacialHelper.distanceBetweenPoints(thisPos, new Point3d(new_point.x, new_y_negative, new_point.y));

        boundPoints.add(quadrant2);
        boundPoints.add(quadrant1);
        boundPoints.add(quadrant4);
        boundPoints.add(quadrant3);

    }

    @Override
    public boolean writeToNBTOptional(NBTTagCompound compound) {
        return false;
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }
}
