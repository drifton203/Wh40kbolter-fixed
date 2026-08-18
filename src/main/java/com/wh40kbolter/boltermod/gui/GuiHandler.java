package com.wh40kbolter.boltermod.gui;

import com.wh40kbolter.boltermod.container.ContainerArmoury;
import com.wh40kbolter.boltermod.tileentity.TileEntityArmoury;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public static final int GUI_ARMOURY = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GUI_ARMOURY) {
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
            if (te instanceof TileEntityArmoury) {
                return new ContainerArmoury(player.inventory, (TileEntityArmoury) te);
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GUI_ARMOURY) {
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
            if (te instanceof TileEntityArmoury) {
                return new GuiArmoury(new ContainerArmoury(player.inventory, (TileEntityArmoury) te), (TileEntityArmoury) te, player);
            }
        }
        return null;
    }
}