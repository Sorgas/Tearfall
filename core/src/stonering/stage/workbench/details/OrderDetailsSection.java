package stonering.stage.workbench.details;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.crafting.IngredientOrder;
import stonering.entity.crafting.ItemOrder;
import stonering.enums.items.recipe.Recipe;
import stonering.enums.items.type.ItemType;
import stonering.enums.items.type.ItemTypeMap;
import stonering.stage.workbench.MenuSection;
import stonering.stage.workbench.WorkbenchMenu;
import stonering.stage.workbench.orderlist.OrderItem;
import stonering.stage.workbench.recipelist.RecipeItem;
import stonering.util.global.Logger;
import stonering.util.global.StaticSkin;

/**
 * This section of menu shows details of selected menu element.
 * {@link ItemOrder} can be configured from this section.
 *
 * @author Alexander on 14.08.2019.
 */
public class OrderDetailsSection extends MenuSection {
    public final WorkbenchMenu menu;
    public final WorkbenchAspect aspect;
    private ItemOrder order;
    private Label itemName;
    private Label itemDescription;
    private Image image;
    private VerticalGroup itemParts;

    public OrderDetailsSection(String title, WorkbenchAspect aspect, WorkbenchMenu menu) {
        super(title);
        this.menu = menu;
        this.aspect = aspect;
        defaults().align(Align.left).expandX();
        add(image = new Image());
        VerticalGroup group = new VerticalGroup();
        group.align(Align.topLeft).columnAlign(Align.left);
        group.addActor(itemName = new Label("", StaticSkin.getSkin()));
        group.addActor(itemDescription = new Label("", StaticSkin.getSkin()));
        add(group).row();
        add(itemParts = new VerticalGroup()).colspan(2).fillX().align(Align.left);
        itemParts.columnAlign(Align.left).align(Align.left);
    }

    public void showItem(Actor actor) {
        clearSection();
        String actorName = actor != null ? actor.getClass().getSimpleName() : "null";
        Logger.UI.logDebug("Showing " + actorName + " in details section.");
        if (actor instanceof RecipeItem) {
            showRecipeDetails((RecipeItem) actor);
        } else if (actor instanceof OrderItem) {
            showOrderDetails((OrderItem) actor);
        } else {
            Logger.UI.logWarn("Attempt to show details of invalid item " + actorName); // invalid case
        }
    }

    /**
     * Shows description of a {@link Recipe}.
     */
    private void showRecipeDetails(RecipeItem recipeItem) {
        itemName.setText(recipeItem.recipe.title);
        itemDescription.setText(recipeItem.recipe.description);
        //TODO collect available items, and display in a list, divided by item parts.
    }

    private void showOrderDetails(OrderItem orderItem) {
        order = orderItem.order;
        ItemType type = ItemTypeMap.instance().getItemType(order.recipe.itemName);
        itemName.setText(orderItem.order.recipe.title);
        itemDescription.setText(orderItem.order.recipe.description);
        for (String key : order.parts.keySet()) { // rows for parts
            itemParts.addActor(new ItemPartRow(order.parts.get(key), order.parts.keySet().size() > 1 ? key : null, this));
        }
//        itemParts.addActor(new Label("Consumed:", StaticSkin.getSkin()));
        for (IngredientOrder ingredientOrder : order.consumed) { // rows for consumed items
            itemParts.addActor(new ItemPartRow(ingredientOrder, null, this));
        }
    }

    private void clearSection() {
        order = null;
        itemName.setText("");
        itemDescription.setText("");
        image.setDrawable(null);
        itemParts.clear();
    }
}
