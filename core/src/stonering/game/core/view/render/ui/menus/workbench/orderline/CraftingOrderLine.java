package stonering.game.core.view.render.ui.menus.workbench.orderline;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.local.crafting.ItemOrder;
import stonering.entity.local.items.Item;
import stonering.enums.items.ItemTypeMap;
import stonering.enums.items.Recipe;
import stonering.game.core.GameMvc;
import stonering.game.core.model.lists.ItemContainer;
import stonering.game.core.view.render.ui.lists.NavigableList;
import stonering.game.core.view.render.ui.lists.NavigableSelectBox;
import stonering.game.core.view.render.ui.menus.util.HideableComponent;
import stonering.game.core.view.render.ui.menus.util.Invokable;
import stonering.game.core.view.render.ui.menus.workbench.WorkbenchMenu;
import stonering.utils.global.Pair;
import stonering.utils.global.StaticSkin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Single line in workbench menu.
 * Used to create of modify orders.
 * Item type of order cannot be changed.
 *
 * @author Alexander on 28.10.2018.
 */
public class CraftingOrderLine extends Table implements HideableComponent {
    private GameMvc gameMvc;
    private ItemContainer itemContainer;
    private WorkbenchMenu menu;

    private boolean repeatable;
    private ItemOrder order;

    private Label itemType;
    private NavigableSelectBox<String> materialselectBox;
    private Label warningLabel;

    private Label statusLabel;
    private NavigableList itemTypeList;

    /**
     * Creates line by given order.
     */
    public CraftingOrderLine(GameMvc gameMvc, WorkbenchMenu menu, ItemOrder order) {
        super();
        this.gameMvc = gameMvc;
        itemContainer = gameMvc.getModel().getItemContainer();
        this.menu = menu;
        this.order = order;
    }

    /**
     * Creates selectBoxes for crafting steps from order.
     */
    private void createOrderLine(ItemOrder order) {
        String itemTitle = ItemTypeMap.getInstance().getItemType(order.getRecipe().getItemName()).getTitle();
        itemType = new Label(itemTitle, StaticSkin.getSkin()); // label with item type
        add(itemType);

        List<Item> items = itemContainer.getResourceItemListByMaterialType(order.getRecipe().getMaterial());
        items = itemContainer.filterUnreachable(items, menu.getWorkbenchAspect().getAspectHolder().getPosition());
        if (items.size() > 0) {                             // select box for material
            Map<String, Pair<String, String>> materialsMap = itemContainer.formItemListFor(items);
            materialselectBox = new NavigableSelectBox<>();
            materialselectBox.setItems(materialsMap.keySet().toArray(new String[]{}));
            add(materialselectBox);
        } else {
            warningLabel = new Label("no items available", StaticSkin.getSkin()); // label with item type
            add(warningLabel);
        }
    }

    /**
     * Creates order status label.
     */
    private void createStatusLabel() {
        statusLabel = new Label("new", StaticSkin.getSkin());
        this.add(statusLabel).top();
    }

    @Override
    public void show() {
        menu.getOrderList().addActorAt(0, this);
        createStatusLabel();
        createOrderLine(order);
    }

    public void hide() {
        menu.getOrderList().removeActor(this);
        menu.getStage().setKeyboardFocus(menu.getOrderList().hasChildren() ? menu.getOrderList() : menu);
    }

    private int NormalizeIndex(int index) {
        if (index < 0) {
            return 0;
        }
        return index >= itemTypeList.getItems().size ? itemTypeList.getItems().size : index;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

    public ItemOrder getOrder() {
        return order;
    }


    public void setOrder(ItemOrder order) {
        this.order = order;
    }
}
