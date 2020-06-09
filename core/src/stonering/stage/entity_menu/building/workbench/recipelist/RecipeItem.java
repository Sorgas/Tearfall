package stonering.stage.entity_menu.building.workbench.recipelist;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import stonering.enums.items.recipe.Recipe;
import stonering.enums.images.DrawableMap;
import stonering.widget.Highlightable;
import stonering.widget.util.WrappedTextButton;

/**
 * Item for recipe in the {@link RecipeTreeSection}.
 * Keys input is handled in recipe list. Clicked as normal button.
 *
 * @author Alexander on 12.08.2019.
 */
public class RecipeItem extends WrappedTextButton implements Highlightable {
    private static final String BACKGROUND_NAME = "recipe_category_item";
    private static final String HINT_TEXT = "WS: navigate recipes ED: add order A: to category Q: to orders";
    public final Recipe recipe;
    private RecipeTreeSection recipeTreeSection;
    private HighlightHandler highlightHandler;
    public final RecipeCategoryItem category;

    public RecipeItem(Recipe recipe, RecipeTreeSection recipeTreeSection, RecipeCategoryItem category) {
        super(recipe.title);
        this.recipe = recipe;
        this.recipeTreeSection = recipeTreeSection;
        this.category = category;
        createDefaultListener();
        createHighlightHandler();
        size(270, 35).pad(5).padLeft(25);
    }

    private void createDefaultListener() {
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                recipeTreeSection.createNewOrder(recipe);
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
                    recipeTreeSection.menu.hintLabel.setText(HINT_TEXT);
                    recipeTreeSection.menu.orderDetailsSection.showItem(owner);
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
