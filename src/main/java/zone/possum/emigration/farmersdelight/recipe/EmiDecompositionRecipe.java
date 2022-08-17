package zone.possum.emigration.farmersdelight.recipe;

import java.util.Collection;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import org.jetbrains.annotations.Nullable;

import com.nhoryzon.mc.farmersdelight.FarmersDelightMod;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.api.widget.DrawableWidget.DrawableWidgetConsumer;
import net.minecraft.block.Block;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import zone.possum.emigration.farmersdelight.FarmersDelightPlugin;

public class EmiDecompositionRecipe implements EmiRecipe {
    private static final Identifier GUI_TEXTURE = new Identifier(FarmersDelightMod.MOD_ID, "textures/gui/rei/decomposition.png");
    private static final EmiTexture BACKGROUND = new EmiTexture(GUI_TEXTURE, 8, 9, 102, 40);

    private static final DrawableWidgetConsumer NULL_RENDERABLE = new DrawableWidgetConsumer() {
        @Override
        public void render(MatrixStack arg0, int arg1, int arg2, float arg3) { }
    };

    private static PrimitiveIterator.OfInt ids = IntStream.iterate(0, n -> n + 1).iterator();

    private final Identifier id;
    private final EmiStack enriched;
    private final EmiIngredient base;
    private final EmiIngredient accelerators;

    public EmiDecompositionRecipe(Block enriched, Block base, Collection<Block> accelerators) {
        this.id = new Identifier(String.format(
            "emi:%s/decomposition/%d",
            FarmersDelightMod.MOD_ID,
            ids.nextInt()));

        this.enriched = EmiStack.of(enriched);
        this.base = EmiIngredient.of(Ingredient.ofItems(base));
        this.accelerators = EmiIngredient.of(accelerators.stream()
            .map(Ingredient::ofItems)
            .map(EmiIngredient::of)
            .toList());
    }

    @Override
    public void addWidgets(WidgetHolder gui) {
        gui.addTexture(BACKGROUND, 0, 0);

        gui.addSlot(this.base, 0, 16)
            .drawBack(false);

        gui.addSlot(this.enriched, 84, 16)
            .drawBack(false);

        if (!accelerators.isEmpty()) {
            // gui.addTexture(SLOT, 55, 44);
            gui.addSlot(this.accelerators, 55, 44)
                .customBackground(GUI_TEXTURE, 119, 0, 18, 18)
                .catalyst(true);
        }

        gui.addDrawable(33, 30, 13, 13, NULL_RENDERABLE)
            .tooltip(tt("jei.decomposition.light"));

        gui.addDrawable(46, 30, 13, 13, NULL_RENDERABLE)
            .tooltip(tt("jei.decomposition.fluid"));

        gui.addDrawable(59, 30, 13, 13, NULL_RENDERABLE)
            .tooltip(tt("jei.decomposition.accelerators"));
    }

    private BiFunction<Integer, Integer, List<TooltipComponent>> tt(String key) {
        return (_1, _2) -> List.of(TooltipComponent.of(FarmersDelightMod.i18n(key).asOrderedText()));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return FarmersDelightPlugin.DECOMPOSITION;
    }

    @Override
    public int getDisplayHeight() {
        return accelerators.isEmpty() ? 48 : 64;
    }

    @Override
    public int getDisplayWidth() {
        return 102;
    }

    @Override
    public @Nullable Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(base);
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return List.of(accelerators);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(enriched);
    }
}
