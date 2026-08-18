package com.wh40kbolter.boltermod.init;

import com.wh40kbolter.boltermod.BolterMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ModCreativeTab {

    public static final CreativeTabs TAB = new CreativeTabs(BolterMod.MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.BOLTER_NORMAL);
        }
    };
}