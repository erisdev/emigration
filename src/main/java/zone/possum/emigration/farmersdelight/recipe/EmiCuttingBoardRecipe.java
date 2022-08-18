package zone.possum.emigration.farmersdelight.recipe;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.nhoryzon.mc.farmersdelight.FarmersDelightMod;
import com.nhoryzon.mc.farmersdelight.recipe.CuttingBoardRecipe;
// import com.nhoryzon.mc.farmersdelight.registry.ItemsRegistry;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;
import zone.possum.emigration.farmersdelight.FarmersDelightPlugin;

public class EmiCuttingBoardRecipe implements EmiRecipe {
    private static final Identifier GUI_TEXTURE = new Identifier(FarmersDelightMod.MOD_ID, "textures/gui/rei/cutting_board.png");
    private static final EmiTexture BACKGROUND = new EmiTexture(GUI_TEXTURE, 0, 0, 117, 57);

    private final Identifier id;
    private final List<EmiStack> outputs;
    private final EmiIngredient tool;
    private final EmiIngredient ingredient;

    public EmiCuttingBoardRecipe(CuttingBoardRecipe recipe) {
        id = recipe.getId();

        outputs = recipe.getResultList().stream()
            .map(EmiStack::of)
            .toList();

        tool = EmiIngredient.of(recipe.getTool());
        ingredient = EmiIngredient.of(recipe.getIngredients().get(0));

        // apply damage to tools
        // for (EmiStack stack : tool.getEmiStacks()) {
        //     Item item = stack.getItemStack().getItem();
        //     if (item.isDamageable()) {
        //         ItemStack damaged = stack.getItemStack().copy();
        //         damaged.setDamage(1);
        //         stack.setRemainder(EmiStack.of(damaged));
        //     }
        // }
    }

    @Override
    public void addWidgets(WidgetHolder gui) {
        gui.addTexture(BACKGROUND, 0, 0);

        // TODO figure out how to draw the damn cutting board

        gui.addSlot(tool, 15, 7)
            .drawBack(false)
            .catalyst(true);

        gui.addSlot(ingredient, 15, 26)
            .drawBack(false);

        final int slotSize = 18;
        for (int row = 0; row < 2; ++row) {
            for (int col = 0; col < 2; ++col) {
                final int i = row * 2 + col;
                EmiStack stack = (
                    i < outputs.size()
                        ? outputs.get(i)
                        : EmiStack.EMPTY);
                gui.addSlot(stack,
                    77 + col * slotSize,
                    11 + row * slotSize);
            }
        }
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return FarmersDelightPlugin.CUTTING;
    }

    @Override
    public int getDisplayHeight() {
        return BACKGROUND.height;
    }

    @Override
    public int getDisplayWidth() {
        return BACKGROUND.width;
    }

    @Override
    public @Nullable Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(ingredient);
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        // var cuttingBoard = EmiStack.of(ItemsRegistry.CUTTING_BOARD.get());
        // cuttingBoard.setRemainder(cuttingBoard);
        return List.of(tool);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }
}
