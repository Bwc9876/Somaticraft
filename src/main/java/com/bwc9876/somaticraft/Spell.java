package com.bwc9876.somaticraft;


import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;

public class Spell extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<Spell>{

    // Properties
    private int spellCost = 10;
    private Color spellColor = Color.WHITE;

    protected void setSpellCost(int newSpellCost) {

        this.spellCost = newSpellCost;

    }

    protected void setSpellColor(Color newSpellColor) {

        this.spellColor = newSpellColor;

    }

    @MethodsReturnNonnullByDefault
    public boolean Trigger(EntityPlayer caster) {


        return true;

    }

}
