package stonering.game.view.render.stages.workbench.recipelist;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import stonering.enums.items.recipe.Recipe;
import stonering.enums.items.recipe.RecipeMap;
import stonering.game.view.render.ui.images.DrawableMap;
import stonering.game.view.render.ui.menus.util.Highlightable;
import stonering.game.view.render.util.WrappedTextButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Item for category of recipes in {@link RecipeListSection}.
 * When activated expands or collapses list of it's recipes;
 * Keys input is handled in recipe list. Clicked as normal button.
 *
 * @author Alexander on 12.08.2019.
 */
public class RecipeCategoryItem extends WrappedTextButton implements Highlightable {
    private static final String BACKGROUND_NAME = "recipe_category_item";
    public final String categoryName;
    private RecipeListSection recipeListSection;
    private HighlightHandler highlightHandler;
    private List<RecipeItem> recipeItems;
    private List<String> recipeNames;
    private boolean expanded = false;

    public RecipeCategoryItem(String categoryName, RecipeListSection recipeListSection, List<String> recipeNames) {
        super(categoryName);
        this.categoryName = categoryName;
        this.recipeListSection = recipeListSection;
        this.recipeNames = recipeNames;
        recipeItems = new ArrayList<>();
        createHighlightHandler();
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

    @Override
    public void act(float delta) {
        super.act(delta);
        updateHighlighting(this.equals(recipeListSection.getSelectedElement()));
    }

    /**
     * Expands/collapses this category in {@link RecipeListSection}.
     */
    public void update(boolean toValue) {
        expanded = toValue;
        if (expanded && recipeItems.isEmpty()) createRecipeItems(); // lazy creation of buttons
        recipeListSection.updateCategory(this);
    }

    private void createRecipeItems() {
        Recipe recipe;
        RecipeMap map = RecipeMap.instance();
        for (String recipeName : recipeNames) {
            if ((recipe = map.getRecipe(recipeName)) == null) continue;
            recipeItems.add(new RecipeItem(recipe, recipeListSection, this));
        }
    }

    private void createHighlightHandler() {
        highlightHandler = new CheckHighlightHandler() {
            private Drawable normal = DrawableMap.instance().getDrawable(BACKGROUND_NAME);
            private Drawable focused = DrawableMap.instance().getDrawable(BACKGROUND_NAME + ":focused");

            @Override
            public void handle() {
//                setBackground(value ? focused : normal);
                button.setColor(value ? Color.RED : Color.LIGHT_GRAY);
            }
        };
    }

    @Override
    public HighlightHandler getHighlightHandler() {
        return highlightHandler;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public List<RecipeItem> getRecipeItems() {
        return recipeItems;
    }
}
