package stonering.game.view.render.stages.workbench.details;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.crafting.ItemOrder;
import stonering.enums.items.recipe.Recipe;
import stonering.game.view.render.stages.workbench.recipelist.RecipeCategoryItem;
import stonering.game.view.render.stages.workbench.recipelist.RecipeItem;
import stonering.util.global.Logger;

/**
 * Table for displaying detailed information about {@link ItemOrder}.
 * Order can be configured from this section.
 *
 * @author Alexander on 14.08.2019.
 */
public class OrderDetailsSection extends Table {
    private ItemOrder order;
    private Label itemName;
    private Label itemDescription;

    /**
     * Refills this pane with data of new order.
     */
    public void showOrder() {
        //TODO
    }

    public void showItem(Actor actor) {
        if(actor instanceof RecipeItem) {

        } else if (actor instanceof RecipeCategoryItem) {

        } else {
            Logger.UI.logError("Attempt to show details of invalid item " + actor.toString()); // invalid case
        }
    }

    /**
     * Shows description of a {@link Recipe}.
     */
    private void showRecipeDetails() {

    }

    /**
     * Shows recipe category, and its description.
     */
    public void showRecipeCategory() {

    }

    private void createTable() {

    }

    private void createListeners() {

    }
}
