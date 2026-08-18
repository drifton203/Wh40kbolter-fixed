package com.wh40kbolter.boltermod.item;

import com.wh40kbolter.boltermod.BolterMod;
import com.wh40kbolter.boltermod.init.ModCreativeTab;
import net.minecraft.item.Item;

public class ItemBase extends Item {

    public ItemBase(String name) {
        setTranslationKey(BolterMod.MODID + "." + name);
        setRegistryName(BolterMod.MODID, name);
        setCreativeTab(ModCreativeTab.TAB);
    }
}
