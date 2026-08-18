package com.wh40kbolter.boltermod.crafting;

import com.wh40kbolter.boltermod.init.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArmouryRecipeRegistry {

    public static final List<ArmouryRecipe> RECIPES = new ArrayList<>();

    public static void init() {
        RECIPES.add(new ArmouryRecipe("imperial_powder",
                new ItemStack(ModItems.IMPERIAL_POWDER, 9),
                Arrays.asList(new IngredientStack(new ItemStack(Items.GUNPOWDER, 9)))));

        RECIPES.add(new ArmouryRecipe("gunpowder",
                new ItemStack(Items.GUNPOWDER, 32),
                Arrays.asList(
                        new IngredientStack(new ItemStack(Blocks.COBBLESTONE, 1)),
                        new IngredientStack(new ItemStack(Items.BLAZE_ROD, 1))
                )));

        RECIPES.add(new ArmouryRecipe("iron_plate",
                new ItemStack(ModItems.IRON_PLATE, 1),
                Arrays.asList(new IngredientStack(new ItemStack(Items.IRON_INGOT, 3)))));

        RECIPES.add(new ArmouryRecipe("gold_plate",
                new ItemStack(ModItems.GOLD_PLATE, 1),
                Arrays.asList(new IngredientStack(new ItemStack(Items.GOLD_INGOT, 3)))));

        RECIPES.add(new ArmouryRecipe("obsidian_plate",
                new ItemStack(ModItems.OBSIDIAN_PLATE, 1),
                Arrays.asList(new IngredientStack(new ItemStack(Blocks.OBSIDIAN, 3)))));

        RECIPES.add(new ArmouryRecipe("magazine_normal",
                new ItemStack(ModItems.MAGAZINE_NORMAL, 2),
                Arrays.asList(
                        new IngredientStack(new ItemStack(Items.IRON_INGOT, 1)),
                        new IngredientStack(new ItemStack(ModItems.IRON_PLATE, 3))
                )));

        RECIPES.add(new ArmouryRecipe("bolter_part",
                new ItemStack(ModItems.BOLTER_PART, 1),
                Arrays.asList(new IngredientStack(new ItemStack(ModItems.IRON_PLATE, 3)))));

        RECIPES.add(new ArmouryRecipe("bolter_normal",
                new ItemStack(ModItems.BOLTER_NORMAL, 1),
                Arrays.asList(
                        new IngredientStack(new ItemStack(ModItems.BOLTER_PART, 4)),
                        new IngredientStack(new ItemStack(ModItems.GOLD_PLATE, 2)),
                        new IngredientStack(new ItemStack(ModItems.OBSIDIAN_PLATE, 4))
                )));

        RECIPES.add(new ArmouryRecipe("bolter_tempestus",
                new ItemStack(ModItems.BOLTER_TEMPESTUS, 1),
                Arrays.asList(
                        new IngredientStack(new ItemStack(ModItems.BOLTER_NORMAL, 1)),
                        new IngredientStack(new ItemStack(ModItems.BOLTER_PART, 3)),
                        new IngredientStack(new ItemStack(ModItems.GOLD_PLATE, 5)),
                        new IngredientStack(new ItemStack(ModItems.IRON_PLATE, 5)),
                        new IngredientStack(new ItemStack(ModItems.OBSIDIAN_PLATE, 3))
                )));

        RECIPES.add(new ArmouryRecipe("magazine_expanded",
                new ItemStack(ModItems.MAGAZINE_EXPANDED, 1),
                Arrays.asList(
                        new IngredientStack(new ItemStack(ModItems.MAGAZINE_NORMAL, 1)),
                        new IngredientStack(new ItemStack(Items.GOLD_INGOT, 1)),
                        new IngredientStack(new ItemStack(ModItems.GOLD_PLATE, 3))
                )));

        RECIPES.add(new ArmouryRecipe("bolter_elite",
                new ItemStack(ModItems.BOLTER_ELITE, 1),
                Arrays.asList(
                        new IngredientStack(new ItemStack(ModItems.BOLTER_TEMPESTUS, 1)),
                        new IngredientStack(new ItemStack(ModItems.BOLTER_PART, 7)),
                        new IngredientStack(new ItemStack(ModItems.GOLD_PLATE, 10)),
                        new IngredientStack(new ItemStack(ModItems.IRON_PLATE, 20)),
                        new IngredientStack(new ItemStack(ModItems.OBSIDIAN_PLATE, 15))
                )));

        RECIPES.add(new ArmouryRecipe("magazine_elite",
                new ItemStack(ModItems.MAGAZINE_ELITE, 1),
                Arrays.asList(
                        new IngredientStack(new ItemStack(ModItems.MAGAZINE_NORMAL, 1)),
                        new IngredientStack(new ItemStack(Blocks.OBSIDIAN, 7)),
                        new IngredientStack(new ItemStack(ModItems.GOLD_PLATE, 15))
                )));
    }

    public static ArmouryRecipe getRecipe(int index) {
        if (index < 0 || index >= RECIPES.size()) return null;
        return RECIPES.get(index);
    }
}