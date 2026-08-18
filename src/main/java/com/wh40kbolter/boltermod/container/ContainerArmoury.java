package com.wh40kbolter.boltermod.container;

import com.wh40kbolter.boltermod.crafting.ArmouryRecipe;
import com.wh40kbolter.boltermod.crafting.ArmouryRecipeRegistry;
import com.wh40kbolter.boltermod.item.ItemBolter;
import com.wh40kbolter.boltermod.tileentity.TileEntityArmoury;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class  ContainerArmoury extends Container {

    private final TileEntityArmoury te;
    private final IItemHandler handler;

    public ContainerArmoury(InventoryPlayer playerInv, TileEntityArmoury te) {
        this.te = te;
        this.handler = te.getInventory();

        // Crafting input 2x2
        addSlotToContainer(new SlotItemHandler(handler, 0, 30, 25));
        addSlotToContainer(new SlotItemHandler(handler, 1, 52, 25));
        addSlotToContainer(new SlotItemHandler(handler, 2, 30, 47));
        addSlotToContainer(new SlotItemHandler(handler, 3, 52, 47));

        // Crafting output
        addSlotToContainer(new SlotArmouryCraftingOutput(handler, 4, 116, 36));

        // Upgrade bolter slot
        addSlotToContainer(new SlotBolterInput(handler, 5, 30, 36));

        // Upgrade materials
        addSlotToContainer(new SlotItemHandler(handler, 6, 80, 20));
        addSlotToContainer(new SlotItemHandler(handler, 7, 102, 20));
        addSlotToContainer(new SlotItemHandler(handler, 8, 80, 42));
        addSlotToContainer(new SlotItemHandler(handler, 9, 102, 42));

        // Player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Hotbar
        for (int col = 0; col < 9; col++) {
            addSlotToContainer(new Slot(playerInv, col, 8 + col * 18, 142));
        }

        updateCraftingOutput();
    }

    public void updateCraftingOutput() {
        ItemStack result = ItemStack.EMPTY;
        for (ArmouryRecipe recipe : ArmouryRecipeRegistry.RECIPES) {
            if (recipe.matches(handler)) {
                result = recipe.getResult();
                break;
            }
        }
        handler.setStackInSlot(4, result);
    }

    public boolean doCraft() {
        for (ArmouryRecipe recipe : ArmouryRecipeRegistry.RECIPES) {
            if (recipe.matches(handler)) {
                if (handler.getStackInSlot(4).isEmpty()) return false;
                if (recipe.craft(handler)) {
                    handler.setStackInSlot(4, recipe.getResult().copy());
                    te.markDirty();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean applyUpgrade(int statIndex) {
        ItemStack bolterStack = handler.getStackInSlot(5);
        if (bolterStack.isEmpty() || !(bolterStack.getItem() instanceof ItemBolter)) return false;

        NBTTagCompound tag = ItemBolter.getOrCreateNBT(bolterStack);
        String[] keys = {
                ItemBolter.NBT_LVL_DAMAGE,
                ItemBolter.NBT_LVL_SPEED,
                ItemBolter.NBT_LVL_RADIUS,
                ItemBolter.NBT_LVL_AMMO,
                ItemBolter.NBT_LVL_DURA
        };

        if (statIndex < 0 || statIndex >= keys.length) return false;

        int current = tag.getInteger(keys[statIndex]);
        if (current >= 5) return false;

        ItemStack material = handler.getStackInSlot(6);
        if (material.isEmpty() || material.getCount() < (current + 1)) return false;

        material.shrink(current + 1);
        if (material.isEmpty()) {
            handler.setStackInSlot(6, ItemStack.EMPTY);
        }

        tag.setInteger(keys[statIndex], current + 1);
        te.markDirty();
        return true;
    }

    public boolean changeSkin(int skinIndex) {
        ItemStack bolterStack = handler.getStackInSlot(5);
        if (bolterStack.isEmpty() || !(bolterStack.getItem() instanceof ItemBolter)) return false;
        if (skinIndex < 0 || skinIndex > 2) return false;

        NBTTagCompound tag = ItemBolter.getOrCreateNBT(bolterStack);
        tag.setInteger(ItemBolter.NBT_SKIN, skinIndex);
        te.markDirty();
        return true;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return te.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            result = stack.copy();

            if (index < 10) {
                if (!mergeItemStack(stack, 10, inventorySlots.size(), true)) return ItemStack.EMPTY;
            } else {
                if (!mergeItemStack(stack, 0, 10, false)) return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();
        }
        return result;
    }

    public TileEntityArmoury getTileEntity() {
        return te;
    }
}
