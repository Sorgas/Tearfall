package stonering.game.view.render.stages.workbench.newmenu.recipelist;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.items.recipe.Recipe;
import stonering.enums.items.recipe.RecipeMap;
import stonering.game.view.render.util.WrappedTextButton;

/**
 * Item for recipe in the {@link RecipeListSection}.
 * Keys input is handled in recipe list. Clicked as normal button.
 *
 * @author Alexander on 12.08.2019.
 */
public class RecipeItem extends WrappedTextButton {
    private Recipe recipe;
    private RecipeListSection recipeListSection;
    public final RecipeCategoryItem category;

    public RecipeItem(String recipeName, RecipeListSection recipeListSection, RecipeCategoryItem category) {
        super(recipeName);
        this.recipe = RecipeMap.instance().getRecipe(recipeName);
        this.recipeListSection = recipeListSection;
        this.category = category;
        createDefaultListener();
        size(185, 25).padLeft(15);
    }

    private void createDefaultListener() {
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                recipeListSection.createNewOrder(recipe.name);
            }
        });
    }
}
