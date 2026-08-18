package com.wh40kbolter.boltermod.item;

import com.wh40kbolter.boltermod.BolterMod;
import com.wh40kbolter.boltermod.init.ModCreativeTab;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemAmmo extends Item {

    public enum AmmoType {
        NORMAL, KRAKEN, IMPERATUS
    }

    private final AmmoType type;

    public ItemAmmo(String name, AmmoType type) {
        this.type = type;
        this.setRegistryName(BolterMod.MODID, name);
        this.setTranslationKey(BolterMod.MODID + "." + name);
        this.setCreativeTab(ModCreativeTab.TAB);
        this.setMaxStackSize(64);
    }

    public AmmoType getType() {
        return type;
    }

    public boolean isCompatible(ItemStack bolterStack) {
        if (!(bolterStack.getItem() instanceof ItemBolter)) return false;
        ItemBolter bolter = (ItemBolter) bolterStack.getItem();
        switch (type) {
            case NORMAL:
                return bolter.getType() == ItemBolter.BolterType.NORMAL;
            case KRAKEN:
                return bolter.getType() == ItemBolter.BolterType.TEMPESTUS;
            case IMPERATUS:
                return bolter.getType() == ItemBolter.BolterType.ELITE;
            default:
                return false;
        }
    }
}