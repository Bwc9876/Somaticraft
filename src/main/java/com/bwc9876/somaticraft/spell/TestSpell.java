package com.bwc9876.somaticraft.spell;

import com.bwc9876.somaticraft.Spell;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;

public class TestSpell extends Spell {

    public TestSpell() {

        this.setRegistryName("xp_up");
        this.setSpellCost(5);
        this.setSpellColor(Color.GREEN);

    }

    @Override
    @MethodsReturnNonnullByDefault
    public boolean Trigger(EntityPlayer caster) {

        caster.experienceLevel += 5;

        return true;

    }

}
