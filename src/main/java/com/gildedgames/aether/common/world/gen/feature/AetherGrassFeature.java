package com.gildedgames.aether.common.world.gen.feature;

import com.gildedgames.aether.core.AetherConfig;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.Random;

public class AetherGrassFeature extends Feature<RandomPatchConfiguration>
{
    public AetherGrassFeature(Codec<RandomPatchConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(WorldGenLevel reader, ChunkGenerator generator, Random rand, BlockPos pos, RandomPatchConfiguration config) {
        if (AetherConfig.COMMON.generate_tall_grass.get()) {
            BlockState blockstate = config.stateProvider.getState(rand, pos);
            BlockPos blockpos;
            if (config.project) {
                blockpos = reader.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos);
            } else {
                blockpos = pos;
            }

            int i = 0;
            BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();

            for (int j = 0; j < config.tries; ++j) {
                blockpos$mutable.setWithOffset(blockpos, rand.nextInt(config.xspread + 1) - rand.nextInt(config.xspread + 1), rand.nextInt(config.yspread + 1) - rand.nextInt(config.yspread + 1), rand.nextInt(config.zspread + 1) - rand.nextInt(config.zspread + 1));
                BlockPos blockpos1 = blockpos$mutable.below();
                BlockState blockstate1 = reader.getBlockState(blockpos1);
                if ((reader.isEmptyBlock(blockpos$mutable) || config.canReplace && reader.getBlockState(blockpos$mutable).getMaterial().isReplaceable()) && blockstate.canSurvive(reader, blockpos$mutable) && (config.whitelist.isEmpty() || config.whitelist.contains(blockstate1.getBlock())) && !config.blacklist.contains(blockstate1) && (!config.needWater || reader.getFluidState(blockpos1.west()).is(FluidTags.WATER) || reader.getFluidState(blockpos1.east()).is(FluidTags.WATER) || reader.getFluidState(blockpos1.north()).is(FluidTags.WATER) || reader.getFluidState(blockpos1.south()).is(FluidTags.WATER))) {
                    config.blockPlacer.place(reader, blockpos$mutable, blockstate, rand);
                    ++i;
                }
            }

            return i > 0;
        }
        
        return true;
    }
}
