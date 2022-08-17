package zone.possum.emigration.farmersdelight.recipe;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.nhoryzon.mc.farmersdelight.FarmersDelightMod;
import com.nhoryzon.mc.farmersdelight.recipe.CookingPotRecipe;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import zone.possum.emigration.farmersdelight.FarmersDelightPlugin;

public class EmiCookingPotRecipe implements EmiRecipe {
    private static final Identifier GUI_TEXTURE = new Identifier(FarmersDelightMod.MOD_ID, "textures/gui/cooking_pot.png");

    private static final EmiTexture BACKGROUND = new EmiTexture(GUI_TEXTURE, 29, 16, 117, 57);

    private static final EmiTexture FIRE = new EmiTexture(GUI_TEXTURE, 176, 0, 17, 15);

    private final CookingPotRecipe recipe;
    private final EmiStack output;
    private final EmiIngredient container;
    private final List<EmiIngredient> ingredients;

    public EmiCookingPotRecipe(CookingPotRecipe recipe) {
        this.recipe = recipe;

        output = EmiStack.of(recipe.getOutput());

        container = EmiIngredient.of(Ingredient.ofStacks(recipe.getContainer()));

        ingredients = recipe.getIngredients().stream()
            .filter(it -> !it.isEmpty())
            .map(EmiIngredient::of)
            .toList();

        // set remainders for ingredients that have them
        for (EmiIngredient ingredient : ingredients) {
            if (ingredient.isEmpty()) continue;
            for (EmiStack stack : ingredient.getEmiStacks()) {
                Item item = stack.getItemStack().getItem();
                Item remainder = item.getRecipeRemainder();
                if (remainder != null)
                    stack.setRemainder(EmiStack.of(remainder));
            }
        }
    }

    @Override
    public void addWidgets(WidgetHolder gui) {
        gui.addTexture(BACKGROUND, 0, 0);
        gui.addTexture(FIRE, 18, 39);

        gui.addFillingArrow(60, 10, 50 * recipe.getCookTime()).tooltip((x, y) -> {
            return List.of(TooltipComponent.of(EmiPort.ordered(EmiPort.translatable("emi.cooking.time", recipe.getCookTime() / 20f))));
        });

        // if (recipe.getExperience() > 0f) {
        //     gui.addText(EmiPort.ordered(EmiPort.translatable("emi.cooking.experience", recipe.getExperience())), 44, 60, -1, true);
        // }

        gui.addSlot(output, 94, 9).drawBack(false);
        gui.addSlot(output, 94, 38).drawBack(false);

        if (!container.isEmpty()) {
            gui.addSlot(container, 62, 38)
                .drawBack(false);
        }

        final int slotSize = 18;
        for (int row = 0; row < 2; ++row) {
            for (int col = 0; col < 3; ++col) {
                final int i = row * 3 + col;
                if (i < ingredients.size()) {
                    gui.addSlot(ingredients.get(i),
                            col * slotSize,
                            row * slotSize)
                        .drawBack(false);
                }
            }
        }
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return FarmersDelightPlugin.COOKING;
    }

    @Override
    public int getDisplayHeight() {
        // return recipe.getExperience() > 0 ? 69 : 57;
        return BACKGROUND.height;
    }

    @Override
    public int getDisplayWidth() {
        return BACKGROUND.width;
    }

    @Override
    public @Nullable Identifier getId() {
        return recipe.getId();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return ingredients;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

}
