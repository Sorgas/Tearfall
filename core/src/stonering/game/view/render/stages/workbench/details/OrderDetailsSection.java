package stonering.game.view.render.stages.workbench.details;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import stonering.entity.crafting.ItemOrder;
import stonering.enums.items.recipe.Recipe;
import stonering.enums.items.type.ItemType;
import stonering.enums.items.type.ItemTypeMap;
import stonering.game.view.render.stages.workbench.recipelist.RecipeCategoryItem;
import stonering.game.view.render.stages.workbench.recipelist.RecipeItem;
import stonering.util.global.Logger;
import stonering.util.global.StaticSkin;

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

    public OrderDetailsSection() {
        align(Align.topLeft);
        defaults().align(Align.left).expandX();
        add(itemName = new Label("", StaticSkin.getSkin())).row();
        add(itemDescription = new Label("", StaticSkin.getSkin())).expandX().row();
    }

    /**
     * Refills this pane with data of new order.
     */
    public void showOrder(ItemOrder order) {
        this.order = order;
        ItemType type = ItemTypeMap.getInstance().getItemType(order.recipe.itemName);
        itemName.setText(type.title);
        itemDescription.setText(type.description);
    }

    public void showItem(Actor actor) {
        if(actor instanceof RecipeItem) {
            ItemType type = ItemTypeMap.getInstance().getItemType(((RecipeItem) actor).recipe.itemName);
            itemName.setText(type.title);
            itemDescription.setText(type.description);
        } else if (actor instanceof RecipeCategoryItem) {
            itemName.setText("");
            itemDescription.setText("");
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
}
