package com.wh40kbolter.boltermod.init;

import com.wh40kbolter.boltermod.BolterMod;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {

    public static void register() {
        GameRegistry.addShapedRecipe(
                new ResourceLocation(BolterMod.MODID, "bolter_normal"),
                null,
                new ItemStack(ModItems.BOLTER_NORMAL),
                "III", "ITI", " S ",
                'I', Items.IRON_INGOT,
                'T', Blocks.TNT,
                'S', Items.STICK
        );

        GameRegistry.addShapedRecipe(
                new ResourceLocation(BolterMod.MODID, "bolter_tempestus"),
                null,
                new ItemStack(ModItems.BOLTER_TEMPESTUS),
                "IDI", "ITI", " S ",
                'I', Items.IRON_INGOT,
                'D', Items.DIAMOND,
                'T', Blocks.TNT,
                'S', Items.STICK
        );

        GameRegistry.addShapedRecipe(
                new ResourceLocation(BolterMod.MODID, "bolter_elite"),
                null,
                new ItemStack(ModItems.BOLTER_ELITE),
                "IOI", "ITI", " S ",
                'I', Items.IRON_INGOT,
                'O', Blocks.OBSIDIAN,
                'T', Blocks.TNT,
                'S', Items.STICK
        );

        GameRegistry.addShapedRecipe(
                new ResourceLocation(BolterMod.MODID, "magazine_normal"),
                null,
                new ItemStack(ModItems.MAGAZINE_NORMAL),
                " I ", "IGI", " I ",
                'I', Items.IRON_INGOT,
                'G', Items.GUNPOWDER
        );

        GameRegistry.addShapedRecipe(
                new ResourceLocation(BolterMod.MODID, "magazine_expanded"),
                null,
                new ItemStack(ModItems.MAGAZINE_EXPANDED),
                "III", "IGI", "III",
                'I', Items.IRON_INGOT,
                'G', Items.GUNPOWDER
        );

        GameRegistry.addShapedRecipe(
                new ResourceLocation(BolterMod.MODID, "magazine_elite"),
                null,
                new ItemStack(ModItems.MAGAZINE_ELITE),
                "IOI", "OGO", "IOI",
                'I', Items.IRON_INGOT,
                'O', Blocks.OBSIDIAN,
                'G', Items.GUNPOWDER
        );

        GameRegistry.addShapedRecipe(
                new ResourceLocation(BolterMod.MODID, "ammo_normal"),
                null,
                new ItemStack(ModItems.AMMO_NORMAL, 32),
                " G ", " I ", " G ",
                'G', Items.GUNPOWDER,
                'I', Items.IRON_NUGGET
        );

        GameRegistry.addShapedRecipe(
                new ResourceLocation(BolterMod.MODID, "ammo_kraken"),
                null,
                new ItemStack(ModItems.AMMO_KRAKEN, 32),
                " D ", " I ", " G ",
                'D', Items.DIAMOND,
                'I', Items.IRON_NUGGET,
                'G', Items.GUNPOWDER
        );

        GameRegistry.addShapedRecipe(
                new ResourceLocation(BolterMod.MODID, "ammo_imperatus"),
                null,
                new ItemStack(ModItems.AMMO_IMPERATUS, 32),
                " O ", " I ", " G ",
                'O', Blocks.OBSIDIAN,
                'I', Items.IRON_NUGGET,
                'G', Items.GUNPOWDER
        );

        GameRegistry.addShapedRecipe(
                new ResourceLocation(BolterMod.MODID, "armoury"),
                null,
                new ItemStack(ModBlocks.ARMOURY),
                "ICI", "I I", "III",
                'I', Items.IRON_INGOT,
                'C', Blocks.CHEST
        );
    }
}