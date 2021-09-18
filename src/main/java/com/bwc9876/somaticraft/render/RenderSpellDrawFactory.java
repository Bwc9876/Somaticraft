package com.bwc9876.somaticraft.render;

import com.bwc9876.somaticraft.entity.EntitySpellDraw;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;


public class RenderSpellDrawFactory implements IRenderFactory<EntitySpellDraw> {

    public static final RenderSpellDrawFactory INSTANCE = new RenderSpellDrawFactory();

    @Override
    public Render<? super EntitySpellDraw> createRenderFor(RenderManager manager) {
        return new RenderSpellDraw(manager);
    }
}
