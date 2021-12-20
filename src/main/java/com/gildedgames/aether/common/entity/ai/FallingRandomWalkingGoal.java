package com.gildedgames.aether.common.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class FallingRandomWalkingGoal extends RandomStrollGoal
{
    protected final float probability;

    public FallingRandomWalkingGoal(PathfinderMob creatureEntity, double speed) {
        this(creatureEntity, speed, 120, 0.001F);
    }

    public FallingRandomWalkingGoal(PathfinderMob creatureEntity, double speed, int interval) {
        this(creatureEntity, speed, interval, 0.001F);
    }

    public FallingRandomWalkingGoal(PathfinderMob creatureEntity, double speed, int interval, float probability) {
        super(creatureEntity, speed, interval);
        this.probability = probability;
    }

    @Nullable
    protected Vec3 getPosition() {
        if (this.mob.isInWaterOrBubble()) {
            Vec3 vector3d = RandomPos.getLandPos(this.mob, 15, this.mob.getMaxFallDistance());
            return vector3d == null ? super.getPosition() : vector3d;
        } else if (!this.mob.isOnGround()) {
            Vec3 vector3d = RandomPos.getLandPos(this.mob, 12, this.mob.getMaxFallDistance());
            return vector3d != null ? vector3d : super.getPosition();
        } else {
            return this.mob.getRandom().nextFloat() >= this.probability ? RandomPos.getLandPos(this.mob, 10, this.mob.getMaxFallDistance()) : super.getPosition();
        }
    }
}
