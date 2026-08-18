package com.wh40kbolter.boltermod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class EntityBolterProjectile extends Entity {

    private EntityLivingBase shooter;
    private float directDamage = 8.0f;
    private float areaDamage = 4.0f;
    private float explosionRadius = 2.0f;
    private float speed = 15.0f;
    private float spread = 0.0f;
    private boolean impacted = false;

    public EntityBolterProjectile(World world) {
        super(world);
        this.setSize(0.1F, 0.1F);
        this.setNoGravity(true);
    }

    public EntityBolterProjectile(World world, EntityLivingBase shooter,
                                  float directDamage, float areaDamage,
                                  float explosionRadius, float speed, float spread) {
        this(world);
        this.shooter = shooter;
        this.directDamage = directDamage;
        this.areaDamage = areaDamage;
        this.explosionRadius = explosionRadius;
        this.speed = speed;
        this.spread = spread;

        this.setPosition(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ);

        Vec3d dir;
        if (spread <= 0.0f) {
            dir = getVectorFromRotation(shooter.rotationPitch, shooter.getRotationYawHead());
        } else {
            float randomPitch = (this.rand.nextFloat() - 0.5f) * spread;
            float randomYaw = (this.rand.nextFloat() - 0.5f) * spread;
            dir = getVectorFromRotation(shooter.rotationPitch + randomPitch, shooter.getRotationYawHead() + randomYaw);
        }

        this.motionX = dir.x * speed;
        this.motionY = dir.y * speed;
        this.motionZ = dir.z * speed;

        this.updateHeading();
    }

    private Vec3d getVectorFromRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3d(f1 * f2, f3, f * f2);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.ticksExisted > 100) {
            this.setDead();
            return;
        }

        Vec3d start = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d end = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (!this.world.isRemote) {
            RayTraceResult hit = traceImpact(start, end);
            if (hit != null) {
                onImpact(hit);
            }
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;

        this.updateHeading();
        this.setPosition(this.posX, this.posY, this.posZ);

        if (this.world.isRemote && this.ticksExisted % 2 == 0) {
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0);
        }
    }

    private RayTraceResult traceImpact(Vec3d start, Vec3d end) {
        RayTraceResult blockHit = this.world.rayTraceBlocks(start, end, false, true, false);
        if (blockHit != null) {
            end = blockHit.hitVec;
        }

        Entity hitEntity = null;
        Vec3d hitVec = null;
        double closest = 0.0D;

        AxisAlignedBB searchBox = this.getEntityBoundingBox()
                .expand(this.motionX, this.motionY, this.motionZ)
                .grow(1.0D);

        List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, searchBox);

        for (Entity entity : entities) {
            if (entity.canBeCollidedWith() && (this.ticksExisted > 1 || entity != this.shooter)) {
                AxisAlignedBB bb = entity.getEntityBoundingBox().grow(0.3D);
                RayTraceResult intercept = bb.calculateIntercept(start, end);
                if (intercept != null) {
                    double dist = start.squareDistanceTo(intercept.hitVec);
                    if (dist < closest || closest == 0.0D) {
                        hitEntity = entity;
                        hitVec = intercept.hitVec;
                        closest = dist;
                    }
                }
            }
        }

        if (hitEntity != null) {
            return new RayTraceResult(hitEntity, hitVec);
        }

        return blockHit;
    }

    protected void onImpact(RayTraceResult result) {
        if (this.world.isRemote || this.impacted) return;
        this.impacted = true;

        if (result.entityHit != null) {
            result.entityHit.attackEntityFrom(
                    DamageSource.causeThrownDamage(this, this.shooter),
                    this.directDamage
            );
        }

        applyAreaDamage();

        this.world.newExplosion(
                this.shooter,
                this.posX, this.posY, this.posZ,
                this.explosionRadius,
                false,
                false
        );

        this.setDead();
    }

    private void applyAreaDamage() {
        AxisAlignedBB area = new AxisAlignedBB(
                this.posX - this.explosionRadius,
                this.posY - this.explosionRadius,
                this.posZ - this.explosionRadius,
                this.posX + this.explosionRadius,
                this.posY + this.explosionRadius,
                this.posZ + this.explosionRadius
        );

        List<EntityLivingBase> entities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, area);
        for (EntityLivingBase entity : entities) {
            if (entity == this.shooter) continue;
            double dist = entity.getDistance(this.posX, this.posY, this.posZ);
            if (dist <= this.explosionRadius) {
                float factor = 1.0f - (float) (dist / this.explosionRadius);
                float damage = this.areaDamage * factor;
                if (damage > 0) {
                    entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shooter), damage);
                }
            }
        }
    }

    public void updateHeading() {
        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
        this.rotationPitch = (float) (MathHelper.atan2(this.motionY, f) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    @Nullable
    public EntityLivingBase getShooter() {
        return this.shooter;
    }

    @Override
    protected void entityInit() {}

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.directDamage = compound.getFloat("DirectDamage");
        this.areaDamage = compound.getFloat("AreaDamage");
        this.explosionRadius = compound.getFloat("Radius");
        this.speed = compound.getFloat("Speed");
        this.spread = compound.getFloat("Spread");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setFloat("DirectDamage", this.directDamage);
        compound.setFloat("AreaDamage", this.areaDamage);
        compound.setFloat("Radius", this.explosionRadius);
        compound.setFloat("Speed", this.speed);
        compound.setFloat("Spread", this.spread);
    }
}