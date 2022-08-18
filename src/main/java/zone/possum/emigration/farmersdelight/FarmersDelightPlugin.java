package zone.possum.emigration.farmersdelight;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import zone.possum.emigration.farmersdelight.recipe.EmiCookingPotRecipe;
import zone.possum.emigration.farmersdelight.recipe.EmiCuttingBoardRecipe;
import zone.possum.emigration.farmersdelight.recipe.EmiDecompositionRecipe;

import com.nhoryzon.mc.farmersdelight.FarmersDelightMod;
import com.nhoryzon.mc.farmersdelight.recipe.CookingPotRecipe;
import com.nhoryzon.mc.farmersdelight.recipe.CuttingBoardRecipe;
import com.nhoryzon.mc.farmersdelight.registry.BlocksRegistry;
import com.nhoryzon.mc.farmersdelight.registry.ItemsRegistry;
import com.nhoryzon.mc.farmersdelight.registry.RecipeTypesRegistry;
import com.nhoryzon.mc.farmersdelight.registry.TagsRegistry;

public class FarmersDelightPlugin implements EmiPlugin {
	private static final RecipeType<CookingPotRecipe> COOKING_T = RecipeTypesRegistry.COOKING_RECIPE_SERIALIZER.type();
	public static final EmiRecipeCategory COOKING = new EmiRecipeCategory(
		Registry.RECIPE_TYPE.getId(COOKING_T),
		EmiStack.of(ItemsRegistry.COOKING_POT.get()));

	private static final RecipeType<CuttingBoardRecipe> CUTTING_T = RecipeTypesRegistry.CUTTING_RECIPE_SERIALIZER.type();
	public static final EmiRecipeCategory CUTTING = new EmiRecipeCategory(
		Registry.RECIPE_TYPE.getId(CUTTING_T),
		EmiStack.of(ItemsRegistry.CUTTING_BOARD.get()));

	public static final EmiRecipeCategory DECOMPOSITION = new EmiRecipeCategory(
		new Identifier(FarmersDelightMod.MOD_ID, "decomposition"),
		EmiStack.of(ItemsRegistry.RICH_SOIL.get()));


	@Override
	public void register(EmiRegistry emi) {
		var recipes = emi.getRecipeManager();

		emi.addWorkstation(VanillaEmiRecipeCategories.CAMPFIRE_COOKING, EmiStack.of(ItemsRegistry.STOVE.get()));
		emi.addWorkstation(VanillaEmiRecipeCategories.CAMPFIRE_COOKING, EmiStack.of(ItemsRegistry.SKILLET.get()));

		emi.addCategory(FarmersDelightPlugin.COOKING);
		emi.addWorkstation(FarmersDelightPlugin.COOKING, EmiStack.of(ItemsRegistry.COOKING_POT.get()));
		recipes.listAllOfType(COOKING_T).stream()
			.parallel()
			.map(EmiCookingPotRecipe::new)
			.forEach(emi::addRecipe);

		emi.addCategory(CUTTING);
		emi.addWorkstation(CUTTING, EmiStack.of(ItemsRegistry.CUTTING_BOARD.get()));
		recipes.listAllOfType(CUTTING_T).stream()
			.parallel()
			.map(EmiCuttingBoardRecipe::new)
			.forEach(emi::addRecipe);

		emi.addCategory(DECOMPOSITION);
		// emi.addWorkstation(DECOMPOSITION, EmiStack.of(BlocksRegistry.RICH_SOIL.get()));
		emi.addRecipe(new EmiDecompositionRecipe(
			BlocksRegistry.RICH_SOIL.get(),
			BlocksRegistry.ORGANIC_COMPOST.get(),
			Registry.BLOCK.getEntryList(TagsRegistry.COMPOST_ACTIVATORS).stream()
				.parallel()
				.flatMap(RegistryEntryList::stream)
				.map(RegistryEntry::value)
				.toList()
		));
	}
}
