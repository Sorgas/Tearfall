package stonering.game.view.render.ui.menus.workbench.newmenu.recipelist;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import stonering.enums.items.recipe.Recipe;
import stonering.util.global.StaticSkin;

import java.util.List;

/**
 * Item for category of recipes in {@link RecipeList}.
 * When activated expands or collapses list of it's recipes;
 * Keys input is handled in recipe list. Clicked as normal button.
 *
 * @author Alexander on 12.08.2019.
 */
public class RecipeCategoryItem extends TextButton {
    private List<Recipe> recipes;

    public RecipeCategoryItem(String text, List<Recipe> recipes) {
        super(text, StaticSkin.getSkin());
        this.recipes = recipes;
    }
}
