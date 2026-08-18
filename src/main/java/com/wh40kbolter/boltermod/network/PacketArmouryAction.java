package com.wh40kbolter.boltermod.network;

import com.wh40kbolter.boltermod.tileentity.TileEntityArmoury;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketArmouryAction implements IMessage {

    public static final int ACTION_SET_TAB = 0;
    public static final int ACTION_CRAFT_RECIPE = 1;

    private int action;
    private int value;
    private long posLong;

    public PacketArmouryAction() {
    }

    public PacketArmouryAction(int action, int value, BlockPos pos) {
        this.action = action;
        this.value = value;
        this.posLong = pos.toLong();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(action);
        buf.writeInt(value);
        buf.writeLong(posLong);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.action = buf.readInt();
        this.value = buf.readInt();
        this.posLong = buf.readLong();
    }

    public static class Handler implements IMessageHandler<PacketArmouryAction, IMessage> {

        @Override
        public IMessage onMessage(PacketArmouryAction message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                BlockPos pos = BlockPos.fromLong(message.posLong);
                TileEntity te = player.world.getTileEntity(pos);
                if (!(te instanceof TileEntityArmoury)) return;

                TileEntityArmoury armoury = (TileEntityArmoury) te;

                if (message.action == ACTION_SET_TAB) {
                    armoury.setActiveTab(message.value);
                } else if (message.action == ACTION_CRAFT_RECIPE) {
                    armoury.craft(message.value);
                }
            });
            return null;
        }
    }
}