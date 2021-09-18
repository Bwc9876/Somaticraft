package com.bwc9876.somaticraft.proxy;

import com.bwc9876.somaticraft.Somaticraft;
import com.bwc9876.somaticraft.entity.EntitySpellDraw;
import com.bwc9876.somaticraft.render.RenderSpellDrawFactory;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Somaticraft.MODID, value = Side.CLIENT)
public class ClientProxy extends CommonProxy {

    private static final Logger LOGGER = LogManager.getLogger("Somaticraft.Client");

    private static void registerRender(Item item, String variant) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), variant));
    }

    @SubscribeEvent
    public static void onRegisterModelsEvent(ModelRegistryEvent event) {

        registerTileEntityRenderers();
        LOGGER.debug("Tile Entity Registration Complete");
        registerEntityHandlers();
        LOGGER.debug("Entity RenderHandler Registration Complete");
        registerItemModels();
        LOGGER.debug("Item Model Registration Complete");

    }

    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event) {
        registerRender(Somaticraft.baseCaster, "inventory");
    }

    private static void registerTileEntityRenderers(){
        // ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExampleTileEntity.class, new RenderExampleTileEntity());
    }

    private static void registerEntityHandlers() {
        RenderingRegistry.registerEntityRenderingHandler(EntitySpellDraw.class, RenderSpellDrawFactory.INSTANCE);
    }

    private static void registerItemModels(){
        registerRender(Somaticraft.baseCaster, "normal");
    }

}
