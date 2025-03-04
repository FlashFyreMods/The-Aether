package com.gildedgames.aether.item.tools.holystone;

import com.gildedgames.aether.item.combat.AetherItemTiers;
import com.gildedgames.aether.item.tools.abilities.HolystoneTool;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraftforge.event.level.BlockEvent;

/**
 * Ambrosium dropping behavior is called by {@link com.gildedgames.aether.event.listeners.abilities.ToolAbilityListener#doHolystoneAbility(BlockEvent.BreakEvent)}.
 */
public class HolystoneShovelItem extends ShovelItem implements HolystoneTool {
    public HolystoneShovelItem() {
        super(AetherItemTiers.HOLYSTONE, 1.5F, -3.0F, new Item.Properties());
    }
}
