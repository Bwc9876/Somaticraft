package com.bwc9876.somaticraft.item;

import com.bwc9876.somaticraft.entity.EntitySpellDraw;
import com.bwc9876.somaticraft.util.SpacialHelper;
import com.sun.javafx.geom.Vec2d;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;

public class BaseCaster extends Item {

    // Constants
    private static final double FACE_VEC_FACTOR_IN_DRAW_POINT = 2D;
    private static final short MAX_SPELL_POINTS = 20;

    // Spell Timing
    private static final long DELAY_BETWEEN_POINT_CREATION = 2;
    private static final long TIME_UNTIL_SPELL_CLEAR = 10;

    private EntitySpellDraw currentDrawing;
    private long last_time_used = -1;
    private Point3d lastPlayerPos;

    public BaseCaster(){

        // Setup Registry values
        this.setRegistryName("base_caster");
        this.setUnlocalizedName("base_caster");
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.setNoRepair();
        this.setMaxDamage(0);
        this.setMaxStackSize(1);

    }

    private void updatePointPos(EntityPlayer player) {

        // Get the player's current position as a Point3d
        final Point3d currentPlayerPos = SpacialHelper.entPosAsPoint3d(player);

        if (!currentPlayerPos.equals(lastPlayerPos)) {
            // If the player isn't in their last position, move the nodes with the player
            final Vec3d diff = SpacialHelper.distanceBetweenPoints(lastPlayerPos, currentPlayerPos);
            SpacialHelper.setEntPosToPoint3d(currentDrawing, SpacialHelper.applyVectorToPoint(SpacialHelper.entPosAsPoint3d(currentDrawing), diff));
        }

        // Set the lastPlayerPos to the current for next time
        lastPlayerPos = currentPlayerPos;

    }

    private Point3d CalcNewOrigin(EntityPlayer player) {

        // Get the direction the player is facing in vector form
        final Vec3d facingVec = SpacialHelper.playerFacingAsVector(player);

        // Get the position of the head of the player
        final Point3d playerHeadPos = SpacialHelper.playerHeadPos(player);

        final Vec3d new_facing = facingVec.scale(FACE_VEC_FACTOR_IN_DRAW_POINT);

        return SpacialHelper.applyVectorToPoint(playerHeadPos, new Vec3d(new_facing.x, 0, new_facing.z));

    }

    private Point3d CalcNewPoint(EntityPlayer player) {

        // Get the direction the player is facing in vector form
        final Vec3d facingVec = SpacialHelper.playerFacingAsVector(player);

        // Get the position of the head of the player
        final Point3d playerHeadPos = SpacialHelper.playerHeadPos(player);

        final Point3d originPos = SpacialHelper.entPosAsPoint3d(currentDrawing);
        final Vec3d diff = SpacialHelper.distanceBetweenPoints(playerHeadPos, originPos);
        final Vector2d adjacent = new Vector2d(diff.x, diff.z);
        final Vector2d hypo = new Vector2d(facingVec.x, facingVec.z);
        final double angle = adjacent.angle(hypo);
        final double horizontal_correction = FACE_VEC_FACTOR_IN_DRAW_POINT / Math.cos(angle) - FACE_VEC_FACTOR_IN_DRAW_POINT;

        // new_facing is a vector relative to the player's head, so we need to get the world position by applying that vector to the head.
        final Vector2d crunched_hypo = new Vector2d(SpacialHelper.crunchXZ(facingVec), facingVec.y * FACE_VEC_FACTOR_IN_DRAW_POINT);
        crunched_hypo.normalize();
        final Vector2d crunched_adjacent = new Vector2d(SpacialHelper.crunchXZ(diff), diff.y);
        final double crunched_angle = crunched_adjacent.angle(crunched_hypo);
        final double vertical_correction = FACE_VEC_FACTOR_IN_DRAW_POINT / Math.cos(crunched_angle) - FACE_VEC_FACTOR_IN_DRAW_POINT;

        final Vec3d new_xz = facingVec.scale(horizontal_correction + vertical_correction);
        final Vec3d final_facing = new Vec3d(new_xz.x, facingVec.y * FACE_VEC_FACTOR_IN_DRAW_POINT, new_xz.z);

        return SpacialHelper.applyVectorToPoint(playerHeadPos, final_facing);

    }

    private void RightClickDrawInit(EntityPlayer player, World worldIn) {

        if (currentDrawing == null) {

            currentDrawing = new EntitySpellDraw(worldIn);
            SpacialHelper.setEntPosToPoint3d(currentDrawing, CalcNewOrigin(player));
            currentDrawing.addPoint(CalcNewPoint(player));
            currentDrawing.genBounds(player, FACE_VEC_FACTOR_IN_DRAW_POINT);
            worldIn.spawnEntity(currentDrawing);
            lastPlayerPos = SpacialHelper.entPosAsPoint3d(player);

        } else {

            if (currentDrawing.pointsLength() <= MAX_SPELL_POINTS) {

                currentDrawing.addPoint(CalcNewPoint(player));

            } else {

                cancelSpell(false);

            }

        }

    }


    private void cancelSpell(boolean instant) {
        // Make the drawing slowly disappear or just vanish, depends on "instant"
        if (currentDrawing != null) {
            if (instant) currentDrawing.setDead();
            else currentDrawing.beginDisappearing();
        }

        // Set the last time used value to -1 and lastPlayerPos value to null so that way we don't do unnecessary calculations in .onUpdate()
        last_time_used = -1;
        lastPlayerPos = null;
        currentDrawing = null;
    }

    @Override
    @MethodsReturnNonnullByDefault
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

        if (!worldIn.isRemote) {

            // If we're currently having the drawing follow the player, update the points so there position moves where the player moves
            if (currentDrawing != null && lastPlayerPos != null && entityIn instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entityIn;
                updatePointPos(player);
            }

            // If the user hasn't right-clicked in TIME_UNTIL_SPELL_CLEAR, we clear the spell
            if (last_time_used != -1 && worldIn.getTotalWorldTime() - last_time_used > TIME_UNTIL_SPELL_CLEAR) {
                cancelSpell(false);
            }

            // If the player switched off the caster, cancel the spell instantaneously
            if (currentDrawing != null && entityIn instanceof EntityPlayer && !isSelected) {
                cancelSpell(true);
            }

        }

    }

    @Override
    @MethodsReturnNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        // Delay uses between DELAY_BETWEEN_POINT_CREATION to stop some wonky math from occurring
        if (worldIn.getTotalWorldTime() - last_time_used > DELAY_BETWEEN_POINT_CREATION) {
            last_time_used = worldIn.getTotalWorldTime();
            RightClickDrawInit(playerIn, worldIn);
        }
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

}
