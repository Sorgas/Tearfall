package stonering.game.view.render.ui.menus.workbench.newmenu.recipelist;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.items.recipe.Recipe;
import stonering.enums.items.recipe.RecipeMap;
import stonering.util.global.StaticSkin;

/**
 * Item for recipe in the {@link RecipeList}.
 * Keys input is handled in recipe list. Clicked as normal button.
 *
 * @author Alexander on 12.08.2019.
 */
public class RecipeItem extends TextButton {
    private Recipe recipe;
    private RecipeList recipeList;
    public final RecipeCategoryItem category;

    public RecipeItem(String recipe, RecipeList recipeList, RecipeCategoryItem category) {
        super(recipe, StaticSkin.getSkin());
        this.recipe = RecipeMap.instance().getRecipe(recipe);
        this.recipeList = recipeList;
        this.category = category;
        createDefaultListener();
    }

    private void createDefaultListener() {
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                recipeList.createNewOrder(recipe.name);
            }
        });
    }
}
