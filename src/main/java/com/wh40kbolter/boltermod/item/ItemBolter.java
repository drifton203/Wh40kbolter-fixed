package com.wh40kbolter.boltermod.item;

import com.wh40kbolter.boltermod.BolterMod;
import com.wh40kbolter.boltermod.entity.EntityBolterProjectile;
import com.wh40kbolter.boltermod.init.ModCreativeTab;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBolter extends Item {

    public enum BolterType {
        NORMAL, TEMPESTUS, ELITE
    }

    public static final String NBT_AMMO = "Ammo";
    public static final String NBT_COOLDOWN = "Cooldown";
    public static final String NBT_RELOADING = "Reloading";
    public static final String NBT_RELOAD_TICKS = "ReloadTicks";
    public static final String NBT_LVL_SPEED = "LvlSpeed";
    public static final String NBT_LVL_RADIUS = "LvlRadius";
    public static final String NBT_LVL_AMMO = "LvlAmmo";
    public static final String NBT_LVL_DURA = "LvlDura";
    public static final String NBT_LVL_DAMAGE = "LvlDamage";
    public static final String NBT_SKIN = "Skin";
    public static final String NBT_ZERO_SPREAD = "ZeroSpread";

    public static final int BASE_AMMO_NORMAL = 40;
    public static final int BASE_AMMO_TEMPESTUS = 40;
    public static final int BASE_AMMO_ELITE = 40;

    public static final int COOLDOWN_NORMAL = 10;
    public static final int COOLDOWN_TEMPESTUS = 1;
    public static final int COOLDOWN_ELITE = 5;

    public static final float DAMAGE_NORMAL = 15.0f;
    public static final float DAMAGE_TEMPESTUS = 8.0f;
    public static final float DAMAGE_ELITE = 30.0f;

    public static final float RADIUS_NORMAL = 4.0f;
    public static final float RADIUS_TEMPESTUS = 2.0f;
    public static final float RADIUS_ELITE = 10.0f;

    public static final float AREA_DAMAGE_NORMAL = 7.5f;
    public static final float AREA_DAMAGE_TEMPESTUS = 8.0f;
    public static final float AREA_DAMAGE_ELITE = 30.0f;

    public static final float BASE_SPREAD = 2.0f;
    public static final float PROJECTILE_SPEED = 15.0f;

    private final BolterType type;

    public ItemBolter(String name, BolterType type) {
        this.type = type;
        this.setRegistryName(BolterMod.MODID, name);
        this.setTranslationKey(BolterMod.MODID + "." + name);
        this.setCreativeTab(ModCreativeTab.TAB);
        this.setMaxStackSize(1);
        this.setMaxDamage(500);
    }

    public BolterType getType() {
        return type;
    }

    public static NBTTagCompound getOrCreateNBT(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger(NBT_AMMO, getBaseAmmoForType(stack));
            tag.setInteger(NBT_COOLDOWN, 0);
            tag.setBoolean(NBT_RELOADING, false);
            tag.setInteger(NBT_RELOAD_TICKS, 0);
            tag.setInteger(NBT_LVL_DAMAGE, 0);
            tag.setInteger(NBT_LVL_SPEED, 0);
            tag.setInteger(NBT_LVL_RADIUS, 0);
            tag.setInteger(NBT_LVL_AMMO, 0);
            tag.setInteger(NBT_LVL_DURA, 0);
            tag.setInteger(NBT_SKIN, 0);
            tag.setBoolean(NBT_ZERO_SPREAD, false);
            stack.setTagCompound(tag);
        }
        return stack.getTagCompound();
    }

    public static int getBaseAmmoForType(ItemStack stack) {
        if (stack.getItem() instanceof ItemBolter) {
            ItemBolter bolter = (ItemBolter) stack.getItem();
            if (bolter.type == BolterType.ELITE) return BASE_AMMO_ELITE;
            if (bolter.type == BolterType.TEMPESTUS) return BASE_AMMO_TEMPESTUS;
        }
        return BASE_AMMO_NORMAL;
    }

    public static int getMaxAmmo(ItemStack stack) {
        NBTTagCompound tag = getOrCreateNBT(stack);
        int base = getBaseAmmoForType(stack);
        return base + tag.getInteger(NBT_LVL_AMMO) * 10;
    }

    public static int getCooldownTicks(ItemStack stack) {
        NBTTagCompound tag = getOrCreateNBT(stack);
        int speedLvl = tag.getInteger(NBT_LVL_SPEED);
        int base = COOLDOWN_NORMAL;
        if (stack.getItem() instanceof ItemBolter) {
            ItemBolter bolter = (ItemBolter) stack.getItem();
            switch (bolter.type) {
                case TEMPESTUS: base = COOLDOWN_TEMPESTUS; break;
                case ELITE: base = COOLDOWN_ELITE; break;
                default: base = COOLDOWN_NORMAL;
            }
        }
        return Math.max(1, base - speedLvl);
    }

    public static float getBolterDamage(ItemStack stack) {
        NBTTagCompound tag = getOrCreateNBT(stack);
        float base = DAMAGE_NORMAL;
        if (stack.getItem() instanceof ItemBolter) {
            ItemBolter bolter = (ItemBolter) stack.getItem();
            switch (bolter.type) {
                case TEMPESTUS: base = DAMAGE_TEMPESTUS; break;
                case ELITE: base = DAMAGE_ELITE; break;
                default: base = DAMAGE_NORMAL;
            }
        }
        return base + tag.getInteger(NBT_LVL_DAMAGE) * 2.0f;
    }

    public static float getExplosionRadius(ItemStack stack) {
        NBTTagCompound tag = getOrCreateNBT(stack);
        float base = RADIUS_NORMAL;
        if (stack.getItem() instanceof ItemBolter) {
            ItemBolter bolter = (ItemBolter) stack.getItem();
            switch (bolter.type) {
                case TEMPESTUS: base = RADIUS_TEMPESTUS; break;
                case ELITE: base = RADIUS_ELITE; break;
                default: base = RADIUS_NORMAL;
            }
        }
        return base + tag.getInteger(NBT_LVL_RADIUS) * 0.5f;
    }

    public static float getAreaDamage(ItemStack stack) {
        float base = AREA_DAMAGE_NORMAL;
        if (stack.getItem() instanceof ItemBolter) {
            ItemBolter bolter = (ItemBolter) stack.getItem();
            switch (bolter.type) {
                case TEMPESTUS: base = AREA_DAMAGE_TEMPESTUS; break;
                case ELITE: base = AREA_DAMAGE_ELITE; break;
                default: base = AREA_DAMAGE_NORMAL;
            }
        }
        return base;
    }

    public static int getDurabilityPerShot(ItemStack stack) {
        NBTTagCompound tag = getOrCreateNBT(stack);
        int duraLvl = tag.getInteger(NBT_LVL_DURA);
        return Math.max(1, 2 - duraLvl);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        getOrCreateNBT(stack);
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        NBTTagCompound tag = getOrCreateNBT(stack);
        tag.setBoolean(NBT_ZERO_SPREAD, false);
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        if (player.world.isRemote) return;
        NBTTagCompound tag = getOrCreateNBT(stack);
        tag.setBoolean(NBT_ZERO_SPREAD, true);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    public static void tryFire(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemBolter)) return;

        World world = player.world;
        if (world.isRemote) return;

        NBTTagCompound tag = getOrCreateNBT(stack);

        if (tag.getBoolean(NBT_RELOADING)) return;

        int cooldown = tag.getInteger(NBT_COOLDOWN);
        if (cooldown > 0) return;

        int ammo = tag.getInteger(NBT_AMMO);
        if (ammo <= 0) return;

        float spread = tag.getBoolean(NBT_ZERO_SPREAD) ? 0.0f : BASE_SPREAD;

        EntityBolterProjectile projectile = new EntityBolterProjectile(
                world,
                player,
                getBolterDamage(stack),
                getAreaDamage(stack),
                getExplosionRadius(stack),
                PROJECTILE_SPEED,
                spread
        );
        world.spawnEntity(projectile);

        tag.setInteger(NBT_AMMO, ammo - 1);
        tag.setInteger(NBT_COOLDOWN, getCooldownTicks(stack));

        stack.damageItem(getDurabilityPerShot(stack), player);

        world.playSound(null, player.posX, player.posY, player.posZ,
                net.minecraft.init.SoundEvents.ENTITY_GENERIC_EXPLODE,
                SoundCategory.PLAYERS, 0.3f, 2.0f);
    }

    public static void tryReload(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemBolter)) return;

        World world = player.world;
        if (world.isRemote) return;

        NBTTagCompound tag = getOrCreateNBT(stack);

        int currentAmmo = tag.getInteger(NBT_AMMO);
        int maxAmmo = getMaxAmmo(stack);
        if (currentAmmo >= maxAmmo || tag.getBoolean(NBT_RELOADING)) return;

        ItemStack magazine = findMagazine(player, stack);
        if (!magazine.isEmpty()) {
            int ammoToAdd = maxAmmo - currentAmmo;
            int magazineAmmo = ItemMagazine.getAmmo(magazine);
            int transfer = Math.min(ammoToAdd, magazineAmmo);

            tag.setInteger(NBT_AMMO, currentAmmo + transfer);
            ItemMagazine.setAmmo(magazine, magazineAmmo - transfer);
            if (ItemMagazine.getAmmo(magazine) <= 0) {
                magazine.shrink(1);
            }

            world.playSound(null, player.posX, player.posY, player.posZ,
                    net.minecraft.init.SoundEvents.BLOCK_PISTON_EXTEND,
                    SoundCategory.PLAYERS, 0.5f, 1.0f);
        } else {
            tag.setBoolean(NBT_RELOADING, true);
            tag.setInteger(NBT_RELOAD_TICKS, 6);
        }
    }

    private static ItemStack findMagazine(EntityPlayer player, ItemStack bolterStack) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemMagazine) {
                ItemMagazine magazine = (ItemMagazine) stack.getItem();
                if (magazine.isCompatible(bolterStack) && ItemMagazine.getAmmo(stack) > 0) {
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public static void tickReload(EntityPlayer player, ItemStack bolterStack) {
        NBTTagCompound tag = getOrCreateNBT(bolterStack);
        if (!tag.getBoolean(NBT_RELOADING)) return;

        int ticks = tag.getInteger(NBT_RELOAD_TICKS);
        if (ticks > 0) {
            tag.setInteger(NBT_RELOAD_TICKS, ticks - 1);
            return;
        }

        int current = tag.getInteger(NBT_AMMO);
        int max = getMaxAmmo(bolterStack);
        if (current >= max) {
            tag.setBoolean(NBT_RELOADING, false);
            return;
        }

        ItemStack ammo = findAmmo(player, bolterStack);
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

    private static ItemStack findAmmo(EntityPlayer player, ItemStack bolterStack) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemAmmo) {
                ItemAmmo ammo = (ItemAmmo) stack.getItem();
                if (ammo.isCompatible(bolterStack)) {
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
        NBTTagCompound tag = getOrCreateNBT(stack);
        tooltip.add("Balas: " + tag.getInteger(NBT_AMMO) + "/" + getMaxAmmo(stack));
        tooltip.add("Danho: " + getBolterDamage(stack) + " corazones");
        tooltip.add("Radio explosion: " + getExplosionRadius(stack) + " bloques");
        tooltip.add("Tipo: " + type.name());
        if (tag.getBoolean(NBT_RELOADING)) {
            tooltip.add("Recargando...");
        }
    }
}