package com.wh40kbolter.boltermod.handler;

import com.wh40kbolter.boltermod.item.ItemBolter;
import com.wh40kbolter.boltermod.item.ItemMagazine;
import com.wh40kbolter.boltermod.network.MessageFire;
import com.wh40kbolter.boltermod.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlayerEventHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen != null) return;
        if (mc.player == null) return;

        if (mc.gameSettings.keyBindAttack.isKeyDown()) {
            EntityPlayer player = mc.player;
            ItemStack main = player.getHeldItem(EnumHand.MAIN_HAND);
            ItemStack off = player.getHeldItem(EnumHand.OFF_HAND);

            if (!main.isEmpty() && main.getItem() instanceof ItemBolter) {
                PacketHandler.CHANNEL.sendToServer(new MessageFire(EnumHand.MAIN_HAND));
            } else if (!off.isEmpty() && off.getItem() instanceof ItemBolter) {
                PacketHandler.CHANNEL.sendToServer(new MessageFire(EnumHand.OFF_HAND));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side != Side.SERVER || event.phase != TickEvent.Phase.START) return;

        EntityPlayer player = event.player;
        ItemStack main = player.getHeldItem(EnumHand.MAIN_HAND);
        if (main.isEmpty()) return;

        if (main.getItem() instanceof ItemBolter) {
            NBTTagCompound tag = ItemBolter.getOrCreateNBT(main);
            int cooldown = tag.getInteger(ItemBolter.NBT_COOLDOWN);
            if (cooldown > 0) {
                tag.setInteger(ItemBolter.NBT_COOLDOWN, cooldown - 1);
            }
            ItemBolter.tickReload(player, main);
        } else if (main.getItem() instanceof ItemMagazine) {
            ItemMagazine.tickReload(player, main);
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player.world.isRemote) return;

        ItemStack main = player.getHeldItem(EnumHand.MAIN_HAND);
        if (!main.isEmpty() && main.getItem() instanceof ItemBolter) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player.world.isRemote) return;

        ItemStack main = player.getHeldItem(EnumHand.MAIN_HAND);
        if (!main.isEmpty() && main.getItem() instanceof ItemBolter) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player.world.isRemote) return;

        ItemStack main = player.getHeldItem(EnumHand.MAIN_HAND);
        if (!main.isEmpty() && main.getItem() instanceof ItemBolter) {
            event.setCanceled(true);
        }
    }
}