package com.wh40kbolter.boltermod.init;

import com.wh40kbolter.boltermod.BolterMod;
import com.wh40kbolter.boltermod.block.BlockArmoury;
import com.wh40kbolter.boltermod.tileentity.TileEntityArmoury;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

    public static final BlockArmoury ARMOURY = new BlockArmoury();

    public static void register() {
        ForgeRegistries.BLOCKS.register(ARMOURY);

        ItemBlock itemBlock = new ItemBlock(ARMOURY);
        itemBlock.setRegistryName(ARMOURY.getRegistryName());
        ForgeRegistries.ITEMS.register(itemBlock);

        // Fix: ResourceLocation requerido en 1.12.2
        GameRegistry.registerTileEntity(
                TileEntityArmoury.class,
                new ResourceLocation(BolterMod.MODID, "armoury")
        );
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(ARMOURY), 0,
                new ModelResourceLocation(BolterMod.MODID + ":armoury", "inventory")
        );
    }
}
