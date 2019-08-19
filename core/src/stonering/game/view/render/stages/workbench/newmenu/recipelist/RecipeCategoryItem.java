package stonering.game.view.render.stages.workbench.newmenu.recipelist;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.game.view.render.ui.images.DrawableMap;
import stonering.game.view.render.ui.menus.util.Highlightable;
import stonering.game.view.render.util.WrappedTextButton;
import stonering.util.global.StaticSkin;

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
        updateText();
        highlightHandler = new CheckHighlightHandler() {
            @Override
            public void handle() {
                setBackground(DrawableMap.getInstance().getDrawable(BACKGROUND_NAME + (value ? ":focused" : "")));
            }
        };
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                update();
            }
        });
        align(Align.left);
        size(290, 35);
        pad(5);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateHighlighting(this.equals(recipeListSection.getSelectedElement()));
    }

    /**
     * Calls list to create recipe buttons.
     */
    private void update() {
        expanded = !expanded; // toggle value
        if (expanded && recipeItems.isEmpty()) createRecipeItems(); // first creation of buttons
        recipeListSection.updateCategory(this);
        updateText();
    }

    /**
     * Changes arrow on the line end.
     */
    private void updateText() {
        button.setText(categoryName + (expanded ? " E" : " C"));
    }

    private void createRecipeItems() {
        for (String recipeName : recipeNames) {
            recipeItems.add(new RecipeItem(recipeName, recipeListSection, this));
        }
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
