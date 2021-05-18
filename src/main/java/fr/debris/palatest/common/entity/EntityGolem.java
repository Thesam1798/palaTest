/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 18/05/2021 : 14:08
 */
package fr.debris.palatest.common.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityGolem extends EntityMob {

    private static final double MAX_HEALTH = 20.0D;
    private static final double FOLLOW_RANGE = 40.0D;
    private static final double KNOCKBACK_RESISTANCE = 0.0D;
    private static final double MOVEMENT_SPEED = 0.26D;
    private static final double ATTACK_DAMAGE = 4.0D;

    public EntityGolem(World world) {
        super(world);
        this.setSize(1.5F / 2, 2.5F / 2);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
    }

    @Override
    protected boolean isAIEnabled() {
        return true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(MAX_HEALTH);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(FOLLOW_RANGE);
        this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(KNOCKBACK_RESISTANCE);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(MOVEMENT_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(ATTACK_DAMAGE);
    }
}
