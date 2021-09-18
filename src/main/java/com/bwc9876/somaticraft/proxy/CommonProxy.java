package com.bwc9876.somaticraft.proxy;

import com.bwc9876.somaticraft.entity.EntitySpellDraw;
import com.bwc9876.somaticraft.item.BaseCaster;
import com.bwc9876.somaticraft.Somaticraft;
import com.bwc9876.somaticraft.Spell;
import com.bwc9876.somaticraft.spell.TestSpell;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod.EventBusSubscriber(modid = Somaticraft.MODID)
public class CommonProxy {

    private static final Logger LOGGER = LogManager.getLogger("Somaticraft.Common");

    // Events

    public void preInit(FMLPreInitializationEvent event) {
        Somaticraft.baseCaster = new BaseCaster();
        Somaticraft.testSpell = new TestSpell();
        Somaticraft.spellDrawEntry = EntityEntryBuilder.create()
                .entity(EntitySpellDraw.class).name("spell_draw").id("spell_draw", Somaticraft.ID++)
                .tracker(10, 1, false).build();
        LOGGER.debug("Mod Content Construction Complete");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(Somaticraft.baseCaster);
        LOGGER.debug("Item Registration Complete");
    }

    @SubscribeEvent
    public static void registerSpells(RegistryEvent.Register<Spell> event) {
        event.getRegistry().registerAll(Somaticraft.testSpell);
        LOGGER.debug("Spell (Somaticraft) Registration Complete");
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().registerAll(Somaticraft.spellDrawEntry);
        LOGGER.debug("Entity Registration Complete");
    }

}
