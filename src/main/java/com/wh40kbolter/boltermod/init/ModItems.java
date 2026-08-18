package com.wh40kbolter.boltermod.init;

import com.wh40kbolter.boltermod.BolterMod;
import com.wh40kbolter.boltermod.item.ItemAmmo;
import com.wh40kbolter.boltermod.item.ItemBolter;
import com.wh40kbolter.boltermod.item.ItemMagazine;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

    public static final ItemBolter BOLTER_NORMAL = new ItemBolter("bolter_normal", ItemBolter.BolterType.NORMAL);
    public static final ItemBolter BOLTER_TEMPESTUS = new ItemBolter("bolter_tempestus", ItemBolter.BolterType.TEMPESTUS);
    public static final ItemBolter BOLTER_ELITE = new ItemBolter("bolter_elite", ItemBolter.BolterType.ELITE);

    public static final ItemMagazine MAGAZINE_NORMAL = new ItemMagazine("magazine_normal", ItemMagazine.MagazineType.NORMAL);
    public static final ItemMagazine MAGAZINE_EXPANDED = new ItemMagazine("magazine_expanded", ItemMagazine.MagazineType.EXPANDED);
    public static final ItemMagazine MAGAZINE_ELITE = new ItemMagazine("magazine_elite", ItemMagazine.MagazineType.ELITE);

    public static final ItemAmmo AMMO_NORMAL = new ItemAmmo("ammo_normal", ItemAmmo.AmmoType.NORMAL);
    public static final ItemAmmo AMMO_KRAKEN = new ItemAmmo("ammo_kraken", ItemAmmo.AmmoType.KRAKEN);
    public static final ItemAmmo AMMO_IMPERATUS = new ItemAmmo("ammo_imperatus", ItemAmmo.AmmoType.IMPERATUS);

    public static void register() {
        registerItem(BOLTER_NORMAL);
        registerItem(BOLTER_TEMPESTUS);
        registerItem(BOLTER_ELITE);

        registerItem(MAGAZINE_NORMAL);
        registerItem(MAGAZINE_EXPANDED);
        registerItem(MAGAZINE_ELITE);

        registerItem(AMMO_NORMAL);
        registerItem(AMMO_KRAKEN);
        registerItem(AMMO_IMPERATUS);
    }

    private static void registerItem(Item item) {
        ForgeRegistries.ITEMS.register(item);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        registerModel(BOLTER_NORMAL, 0, "bolter_normal");
        registerModel(BOLTER_TEMPESTUS, 0, "bolter_tempestus");
        registerModel(BOLTER_ELITE, 0, "bolter_elite");

        registerModel(MAGAZINE_NORMAL, 0, "magazine_normal");
        registerModel(MAGAZINE_EXPANDED, 0, "magazine_expanded");
        registerModel(MAGAZINE_ELITE, 0, "magazine_elite");

        registerModel(AMMO_NORMAL, 0, "ammo_normal");
        registerModel(AMMO_KRAKEN, 0, "ammo_kraken");
        registerModel(AMMO_IMPERATUS, 0, "ammo_imperatus");
    }

    @SideOnly(Side.CLIENT)
    private static void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(
                item, meta,
                new ModelResourceLocation(BolterMod.MODID + ":" + name, "inventory")
        );
    }
}