package com.wh40kbolter.boltermod.crafting;

import net.minecraft.item.ItemStack;

public class IngredientStack {

    public final ItemStack stack;
    public final boolean ignoreNBT;

    public IngredientStack(ItemStack stack, boolean ignoreNBT) {
        this.stack = stack.copy();
        this.ignoreNBT = ignoreNBT;
    }

    public IngredientStack(ItemStack stack) {
        this(stack, true);
    }

    public boolean matches(ItemStack other) {
        if (other.isEmpty()) return false;
        if (ignoreNBT) {
            return other.getItem() == stack.getItem() && other.getMetadata() == stack.getMetadata();
        }
        return ItemStack.areItemStacksEqual(other, stack);
    }
}