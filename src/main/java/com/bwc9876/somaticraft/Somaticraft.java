package com.bwc9876.somaticraft;

import com.bwc9876.somaticraft.item.BaseCaster;
import com.bwc9876.somaticraft.proxy.CommonProxy;
import com.bwc9876.somaticraft.spell.TestSpell;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import org.apache.logging.log4j.Logger;

@Mod(modid = Somaticraft.MODID, name = Somaticraft.NAME, version = Somaticraft.VERSION, useMetadata = true)
public class Somaticraft
{
    public static final String MODID = "somaticraft";
    public static final String NAME = "Somaticraft";
    public static final String VERSION = "dev";

    // Items
    public static BaseCaster baseCaster;

    // Spells
    public static TestSpell testSpell;

    // Entity (Entries)
    public static EntityEntry spellDrawEntry;

    // Proxy
    @SidedProxy(clientSide = "com.bwc9876.somaticraft.proxy.ClientProxy", serverSide = "com.bwc9876.somaticraft.proxy.CommonProxy")
    public static CommonProxy proxy;

    private static Logger LOGGER;
    public static int ID = 1;


    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LOGGER = event.getModLog();
        proxy.preInit(event);
        LOGGER.debug("Somaticraft PreInit Finished");
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        LOGGER.debug("Somaticraft Init Finished");
    }

}

