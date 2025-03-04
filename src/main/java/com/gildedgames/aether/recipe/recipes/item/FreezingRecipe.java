package com.gildedgames.aether.recipe.recipes.item;

import com.gildedgames.aether.block.AetherBlocks;
import com.gildedgames.aether.recipe.AetherBookCategory;
import com.gildedgames.aether.recipe.AetherRecipeSerializers;
import com.gildedgames.aether.recipe.AetherRecipeTypes;
import com.gildedgames.aether.recipe.serializer.AetherCookingSerializer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;

public class FreezingRecipe extends AbstractAetherCookingRecipe {
	public FreezingRecipe(ResourceLocation id, String group, AetherBookCategory category, Ingredient ingredient, ItemStack result, float experience, int freezingTime) {
		super(AetherRecipeTypes.FREEZING.get(), id, group, category, ingredient, result, experience, freezingTime);
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(AetherBlocks.FREEZER.get());
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return AetherRecipeSerializers.FREEZING.get();
	}

	public static class Serializer extends AetherCookingSerializer<FreezingRecipe> {
		public Serializer() {
			super(FreezingRecipe::new, 200);
		}
	}
}
