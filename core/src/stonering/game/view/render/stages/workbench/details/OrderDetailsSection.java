package stonering.game.view.render.stages.workbench.details;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import stonering.entity.crafting.ItemOrder;
import stonering.enums.items.recipe.Recipe;
import stonering.enums.items.type.ItemType;
import stonering.enums.items.type.ItemTypeMap;
import stonering.game.view.render.stages.workbench.orderlist.OrderItem;
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
    private Image image;

    public OrderDetailsSection() {
        align(Align.topLeft);
        defaults().align(Align.left).expandX();
        add(image = new Image());
        VerticalGroup group = new VerticalGroup();
        group.align(Align.top);
        group.addActor(itemName = new Label("", StaticSkin.getSkin()));
        group.addActor(itemDescription = new Label("", StaticSkin.getSkin()));
    }

    public void showItem(Actor actor) {
        clearSection();
        String actorName = actor != null ? actor.getName() : "null";
        Logger.UI.logDebug("Showing " + actorName + " in details section.");
        if (actor instanceof RecipeItem) {
            showRecipeDetails((RecipeItem) actor);
        } else if(actor instanceof OrderItem) {
            showOrderDetails((OrderItem) actor);
        } else {
            Logger.UI.logWarn("Attempt to show details of invalid item " + actorName); // invalid case
        }
    }

    /**
     * Shows description of a {@link Recipe}.
     */
    private void showRecipeDetails(RecipeItem recipeItem) {
        ItemType type = ItemTypeMap.getInstance().getItemType(recipeItem.recipe.itemName);
        itemName.setText(type.title);
        itemDescription.setText(type.description);
    }

    private void showOrderDetails(OrderItem orderItem) {
        //TODO
        order = orderItem.order;
        ItemType type = ItemTypeMap.getInstance().getItemType(order.recipe.itemName);
        itemName.setText(type.title);
    }

    private void clearSection() {
        order = null;
        itemName.setText("");
        itemDescription.setText("");
        image.setDrawable(null);
    }
}
