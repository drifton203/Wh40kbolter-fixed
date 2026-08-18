package com.wh40kbolter.boltermod;

import com.wh40kbolter.boltermod.gui.GuiHandler;
import com.wh40kbolter.boltermod.handler.KeyInputHandler;
import com.wh40kbolter.boltermod.handler.PlayerEventHandler;
import com.wh40kbolter.boltermod.init.ModBlocks;
import com.wh40kbolter.boltermod.init.ModCreativeTab;
import com.wh40kbolter.boltermod.init.ModEntities;
import com.wh40kbolter.boltermod.init.ModItems;
import com.wh40kbolter.boltermod.init.ModRecipes;
import com.wh40kbolter.boltermod.network.PacketHandler;
import com.wh40kbolter.boltermod.proxy.CommonProxy;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

@Mod(
        modid = BolterMod.MODID,
        name = BolterMod.NAME,
        version = BolterMod.VERSION,
        acceptedMinecraftVersions = "[1.12.2]"
)
public class BolterMod {

    public static final String MODID = "wh40kbolter";
    public static final String NAME = "WH40K Bolter Mod";
    public static final String VERSION = "1.0.0";

    public static final String PROXY_CLIENT = "com.wh40kbolter.boltermod.proxy.ClientProxy";
    public static final String PROXY_COMMON = "com.wh40kbolter.boltermod.proxy.CommonProxy";

    @Instance(MODID)
    public static BolterMod instance;

    @SidedProxy(clientSide = PROXY_CLIENT, serverSide = PROXY_COMMON)
    public static CommonProxy proxy;

    public static CreativeTabs creativeTab;
    public static KeyBinding RELOAD_KEY;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        creativeTab = ModCreativeTab.TAB;

        ModItems.register();
        ModBlocks.register();
        ModEntities.register();
        PacketHandler.register();

        if (event.getSide() == Side.CLIENT) {
            RELOAD_KEY = new KeyBinding("key.reload", Keyboard.KEY_R, "key.categories.wh40kbolter");
            ClientRegistry.registerKeyBinding(RELOAD_KEY);
            MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
        }

        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());

        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ModRecipes.register();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}