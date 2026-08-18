package com.wh40kbolter.boltermod.container;

import com.wh40kbolter.boltermod.item.ItemBolter;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBolterInput extends Slot {

    public SlotBolterInput(IInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemBolter;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }
}
