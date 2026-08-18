package com.wh40kbolter.boltermod.crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ArmouryRecipe {

    public final String name;
    public final ItemStack output;
    private final List<IngredientStack> inputs;

    public ArmouryRecipe(String name, ItemStack output, List<IngredientStack> inputs) {
        this.name = name;
        this.output = output.copy();
        this.inputs = new ArrayList<>(inputs);
    }

    public boolean matches(IItemHandler inventory) {
        for (IngredientStack ingredient : inputs) {
            int found = 0;
            for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack slot = inventory.getStackInSlot(i);
                if (ingredient.matches(slot)) {
                    found += slot.getCount();
                    if (found >= ingredient.stack.getCount()) break;
                }
            }
            if (found < ingredient.stack.getCount()) return false;
        }
        return true;
    }

    public boolean craft(IItemHandler inventory) {
        if (!matches(inventory)) return false;

        for (IngredientStack ingredient : inputs) {
            int remaining = ingredient.stack.getCount();
            for (int i = 0; i < inventory.getSlots() && remaining > 0; i++) {
                ItemStack slot = inventory.getStackInSlot(i);
                if (ingredient.matches(slot)) {
                    int take = Math.min(remaining, slot.getCount());
                    slot.shrink(take);
                    remaining -= take;
                }
            }
        }
        return true;
    }
}