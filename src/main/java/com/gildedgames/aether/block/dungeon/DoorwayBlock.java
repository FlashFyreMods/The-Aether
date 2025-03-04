package com.gildedgames.aether.block.dungeon;

import com.gildedgames.aether.client.particle.AetherParticleTypes;
import com.gildedgames.aether.entity.ai.AetherBlockPathTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DoorwayBlock extends Block {
    public static final BooleanProperty INVISIBLE = BooleanProperty.create("invisible");
    public static final VoxelShape INVISIBLE_SHAPE = Block.box(5.0, 5.0, 5.0, 11.0, 11.0, 11.0);

    public DoorwayBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(INVISIBLE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(INVISIBLE);
    }

    /**
     * Toggles the block between invisible and not invisible if a creative player interacts with it.<br><br>
     * Warning for "deprecation" is suppressed because the method is fine to override.
     * @param state The {@link BlockState} of the block.
     * @param level The {@link Level} the block is in.
     * @param pos The {@link BlockPos} of the block.
     * @param player The {@link Player} interacting with the block.
     * @param hand The {@link InteractionHand} the player interacts with.
     * @param hit The {@link BlockHitResult} of the interaction.
     * @return The {@link InteractionResult} of the interaction.
     */
    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isCreative()) {
            BlockState newState = state.cycle(INVISIBLE);
            level.setBlock(pos, newState, 1 | 2);
            return InteractionResult.SUCCESS;
        } else {
            return super.use(state, level, pos, player, hand, hit);
        }
    }

    /**
     * Spawns smoke particles when a player attempts to place anything inside the block.<br><br>
     * Warning for "deprecation" is suppressed because the method is fine to override.
     * @param state The {@link BlockState} of the block.
     * @param context The {@link BlockPlaceContext} of the replacement attempt.
     * @return Whether the block can be replaced, as a {@link Boolean}.
     */
    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        boolean flag = super.canBeReplaced(state, context);
        if (!flag) {
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();
            for (int i = 0; i < 2; i++) {
                double a = pos.getX() + 0.5 + (double) (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.375;
                double b = pos.getY() + 0.5 + (double) (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.375;
                double c = pos.getZ() + 0.5 + (double) (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.375;
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.POOF, a, b, c, 1, 0.0, 0.0, 0.0, 0.0);
                }
            }
        }
        return flag;
    }

    /**
     * Based on {@link ClientLevel#getMarkerParticleTarget()} and {@link ClientLevel#doAnimateTick(int, int, int, int, RandomSource, Block, BlockPos.MutableBlockPos)}.
     * Similar to barrier blocks, this renders the boss doorway block overlay icon at a doorway block's position while it's invisible if the block is held by the player.
     * @param state The {@link BlockState} of the block.
     * @param level The {@link Level} the block is in.
     * @param pos The {@link BlockPos} of the block.
     * @param random The {@link RandomSource} of the level.
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.gameMode != null && minecraft.gameMode.getPlayerMode() == GameType.CREATIVE && minecraft.player != null && minecraft.level != null) {
            ItemStack itemStack = minecraft.player.getMainHandItem();
            Item item = itemStack.getItem();
            if (item instanceof BlockItem blockItem) {
                if (blockItem.getBlock() == this && state.getValue(INVISIBLE)) {
                    minecraft.level.addParticle(AetherParticleTypes.BOSS_DOORWAY_BLOCK.get(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    /**
     * Creates a small hitbox for doorway blocks when invisible and hovered over by creative players.<br><br>
     * Warning for "deprecation" is suppressed because the method is fine to override.
     * @param state The {@link BlockState} of the block.
     * @param level The {@link Level} the block is in.
     * @param pos The {@link BlockPos} of the block.
     * @param context The {@link CollisionContext} of the entity with the block.
     * @return The {@link VoxelShape} of the block.
     */
    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(INVISIBLE)) {
            if (context instanceof EntityCollisionContext entity && entity.getEntity() instanceof Player player && player.isCreative()) {
                return INVISIBLE_SHAPE;
            }
            return Shapes.empty();
        }
        return super.getShape(state, level, pos, context);
    }

    /**
     * Warning for "deprecation" is suppressed because the method is fine to override.
     */
    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(INVISIBLE) ? Shapes.empty() : super.getCollisionShape(state, level, pos, context);
    }

    /**
     * Warning for "deprecation" is suppressed because the method is fine to override.
     */
    @SuppressWarnings("deprecation")
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return state.getValue(INVISIBLE) ? RenderShape.INVISIBLE : super.getRenderShape(state);
    }

    /**
     * Gets the {@link BlockPathTypes} corresponding to this block for mob navigation checks.
     * @param state The {@link BlockState} of the block.
     * @param level The {@link Level} the block is in.
     * @param pos The {@link BlockPos} of the block.
     * @param mob The {@link Mob} trying to pathfind in respect to this block.
     * @return The {@link BlockPathTypes} corresponding to this block.
     */
    @Nullable
    @Override
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        return AetherBlockPathTypes.BOSS_DOORWAY;
    }
}
