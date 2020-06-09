package stonering.stage.entity_menu.building.workbench.recipelist;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.enums.items.recipe.Recipe;
import stonering.enums.items.recipe.RecipeMap;
import stonering.widget.util.WrappedTextButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Item for category of recipes in {@link RecipeTreeSection}.
 * When activated expands or collapses list of it's recipes;
 * Keys input is handled in recipe list. Clicked as normal button.
 *
 * @author Alexander on 12.08.2019.
 */
public class RecipeCategoryItem extends WrappedTextButton {
    private static final String EXPANDED_HINT_TEXT = "WS: navigate recipes ED: to recipes A: collapse Q: to orders";
    private static final String COLLAPSED_HINT_TEXT = "WS: navigate recipes ED: expand Q: to orders";
    private static final String BACKGROUND_NAME = "recipe_category_item";
    public final String categoryName;
    private RecipeTreeSection recipeTreeSection;
    private List<RecipeItem> recipeItems;
    private List<String> recipeNames;
    private boolean expanded = false;

    public RecipeCategoryItem(String categoryName, RecipeTreeSection recipeTreeSection, List<String> recipeNames) {
        super(categoryName);
        this.categoryName = categoryName;
        this.recipeTreeSection = recipeTreeSection;
        this.recipeNames = recipeNames;
        recipeItems = new ArrayList<>();
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                update(!expanded); // toggle state
            }
        });
        align(Align.top);
        size(300, 35);
        padBottom(5);
    }

    /**
     * Expands/collapses this category in {@link RecipeTreeSection}.
     */
    public void update(boolean toValue) {
        expanded = toValue;
        if (expanded && recipeItems.isEmpty()) createRecipeItems(); // lazy creation of buttons
    }

    private void createRecipeItems() {
        Recipe recipe;
        RecipeMap map = RecipeMap.instance();
        for (String recipeName : recipeNames) {
            if ((recipe = map.getRecipe(recipeName)) == null) continue;
            recipeItems.add(new RecipeItem(recipe, recipeTreeSection, this));
        }
    }

    public boolean isExpanded() {
        return expanded;
    }

    public List<RecipeItem> getRecipeItems() {
        return recipeItems;
    }
}
