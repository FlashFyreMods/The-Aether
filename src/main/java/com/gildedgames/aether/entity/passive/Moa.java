package com.gildedgames.aether.entity.passive;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.gildedgames.aether.client.AetherSoundEvents;
import com.gildedgames.aether.entity.WingedBird;
import com.gildedgames.aether.entity.ai.goal.FallingRandomStrollGoal;
import com.gildedgames.aether.entity.ai.navigator.FallPathNavigation;

import com.gildedgames.aether.item.miscellaneous.MoaEggItem;
import com.gildedgames.aether.item.AetherItems;
import com.gildedgames.aether.AetherTags;
import com.gildedgames.aether.api.registers.MoaType;
import com.gildedgames.aether.network.AetherPacketHandler;
import com.gildedgames.aether.network.packet.client.MoaInteractPacket;
import com.gildedgames.aether.api.AetherMoaTypes;
import com.gildedgames.aether.util.EntityUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;

public class Moa extends MountableAnimal implements WingedBird {
	private static final EntityDataAccessor<String> DATA_MOA_TYPE_ID = SynchedEntityData.defineId(Moa.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Optional<UUID>> DATA_RIDER_UUID = SynchedEntityData.defineId(Moa.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<Optional<UUID>> DATA_LAST_RIDER_UUID = SynchedEntityData.defineId(Moa.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<Integer> DATA_REMAINING_JUMPS_ID = SynchedEntityData.defineId(Moa.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> DATA_HUNGRY_ID = SynchedEntityData.defineId(Moa.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> DATA_AMOUNT_FED_ID = SynchedEntityData.defineId(Moa.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> DATA_PLAYER_GROWN_ID = SynchedEntityData.defineId(Moa.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_SITTING_ID = SynchedEntityData.defineId(Moa.class, EntityDataSerializers.BOOLEAN);

	public float wingRotation;
	public float prevWingRotation;
	public float destPos;
	public float prevDestPos;

	private int jumpCooldown;
	private int flapCooldown;

	public int eggTime = this.getEggTime();

	public Moa(EntityType<? extends Moa> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 0.65));
		this.goalSelector.addGoal(2, new TemptGoal(this, 1.0, Ingredient.of(AetherTags.Items.MOA_TEMPTATION_ITEMS), false));
		this.goalSelector.addGoal(3, new FallingRandomStrollGoal(this, 0.35));
		this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
	}

	@Nonnull
	@Override
	protected PathNavigation createNavigation(@Nonnull Level level) {
		return new FallPathNavigation(this, level);
	}

	@Nonnull
	public static AttributeSupplier.Builder createMobAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 35.0)
				.add(Attributes.MOVEMENT_SPEED, 1.0);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_MOA_TYPE_ID, "");
		this.entityData.define(DATA_RIDER_UUID, Optional.empty());
		this.entityData.define(DATA_LAST_RIDER_UUID, Optional.empty());
		this.entityData.define(DATA_REMAINING_JUMPS_ID, 0);
		this.entityData.define(DATA_HUNGRY_ID, false);
		this.entityData.define(DATA_AMOUNT_FED_ID, 0);
		this.entityData.define(DATA_PLAYER_GROWN_ID, false);
		this.entityData.define(DATA_SITTING_ID, false);
	}

	@Override
	public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor level, @Nonnull DifficultyInstance difficulty, @Nonnull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag tag) {
		if (tag != null) {
			if (tag.contains("IsBaby")) {
				this.setBaby(tag.getBoolean("IsBaby"));
			}
			if (tag.contains("MoaType")) {
				this.setMoaType(AetherMoaTypes.get(tag.getString("MoaType")));
			}
			if (tag.contains("Hungry")) {
				this.setHungry(tag.getBoolean("Hungry"));
			}
			if (tag.contains("PlayerGrown")) {
				this.setPlayerGrown(tag.getBoolean("PlayerGrown"));
			}
		} else {
			this.setMoaType(AetherMoaTypes.random(this.random));
		}
		return super.finalizeSpawn(level, difficulty, reason, spawnData, tag);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		this.animateWings();
	}

	@Override
	public void tick() {
		super.tick();
		AttributeInstance gravity = this.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
		if (gravity != null) {
			double fallSpeed = Math.max(gravity.getValue() * -1.25, -0.1);
			if (this.getDeltaMovement().y < fallSpeed && !this.playerTriedToCrouch()) {
				this.setDeltaMovement(this.getDeltaMovement().x, fallSpeed, this.getDeltaMovement().z);
				this.hasImpulse = true;
				this.setEntityOnGround(false);
			}
		}
		if (this.isOnGround()) {
			this.setRemainingJumps(this.getMaxJumps());
		}
		if (this.getJumpCooldown() > 0) {
			this.setJumpCooldown(this.getJumpCooldown() - 1);
			this.setPlayerJumped(false);
		} else if (this.getJumpCooldown() == 0) {
			this.setMountJumping(false);
		}

		if (!this.level.isClientSide() && this.isAlive()) {
			if (this.random.nextInt(900) == 0 && this.deathTime == 0) {
				this.heal(1.0F);
			}
			if (!this.isBaby() && this.getPassengers().isEmpty() && --this.eggTime <= 0) {
				this.playSound(AetherSoundEvents.ENTITY_MOA_EGG.get(), 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
				this.spawnAtLocation(this.getMoaType().getEgg());
				this.eggTime = this.getEggTime();
			}
		}

		if (this.isBaby()) {
			if (!this.isHungry()) {
				if (!this.level.isClientSide()) {
					if (this.random.nextInt(2000) == 0) {
						this.setHungry(true);
					}
				}
			} else {
				if (this.random.nextInt(10) == 0) {
					this.level.addParticle(ParticleTypes.ANGRY_VILLAGER, this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), this.getY() + 1, this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), 0.0, 0.0, 0.0);
				}
			}
		} else {
			this.setHungry(false);
			this.setAmountFed(0);
		}
	}

	@Override
	public void riderTick() {
		super.riderTick();
		if (this.getControllingPassenger() instanceof Player) {
			if (this.getFlapCooldown() > 0) {
				this.setFlapCooldown(this.getFlapCooldown() - 1);
			} else if (this.getFlapCooldown() == 0) {
				if (!this.isOnGround()) {
					this.level.playSound(null, this, AetherSoundEvents.ENTITY_MOA_FLAP.get(), SoundSource.NEUTRAL, 0.15F, Mth.clamp(this.random.nextFloat(), 0.7F, 1.0F) + Mth.clamp(this.random.nextFloat(), 0.0F, 0.3F));
					this.setFlapCooldown(15);
				}
			}
			this.resetFallDistance();
		}
	}

	@Override
	public void travel(@Nonnull Vec3 vector3d) {
		if (!this.isSitting()) {
			super.travel(vector3d);
		} else {
			if (this.isAlive()) {
				LivingEntity entity = this.getControllingPassenger();
				if (this.isVehicle() && entity != null) {
					EntityUtil.copyRotations(this, entity);
					if (this.isControlledByLocalInstance()) {
						this.travelWithInput(new Vec3(0, vector3d.y(), 0));
						this.lerpSteps = 0;
					} else {
						this.calculateEntityAnimation(this, false);
						this.setDeltaMovement(Vec3.ZERO);
					}
				} else {
					this.travelWithInput(new Vec3(0, vector3d.y(), 0));
				}
			}
		}
	}

	@Override
	public void onJump(Mob moa) {
		super.onJump(moa);
		this.setJumpCooldown(10);
		if (!this.isOnGround()) {
			this.setRemainingJumps(this.getRemainingJumps() - 1);
			this.spawnExplosionParticle();
		}
		this.setFlapCooldown(0);
	}

	@Nonnull
	@Override
	public InteractionResult mobInteract(Player playerEntity, @Nonnull InteractionHand hand) {
		ItemStack itemStack = playerEntity.getItemInHand(hand);
		if (this.isPlayerGrown() && itemStack.is(AetherItems.NATURE_STAFF.get())) {
			itemStack.hurtAndBreak(2, playerEntity, (p) -> p.broadcastBreakEvent(hand));
			this.setSitting(!this.isSitting());
			this.spawnExplosionParticle();
			return InteractionResult.sidedSuccess(this.level.isClientSide);
		} else if (!this.level.isClientSide() && this.isPlayerGrown() && this.isBaby() && this.isHungry() && this.getAmountFed() < 3 && itemStack.is(AetherTags.Items.MOA_FOOD_ITEMS)) {
			if (!playerEntity.getAbilities().instabuild) {
				itemStack.shrink(1);
			}
			this.setAmountFed(this.getAmountFed() + 1);
			if (this.getAmountFed() >= 3) {
				this.setAge(0);
			}
			this.setHungry(false);
			AetherPacketHandler.sendToAll(new MoaInteractPacket(playerEntity.getId(), hand == InteractionHand.MAIN_HAND)); // packet necessary to play animation because this code segment is server-side only, so no animations.
			return InteractionResult.CONSUME;
		} else if (this.isPlayerGrown() && !this.isBaby() && this.getHealth() < this.getMaxHealth() && itemStack.is(AetherTags.Items.MOA_FOOD_ITEMS)) {
			if (!playerEntity.getAbilities().instabuild) {
				itemStack.shrink(1);
			}
			this.heal(5.0F);
			return InteractionResult.sidedSuccess(this.level.isClientSide);
		} else {
			return super.mobInteract(playerEntity, hand);
		}
	}

	public void spawnExplosionParticle() {
		for (int i = 0; i < 20; ++i) {
			EntityUtil.spawnMovementExplosionParticles(this);
		}
	}

	public MoaType getMoaType() {
		return AetherMoaTypes.get(this.entityData.get(DATA_MOA_TYPE_ID));
	}

	public void setMoaType(MoaType moaType) {
		this.entityData.set(DATA_MOA_TYPE_ID, moaType.toString());
	}

	@Nullable
	public UUID getRider() {
		return this.entityData.get(DATA_RIDER_UUID).orElse(null);
	}

	public void setRider(@Nullable UUID uuid) {
		this.entityData.set(DATA_RIDER_UUID, Optional.ofNullable(uuid));
	}

	@Nullable
	public UUID getLastRider() {
		return this.entityData.get(DATA_LAST_RIDER_UUID).orElse(null);
	}

	public void setLastRider(@Nullable UUID uuid) {
		this.entityData.set(DATA_LAST_RIDER_UUID, Optional.ofNullable(uuid));
	}

	public int getRemainingJumps() {
		return this.entityData.get(DATA_REMAINING_JUMPS_ID);
	}

	public void setRemainingJumps(int remainingJumps) {
		this.entityData.set(DATA_REMAINING_JUMPS_ID, remainingJumps);
	}

	public boolean isHungry() {
		return this.entityData.get(DATA_HUNGRY_ID);
	}

	public void setHungry(boolean hungry) {
		this.entityData.set(DATA_HUNGRY_ID, hungry);
	}

	public int getAmountFed() {
		return this.entityData.get(DATA_AMOUNT_FED_ID);
	}

	public void setAmountFed(int amountFed) {
		this.entityData.set(DATA_AMOUNT_FED_ID, amountFed);
	}

	public boolean isPlayerGrown() {
		return this.entityData.get(DATA_PLAYER_GROWN_ID);
	}

	public void setPlayerGrown(boolean playerGrown) {
		this.entityData.set(DATA_PLAYER_GROWN_ID, playerGrown);
	}

	public boolean isSitting() {
		return this.entityData.get(DATA_SITTING_ID);
	}

	public void setSitting(boolean isSitting) {
		this.entityData.set(DATA_SITTING_ID, isSitting);
	}

	@Override
	public float getWingRotation() {
		return this.wingRotation;
	}

	@Override
	public void setWingRotation(float rot) {
		this.wingRotation = rot;
	}

	@Override
	public float getPrevWingRotation() {
		return this.prevWingRotation;
	}

	@Override
	public void setPrevWingRotation(float rot) {
		this.prevWingRotation = rot;
	}

	@Override
	public float getDestPos() {
		return this.destPos;
	}

	@Override
	public void setDestPos(float pos) {
		this.destPos = pos;
	}

	@Override
	public float getPrevDestPos() {
		return this.prevDestPos;
	}

	@Override
	public void setPrevDestPos(float pos) {
		this.prevDestPos = pos;
	}

	public int getJumpCooldown() {
		return this.jumpCooldown;
	}

	public void setJumpCooldown(int jumpCooldown) {
		this.jumpCooldown = jumpCooldown;
	}

	public int getFlapCooldown() {
		return this.flapCooldown;
	}

	public void setFlapCooldown(int flapCooldown) {
		this.flapCooldown = flapCooldown;
	}

	public int getMaxJumps() {
		return this.getMoaType().getMaxJumps();
	}

	public int getEggTime() {
		return this.random.nextInt(6000) + 6000;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return AetherSoundEvents.ENTITY_MOA_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
		return AetherSoundEvents.ENTITY_MOA_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return AetherSoundEvents.ENTITY_MOA_DEATH.get();
	}

	@Override
	protected SoundEvent getSaddledSound() {
		return AetherSoundEvents.ENTITY_MOA_SADDLE.get();
	}

	@Override
	public boolean isFood(@Nonnull ItemStack stack) {
		return false;
	}

	@Override
	public float getSpeed() {
		return this.getMoaType().getSpeed();
	}

	@Override
	public boolean canJump() {
		return this.getRemainingJumps() > 0 && this.getJumpCooldown() == 0;
	}

	@Override
	public boolean isSaddleable() {
		return super.isSaddleable() && this.isPlayerGrown();
	}

	@Override
	public double getMountJumpStrength() {
		return this.isOnGround() ? 0.9 : 0.75;
	}

	@Override
	public float getSteeringSpeed() {
		return this.getMoaType().getSpeed();
	}

	@Override
	protected int calculateFallDamage(float distance, float damageMultiplier) {
		return 0;
	}

	@Override
	public int getMaxFallDistance() {
		return this.isOnGround() ? super.getMaxFallDistance() : 14;
	}

	@Override
	public double getPassengersRidingOffset() {
		return this.isSitting() ? 0.25 : 1.25;
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(@Nonnull ServerLevel level, @Nonnull AgeableMob entity) {
		return null;
	}

	@Override
	public boolean canBreed() {
		return false;
	}

	@Override
	public ItemStack getPickResult() {
		MoaEggItem moaEggItem = MoaEggItem.byId(this.getMoaType());
		return moaEggItem == null ? null : new ItemStack(moaEggItem);
	}

	@Override
	public void readAdditionalSaveData(@Nonnull CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("IsBaby")) {
			this.setBaby(tag.getBoolean("IsBaby"));
		}
		if (tag.contains("MoaType")) {
			this.setMoaType(AetherMoaTypes.get(tag.getString("MoaType")));
		}
		if (tag.hasUUID("Rider")) {
			this.setRider(tag.getUUID("Rider"));
		}
		if (tag.hasUUID("LastRider")) {
			this.setLastRider(tag.getUUID("LastRider"));
		}
		if (tag.contains("RemainingJumps")) {
			this.setRemainingJumps(tag.getInt("RemainingJumps"));
		}
		if (tag.contains("Hungry")) {
			this.setHungry(tag.getBoolean("Hungry"));
		}
		if (tag.contains("AmountFed")) {
			this.setAmountFed(tag.getInt("AmountFed"));
		}
		if (tag.contains("PlayerGrown")) {
			this.setPlayerGrown(tag.getBoolean("PlayerGrown"));
		}
		if (tag.contains("Sitting")) {
			this.setSitting(tag.getBoolean("Sitting"));
		}
	}

	@Override
	public void addAdditionalSaveData(@Nonnull CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("IsBaby", this.isBaby());
		tag.putString("MoaType", this.getMoaType().toString());
		if (this.getRider() != null) {
			tag.putUUID("Rider", this.getRider());
		}
		if (this.getLastRider() != null) {
			tag.putUUID("LastRider", this.getLastRider());
		}
		tag.putInt("RemainingJumps", this.getRemainingJumps());
		tag.putBoolean("Hungry", this.isHungry());
		tag.putInt("AmountFed", this.getAmountFed());
		tag.putBoolean("PlayerGrown", this.isPlayerGrown());
		tag.putBoolean("Sitting", this.isSitting());
	}
}