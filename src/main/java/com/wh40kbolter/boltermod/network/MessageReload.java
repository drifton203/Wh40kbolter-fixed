package com.wh40kbolter.boltermod.network;

import com.wh40kbolter.boltermod.item.ItemBolter;
import com.wh40kbolter.boltermod.item.ItemMagazine;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageReload implements IMessage {

    private EnumHand hand;

    public MessageReload() {}

    public MessageReload(EnumHand hand) {
        this.hand = hand;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.hand = EnumHand.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(hand.ordinal());
    }

    public static class Handler implements IMessageHandler<MessageReload, IMessage> {

        @Override
        public IMessage onMessage(MessageReload message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                ItemStack stack = player.getHeldItem(message.hand);
                if (stack.isEmpty()) return;

                if (stack.getItem() instanceof ItemBolter) {
                    ItemBolter.tryReload(player, message.hand);
                } else if (stack.getItem() instanceof ItemMagazine) {
                    ItemMagazine.tryReload(player, message.hand);
                }
            });
            return null;
        }
    }
}