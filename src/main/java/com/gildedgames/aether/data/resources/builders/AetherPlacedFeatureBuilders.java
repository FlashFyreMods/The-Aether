package com.gildedgames.aether.data.resources.builders;

import com.gildedgames.aether.world.placementmodifier.ConfigFilter;
import com.gildedgames.aether.world.placementmodifier.DungeonBlacklistFilter;
import com.gildedgames.aether.world.placementmodifier.ImprovedLayerPlacementModifier;
import com.gildedgames.aether.AetherConfig;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class AetherPlacedFeatureBuilders {
    public static List<PlacementModifier> aercloudPlacement(int height, int chance) {
        return List.of(
                HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(height)),
                InSquarePlacement.spread(),
                RarityFilter.onAverageOnceEvery(chance),
                BiomeFilter.biome(),
                new DungeonBlacklistFilter());
    }

    public static List<PlacementModifier> pinkAercloudPlacement(int height, int chance) {
        return List.of(
                HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(height)),
                InSquarePlacement.spread(),
                RarityFilter.onAverageOnceEvery(chance),
                BiomeFilter.biome(),
                new DungeonBlacklistFilter(),
                new ConfigFilter(AetherConfig.COMMON.generate_pink_aerclouds));
    }

    /**
     * Copy of {@link net.minecraft.data.worldgen.placement.VegetationPlacements#treePlacement(PlacementModifier)}
     */
    public static List<PlacementModifier> treePlacement(PlacementModifier count) {
        return treePlacementBase(count).build();
    }

    /**
     * Based on {@link net.minecraft.data.worldgen.placement.VegetationPlacements#treePlacementBase(PlacementModifier)}
     */
    private static ImmutableList.Builder<PlacementModifier> treePlacementBase(PlacementModifier count) {
        return ImmutableList.<PlacementModifier>builder()
                .add(count)
                .add(ImprovedLayerPlacementModifier.of(Heightmap.Types.OCEAN_FLOOR, UniformInt.of(0, 1), 4))
                .add(SurfaceWaterDepthFilter.forMaxDepth(0))
                .add(BiomeFilter.biome())
                .add(new DungeonBlacklistFilter());
    }

    /**
     * Copy of {@link net.minecraft.data.worldgen.placement.OrePlacements#commonOrePlacement(int, PlacementModifier)}.
     */
    public static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightRange) {
        return orePlacement(CountPlacement.of(count), heightRange);
    }

    /**
     * Copy of {@link net.minecraft.data.worldgen.placement.OrePlacements#orePlacement(PlacementModifier, PlacementModifier)}.
     */
    private static List<PlacementModifier> orePlacement(PlacementModifier count, PlacementModifier heightRange) {
        return List.of(count, InSquarePlacement.spread(), heightRange, BiomeFilter.biome());
    }
}
