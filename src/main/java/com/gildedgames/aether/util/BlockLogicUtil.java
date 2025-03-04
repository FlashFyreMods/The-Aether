package com.gildedgames.aether.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class BlockLogicUtil {
    public static boolean doesAirExistNearby(BlockPos center, int radius, WorldGenLevel level) {
        return level.isEmptyBlock(center.north(radius))
                || level.isEmptyBlock(center.south(radius))
                || level.isEmptyBlock(center.west(radius))
                || level.isEmptyBlock(center.east(radius));
    }

    /**
     * Find the entry point for a tunnel piece
     * @param box - The room to tunnel from
     * @param direction - The direction to tunnel in
     * @param width - The width of the tunnel to build
     */
    public static BlockPos tunnelFromEvenSquareRoom(BoundingBox box, Direction direction, int width) {
        int offsetFromCenter = (((direction.getAxis() == Direction.Axis.X ? box.getZSpan() : box.getXSpan()) + 1) >> 1);
        int sidedOffset = (width >> 1);

        int xOffset = direction.getStepX() * offsetFromCenter - direction.getStepZ() * sidedOffset - Math.max(0, direction.getStepX()) + Math.min(0, direction.getStepZ());
        int zOffset = direction.getStepZ() * offsetFromCenter + direction.getStepX() * sidedOffset - Math.max(0, direction.getStepZ()) - Math.max(0, direction.getStepX());

        return box.getCenter().offset(
                xOffset,
                -(box.getYSpan() >> 1),
                zOffset
        );
    }
}
