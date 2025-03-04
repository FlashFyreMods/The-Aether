package com.gildedgames.aether.entity.projectile;

import com.gildedgames.aether.entity.projectile.dart.AbstractDart;
import com.gildedgames.aether.effect.AetherEffects;
import com.gildedgames.aether.entity.AetherEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class PoisonNeedle extends AbstractDart {
    public PoisonNeedle(EntityType<? extends PoisonNeedle> type, Level level) {
        super(type, level);
        this.setBaseDamage(1.0);
    }

    public PoisonNeedle(Level level, LivingEntity shooter) {
        super(AetherEntityTypes.POISON_NEEDLE.get(), shooter, level);
        this.setBaseDamage(1.0);
    }

    @Override
    protected void doPostHurtEffects(@Nonnull LivingEntity living) {
        super.doPostHurtEffects(living);
        living.addEffect(new MobEffectInstance(AetherEffects.INEBRIATION.get(), 500, 0, false, false));
    }

    @Nonnull
    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
}
