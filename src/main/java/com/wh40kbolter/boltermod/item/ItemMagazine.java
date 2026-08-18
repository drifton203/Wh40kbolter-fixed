package com.wh40kbolter.boltermod.item;

import com.wh40kbolter.boltermod.BolterMod;
import com.wh40kbolter.boltermod.init.ModCreativeTab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMagazine extends Item {

    public enum MagazineType {
        NORMAL(40),
        EXPANDED(80),
        ELITE(40);

        public final int capacity;

        MagazineType(int capacity) {
            this.capacity = capacity;
        }
    }

    public static final String NBT_AMMO = "Ammo";
    public static final String NBT_RELOADING = "Reloading";
    public static final String NBT_RELOAD_TICKS = "ReloadTicks";

    private final MagazineType type;

    public ItemMagazine(String name, MagazineType type) {
        this.type = type;
        this.setRegistryName(BolterMod.MODID, name);
        this.setTranslationKey(BolterMod.MODID + "." + name);
        this.setCreativeTab(ModCreativeTab.TAB);
        this.setMaxStackSize(1);
    }

    public MagazineType getType() {
        return type;
    }

    public static NBTTagCompound getOrCreateNBT(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger(NBT_AMMO, 0);
            tag.setBoolean(NBT_RELOADING, false);
            tag.setInteger(NBT_RELOAD_TICKS, 0);
            stack.setTagCompound(tag);
        }
        return stack.getTagCompound();
    }

    public static int getAmmo(ItemStack stack) {
        return getOrCreateNBT(stack).getInteger(NBT_AMMO);
    }

    public static void setAmmo(ItemStack stack, int ammo) {
        getOrCreateNBT(stack).setInteger(NBT_AMMO, Math.max(0, ammo));
    }

    public int getCapacity() {
        return type.capacity;
    }

    public boolean isCompatible(ItemStack bolterStack) {
        if (!(bolterStack.getItem() instanceof ItemBolter)) return false;
        ItemBolter bolter = (ItemBolter) bolterStack.getItem();
        switch (type) {
            case NORMAL:
                return bolter.getType() == ItemBolter.BolterType.NORMAL;
            case EXPANDED:
                return bolter.getType() == ItemBolter.BolterType.TEMPESTUS;
            case ELITE:
                return bolter.getType() == ItemBolter.BolterType.ELITE;
            default:
                return false;
        }
    }

    public boolean isCompatibleAmmo(ItemAmmo ammo) {
        switch (type) {
            case NORMAL:
                return ammo.getType() == ItemAmmo.AmmoType.NORMAL;
            case EXPANDED:
                return ammo.getType() == ItemAmmo.AmmoType.KRAKEN;
            case ELITE:
                return ammo.getType() == ItemAmmo.AmmoType.IMPERATUS;
            default:
                return false;
        }
    }

    public static void tryReload(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemMagazine)) return;

        World world = player.world;
        if (world.isRemote) return;

        NBTTagCompound tag = getOrCreateNBT(stack);
        int current = tag.getInteger(NBT_AMMO);
        int max = ((ItemMagazine) stack.getItem()).getCapacity();

        if (current >= max || tag.getBoolean(NBT_RELOADING)) return;

        tag.setBoolean(NBT_RELOADING, true);
        tag.setInteger(NBT_RELOAD_TICKS, 6);
    }

    public static void tickReload(EntityPlayer player, ItemStack magazineStack) {
        if (!(magazineStack.getItem() instanceof ItemMagazine)) return;

        NBTTagCompound tag = getOrCreateNBT(magazineStack);
        if (!tag.getBoolean(NBT_RELOADING)) return;

        int ticks = tag.getInteger(NBT_RELOAD_TICKS);
        if (ticks > 0) {
            tag.setInteger(NBT_RELOAD_TICKS, ticks - 1);
            return;
        }

        ItemMagazine magazine = (ItemMagazine) magazineStack.getItem();
        int current = tag.getInteger(NBT_AMMO);
        int max = magazine.getCapacity();
        if (current >= max) {
            tag.setBoolean(NBT_RELOADING, false);
            return;
        }

        ItemStack ammo = findAmmo(player, magazineStack);
        if (ammo.isEmpty()) {
            tag.setBoolean(NBT_RELOADING, false);
            return;
        }

        ammo.shrink(1);
        tag.setInteger(NBT_AMMO, current + 1);
        tag.setInteger(NBT_RELOAD_TICKS, 6);

        if (tag.getInteger(NBT_AMMO) >= max) {
            tag.setBoolean(NBT_RELOADING, false);
        }
    }

    private static ItemStack findAmmo(EntityPlayer player, ItemStack magazineStack) {
        ItemMagazine magazine = (ItemMagazine) magazineStack.getItem();
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemAmmo) {
                ItemAmmo ammo = (ItemAmmo) stack.getItem();
                if (magazine.isCompatibleAmmo(ammo)) {
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
        tooltip.add("Municion: " + getAmmo(stack) + "/" + getCapacity());
    }
}