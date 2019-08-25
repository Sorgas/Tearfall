package stonering.game.view.render.stages.workbench.recipelist;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import stonering.enums.items.recipe.Recipe;
import stonering.game.view.render.ui.images.DrawableMap;
import stonering.game.view.render.ui.menus.util.Highlightable;
import stonering.game.view.render.util.WrappedTextButton;

/**
 * Item for recipe in the {@link RecipeListSection}.
 * Keys input is handled in recipe list. Clicked as normal button.
 *
 * @author Alexander on 12.08.2019.
 */
public class RecipeItem extends WrappedTextButton implements Highlightable {
    private static final String BACKGROUND_NAME = "recipe_category_item";
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

    @Override
    public void act(float delta) {
        super.act(delta);
        updateHighlighting(this.equals(recipeListSection.getSelectedElement()));
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
        highlightHandler = new Highlightable.CheckHighlightHandler() {
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
}
