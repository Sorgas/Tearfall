package stonering.stage.workbench.recipelist;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import stonering.enums.items.recipe.Recipe;
import stonering.enums.images.DrawableMap;
import stonering.widget.Highlightable;
import stonering.widget.util.WrappedTextButton;

/**
 * Item for recipe in the {@link RecipeListSection}.
 * Keys input is handled in recipe list. Clicked as normal button.
 *
 * @author Alexander on 12.08.2019.
 */
public class RecipeItem extends WrappedTextButton implements Highlightable {
    private static final String BACKGROUND_NAME = "recipe_category_item";
    private static final String HINT_TEXT = "WS: navigate recipes ED: add order A: to category Q: to orders";
    public final Recipe recipe;
    private RecipeListSection recipeListSection;
    private HighlightHandler highlightHandler;
    public final RecipeCategoryItem category;

    public RecipeItem(Recipe recipe, RecipeListSection recipeListSection, RecipeCategoryItem category) {
        super(recipe.title);
        this.recipe = recipe;
        this.recipeListSection = recipeListSection;
        this.category = category;
        createDefaultListener();
        createHighlightHandler();
        size(270, 35).pad(5).padLeft(25);
    }

    private void createDefaultListener() {
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                recipeListSection.createNewOrder(recipe);
            }
        });
    }

    private void createHighlightHandler() {
        highlightHandler = new Highlightable.CheckHighlightHandler(this) {
            private Drawable normal = DrawableMap.REGION.getDrawable(BACKGROUND_NAME);
            private Drawable focused = DrawableMap.REGION.getDrawable(BACKGROUND_NAME + ":focused");

            @Override
            public void handle(boolean value) {
                if (value) {
                    recipeListSection.menu.hintLabel.setText(HINT_TEXT);
                    recipeListSection.menu.orderDetailsSection.showItem(owner);
                }
//                setBackground(value ? focused : normal);
                button.setColor(value ? Color.RED : Color.LIGHT_GRAY);
            }
        };
    }

    @Override
    public HighlightHandler getHighlightHandler() {
        return highlightHandler;
    }
}
