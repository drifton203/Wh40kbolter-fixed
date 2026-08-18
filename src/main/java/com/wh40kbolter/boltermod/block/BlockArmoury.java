package com.wh40kbolter.boltermod.block;

import com.wh40kbolter.boltermod.BolterMod;
import com.wh40kbolter.boltermod.init.ModCreativeTab;
import com.wh40kbolter.boltermod.tileentity.TileEntityArmoury;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockArmoury extends BlockContainer {

    public BlockArmoury() {
        super(Material.IRON);
        setTranslationKey(BolterMod.MODID + ".armoury");
        setRegistryName(BolterMod.MODID, "armoury");
        setCreativeTab(ModCreativeTab.TAB);
        setHardness(3.0f);
        setResistance(10.0f);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityArmoury();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
                                    EntityPlayer player, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return true;

        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityArmoury) {
            player.openGui(BolterMod.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityArmoury) {
            ((TileEntityArmoury) te).dropInventory(world, pos);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, net.minecraft.world.IBlockAccess world,
                         BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(this));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}