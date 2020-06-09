package stonering.stage.entity_menu.building.workbench.details;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import stonering.enums.items.recipe.Recipe;

/**
 * @author Alexander on 19.08.2019.
 */
public class RecipeDetails extends Container {
    private Recipe recipe;

    public RecipeDetails(Recipe recipe) {
        this.recipe = recipe;
    }
}