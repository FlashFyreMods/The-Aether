package com.gildedgames.aether;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.Item;
import net.minecraft.tags.*;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.curios.api.CuriosApi;

public class AetherTags {
	public static class Blocks {
		public static final TagKey<Block> TREATED_AS_VANILLA_BLOCK = tag("treated_as_vanilla_block");
		public static final TagKey<Block> AETHER_PORTAL_BLOCKS = tag("aether_portal_blocks");
		public static final TagKey<Block> AETHER_ISLAND_BLOCKS = tag("aether_island_blocks");
		public static final TagKey<Block> AETHER_DIRT = tag("aether_dirt");
		public static final TagKey<Block> HOLYSTONE = tag("holystone");
		public static final TagKey<Block> AERCLOUDS = tag("aerclouds");
		public static final TagKey<Block> SKYROOT_LOGS = tag("skyroot_logs");
		public static final TagKey<Block> GOLDEN_OAK_LOGS = tag("golden_oak_logs");
		public static final TagKey<Block> ALLOWED_BUCKET_PICKUP = tag("allowed_bucket_pickup");
		public static final TagKey<Block> AEROGEL = tag("aerogel");
		public static final TagKey<Block> DUNGEON_BLOCKS = tag("dungeon_blocks");
		public static final TagKey<Block> LOCKED_DUNGEON_BLOCKS = tag("locked_dungeon_blocks");
		public static final TagKey<Block> TRAPPED_DUNGEON_BLOCKS = tag("trapped_dungeon_blocks");
		public static final TagKey<Block> BOSS_DOORWAY_DUNGEON_BLOCKS = tag("boss_doorway_dungeon_blocks");
		public static final TagKey<Block> TREASURE_DOORWAY_DUNGEON_BLOCKS = tag("treasure_doorway_dungeon_blocks");
		public static final TagKey<Block> SENTRY_BLOCKS = tag("sentry_blocks");
		public static final TagKey<Block> ANGELIC_BLOCKS = tag("angelic_blocks");
		public static final TagKey<Block> HELLFIRE_BLOCKS = tag("hellfire_blocks");
		public static final TagKey<Block> SLIDER_UNBREAKABLE = tag("slider_unbreakable");
		public static final TagKey<Block> GRAVITITE_ABILITY_BLACKLIST = tag("gravitite_ability_blacklist");
		public static final TagKey<Block> AETHER_ANIMALS_SPAWNABLE_ON = tag("aether_animals_spawnable_on");
		public static final TagKey<Block> AERWHALE_SPAWNABLE_ON = tag("aerwhale_spawnable_on");
		public static final TagKey<Block> SWET_SPAWNABLE_ON = tag("swet_spawnable_on");
		public static final TagKey<Block> AECHOR_PLANT_SPAWNABLE_ON = tag("aechor_plant_spawnable_on");
		public static final TagKey<Block> ZEPHYR_SPAWNABLE_ON = tag("zephyr_spawnable_on");
		public static final TagKey<Block> COCKATRICE_SPAWNABLE_BLACKLIST = tag("cockatrice_spawnable_blacklist");
		public static final TagKey<Block> INFINIBURN = tag("infiniburn");
		public static final TagKey<Block> ALLOWED_FLAMMABLES = tag("allowed_flammables");
		public static final TagKey<Block> QUICKSOIL_CAN_GENERATE = tag("quicksoil_can_generate");
		public static final TagKey<Block> VALKYRIE_TELEPORTABLE_ON = tag("valkyrie_teleportable_on");
		public static final TagKey<Block> TREATED_AS_AETHER_BLOCK = tag("treated_as_aether_block");

		private static TagKey<Block> tag(String name) {
			return TagKey.create(Registries.BLOCK, new ResourceLocation(Aether.MODID, name));
		}
	}

	public static class Items {
		public static final TagKey<Item> AETHER_DIRT = tag("aether_dirt");
		public static final TagKey<Item> HOLYSTONE = tag("holystone");
		public static final TagKey<Item> AERCLOUDS = tag("aerclouds");
		public static final TagKey<Item> SKYROOT_LOGS = tag("skyroot_logs");
		public static final TagKey<Item> GOLDEN_OAK_LOGS = tag("golden_oak_logs");
		public static final TagKey<Item> AEROGEL = tag("aerogel");
		public static final TagKey<Item> DUNGEON_BLOCKS = tag("dungeon_blocks");
		public static final TagKey<Item> LOCKED_DUNGEON_BLOCKS = tag("locked_dungeon_blocks");
		public static final TagKey<Item> TRAPPED_DUNGEON_BLOCKS = tag("trapped_dungeon_blocks");
		public static final TagKey<Item> BOSS_DOORWAY_DUNGEON_BLOCKS = tag("boss_doorway_dungeon_blocks");
		public static final TagKey<Item> TREASURE_DOORWAY_DUNGEON_BLOCKS = tag("treasure_doorway_dungeon_blocks");
		public static final TagKey<Item> SENTRY_BLOCKS = tag("sentry_blocks");
		public static final TagKey<Item> ANGELIC_BLOCKS = tag("angelic_blocks");
		public static final TagKey<Item> HELLFIRE_BLOCKS = tag("hellfire_blocks");

		public static final TagKey<Item> CRAFTS_SKYROOT_PLANKS = tag("crafts_skyroot_planks");
		public static final TagKey<Item> PLANKS_CRAFTING = tag("planks_crafting");

		public static final TagKey<Item> AETHER_PORTAL_ACTIVATION_ITEMS = tag("aether_portal_activation_items");
		public static final TagKey<Item> BOOK_OF_LORE_MATERIALS = tag("book_of_lore_materials");
		public static final TagKey<Item> SKYROOT_STICKS = tag("skyroot_stick");
		public static final TagKey<Item> GOLDEN_AMBER_HARVESTERS = tag("golden_amber_harvesters");
		public static final TagKey<Item> TREATED_AS_AETHER_ITEM = tag("treated_as_aether_item");
		public static final TagKey<Item> NO_SKYROOT_DOUBLE_DROPS = tag("no_skyroot_double_drops");
		public static final TagKey<Item> PIG_DROPS = tag("pig_drops");
		public static final TagKey<Item> DARTS = tag("darts");
		public static final TagKey<Item> DART_SHOOTERS = tag("dart_shooters");
		public static final TagKey<Item> DEPLOYABLE_PARACHUTES = tag("deployable_parachutes");
		public static final TagKey<Item> DUNGEON_KEYS = tag("dungeon_keys");
		public static final TagKey<Item> ACCEPTED_MUSIC_DISCS = tag("accepted_music_discs");
		public static final TagKey<Item> SAVE_NBT_IN_RECIPE = tag("save_nbt_in_recipe");
		public static final TagKey<Item> MOA_EGGS = tag("moa_eggs");
		public static final TagKey<Item> FREEZABLE_BUCKETS = tag("freezable_buckets");
		public static final TagKey<Item> FREEZABLE_RINGS = tag("freezable_rings");
		public static final TagKey<Item> FREEZABLE_PENDANTS = tag("freezable_pendants");
		public static final TagKey<Item> SLIDER_DAMAGING_ITEMS = tag("slider_damaging_items");

		public static final TagKey<Item> PHYG_TEMPTATION_ITEMS = tag("phyg_temptation_items");
		public static final TagKey<Item> FLYING_COW_TEMPTATION_ITEMS = tag("flying_cow_temptation_items");
		public static final TagKey<Item> SHEEPUFF_TEMPTATION_ITEMS = tag("sheepuff_temptation_items");
		public static final TagKey<Item> AERBUNNY_TEMPTATION_ITEMS = tag("aerbunny_temptation_items");
		public static final TagKey<Item> MOA_TEMPTATION_ITEMS = tag("moa_temptation_items");
		public static final TagKey<Item> MOA_FOOD_ITEMS = tag("moa_food_items");

		public static final TagKey<Item> SKYROOT_REPAIRING = tag("skyroot_repairing");
		public static final TagKey<Item> HOLYSTONE_REPAIRING = tag("holystone_repairing");
		public static final TagKey<Item> ZANITE_REPAIRING = tag("zanite_repairing");
		public static final TagKey<Item> GRAVITITE_REPAIRING = tag("gravitite_repairing");
		public static final TagKey<Item> VALKYRIE_REPAIRING = tag("valkyrie_repairing");
		public static final TagKey<Item> FLAMING_REPAIRING = tag("flaming_repairing");
		public static final TagKey<Item> LIGHTNING_REPAIRING = tag("lightning_repairing");
		public static final TagKey<Item> HOLY_REPAIRING = tag("holy_repairing");
		public static final TagKey<Item> VAMPIRE_REPAIRING = tag("vampire_repairing");
		public static final TagKey<Item> PIG_SLAYER_REPAIRING = tag("pig_slayer_repairing");
		public static final TagKey<Item> HAMMER_OF_NOTCH_REPAIRING = tag("hammer_of_notch_repairing");
		public static final TagKey<Item> CANDY_CANE_REPAIRING = tag("candy_cane_repairing");
		public static final TagKey<Item> NEPTUNE_REPAIRING = tag("neptune_repairing");
		public static final TagKey<Item> PHOENIX_REPAIRING = tag("phoenix_repairing");
		public static final TagKey<Item> OBSIDIAN_REPAIRING = tag("obsidian_repairing");
		public static final TagKey<Item> SENTRY_REPAIRING = tag("sentry_repairing");
		public static final TagKey<Item> ICE_REPAIRING = tag("ice_repairing");

		public static final TagKey<Item> TOOLS_LANCES = tag("tools/lances");
		public static final TagKey<Item> TOOLS_HAMMERS = tag("tools/hammers");

		public static final TagKey<Item> AETHER_RING = curio("aether_ring");
		public static final TagKey<Item> AETHER_PENDANT = curio("aether_pendant");
		public static final TagKey<Item> AETHER_GLOVES = curio("aether_gloves");
		public static final TagKey<Item> AETHER_CAPE = curio("aether_cape");
		public static final TagKey<Item> AETHER_ACCESSORY = curio("aether_accessory");
		public static final TagKey<Item> AETHER_SHIELD = curio("aether_shield");

		public static final TagKey<Item> RING = curio("ring");
		public static final TagKey<Item> NECKLACE = curio("necklace");
		public static final TagKey<Item> HANDS = curio("hands");
		public static final TagKey<Item> BACK = curio("back");
		public static final TagKey<Item> CURIO = curio("curio");
		public static final TagKey<Item> CHARM = curio("charm");

		public static final TagKey<Item> ACCESSORIES = tag("accessories");

		private static TagKey<Item> tag(String name) {
			return TagKey.create(Registries.ITEM, new ResourceLocation(Aether.MODID, name));
		}

		private static TagKey<Item> curio(String name) {
			return TagKey.create(Registries.ITEM, new ResourceLocation(CuriosApi.MODID, name));
		}
	}

	public static class Entities {
		public static final TagKey<EntityType<?>> PIGS = tag("pigs");
		public static final TagKey<EntityType<?>> NO_SKYROOT_DOUBLE_DROPS = tag("no_skyroot_double_drops");
		public static final TagKey<EntityType<?>> NO_AMBROSIUM_DROPS = tag("no_ambrosium_drops");
		public static final TagKey<EntityType<?>> UNLAUNCHABLE = tag("unlaunchable");
		public static final TagKey<EntityType<?>> NO_CANDY_CANE_DROPS = tag("no_candy_cane_drops");
		public static final TagKey<EntityType<?>> DEFLECTABLE_PROJECTILES = tag("deflectable_projectiles");
		public static final TagKey<EntityType<?>> SWET_TARGETS = tag("swet_targets");
		public static final TagKey<EntityType<?>> AECHOR_PLANT_TARGETS = tag("aechor_plant_targets");
		public static final TagKey<EntityType<?>> AERCLOUD_SPAWNABLE = tag("aercloud_spawnable");

		private static TagKey<EntityType<?>> tag(String name) {
			return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Aether.MODID, name));
		}
	}

	public static class Fluids {
		public static final TagKey<Fluid> ALLOWED_BUCKET_PICKUP = tag("allowed_bucket_pickup");

		private static TagKey<Fluid> tag(String name) {
			return TagKey.create(Registries.FLUID, new ResourceLocation(Aether.MODID, name));
		}
	}

	public static class Biomes {
		public static final TagKey<Biome> IS_AETHER = tag("is_aether");
		public static final TagKey<Biome> HAS_LARGE_AERCLOUD = tag("has_large_aercloud");
		public static final TagKey<Biome> HAS_BRONZE_DUNGEON = tag("has_bronze_dungeon");
		public static final TagKey<Biome> HAS_SILVER_DUNGEON = tag("has_silver_dungeon");
		public static final TagKey<Biome> HAS_GOLD_DUNGEON = tag("has_gold_dungeon");

		public static final TagKey<Biome> MYCELIUM_CONVERSION = tag("mycelium_conversion");
		public static final TagKey<Biome> PODZOL_CONVERSION = tag("podzol_conversion");
		public static final TagKey<Biome> CRIMSON_NYLIUM_CONVERSION = tag("crimson_nylium_conversion");
		public static final TagKey<Biome> WARPED_NYLIUM_CONVERSION = tag("warped_nylium_conversion");

		public static final TagKey<Biome> ULTRACOLD = tag("ultracold");
		public static final TagKey<Biome> NO_WHEAT_SEEDS = tag("no_wheat_seeds");
		public static final TagKey<Biome> FALL_TO_OVERWORLD = tag("fall_to_overworld");
		public static final TagKey<Biome> DISPLAY_TRAVEL_TEXT = tag("display_travel_text");
		public static final TagKey<Biome> AETHER_MUSIC = tag("aether_music");

		private static TagKey<Biome> tag(String name) {
			return TagKey.create(Registries.BIOME, new ResourceLocation(Aether.MODID, name));
		}
	}

	public static class Structures {
		public static final TagKey<Structure> DUNGEONS = tag("dungeons");

		private static TagKey<Structure> tag(String name) {
			return TagKey.create(Registries.STRUCTURE, new ResourceLocation(Aether.MODID, name));
		}
	}
}