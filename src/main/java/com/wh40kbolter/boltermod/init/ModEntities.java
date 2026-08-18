package com.wh40kbolter.boltermod.init;

import com.wh40kbolter.boltermod.BolterMod;
import com.wh40kbolter.boltermod.entity.EntityBolterProjectile;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {

    private static int entityId = 0;

    public static void register() {
        EntityRegistry.registerModEntity(
                new ResourceLocation(BolterMod.MODID, "bolter_projectile"),
                EntityBolterProjectile.class,
                "bolter_projectile",
                entityId++,
                BolterMod.instance,
                64,
                5,
                true
        );
    }
}