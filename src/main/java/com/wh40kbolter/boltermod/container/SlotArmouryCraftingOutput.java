package com.wh40kbolter.boltermod.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotArmouryCraftingOutput extends Slot {

    public SlotArmouryCraftingOutput(IInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false; // No se puede insertar manualmente
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return !getStack().isEmpty();
    }

    @Override
    public int getSlotStackLimit() {
        return 64;
    }
}
