package com.wh40kbolter.boltermod.handler;

import com.wh40kbolter.boltermod.BolterMod;
import com.wh40kbolter.boltermod.item.ItemBolter;
import com.wh40kbolter.boltermod.item.ItemMagazine;
import com.wh40kbolter.boltermod.network.MessageReload;
import com.wh40kbolter.boltermod.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KeyInputHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (BolterMod.RELOAD_KEY.isPressed()) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            if (player == null) return;

            ItemStack main = player.getHeldItem(EnumHand.MAIN_HAND);
            ItemStack off = player.getHeldItem(EnumHand.OFF_HAND);

            if (!main.isEmpty() && (main.getItem() instanceof ItemBolter || main.getItem() instanceof ItemMagazine)) {
                PacketHandler.CHANNEL.sendToServer(new MessageReload(EnumHand.MAIN_HAND));
            } else if (!off.isEmpty() && (off.getItem() instanceof ItemBolter || off.getItem() instanceof ItemMagazine)) {
                PacketHandler.CHANNEL.sendToServer(new MessageReload(EnumHand.OFF_HAND));
            }
        }
    }
}