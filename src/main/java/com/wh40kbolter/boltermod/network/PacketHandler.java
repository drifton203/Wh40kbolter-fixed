package com.wh40kbolter.boltermod.network;

import com.wh40kbolter.boltermod.BolterMod;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

    public static final SimpleNetworkWrapper CHANNEL =
            NetworkRegistry.INSTANCE.newSimpleChannel(BolterMod.MODID);

    private static int id = 0;

    public static void register() {
        CHANNEL.registerMessage(
                PacketArmouryAction.Handler.class,
                PacketArmouryAction.class,
                id++,
                Side.SERVER
        );

        CHANNEL.registerMessage(
                MessageReload.Handler.class,
                MessageReload.class,
                id++,
                Side.SERVER
        );

        CHANNEL.registerMessage(
                MessageFire.Handler.class,
                MessageFire.class,
                id++,
                Side.SERVER
        );
    }
}