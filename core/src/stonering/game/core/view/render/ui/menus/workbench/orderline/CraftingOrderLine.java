package stonering.game.core.view.render.ui.menus.workbench.orderline;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.local.crafting.ItemOrder;
import stonering.enums.items.ItemTypeMap;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.lists.NavigableSelectBox;
import stonering.game.core.view.render.ui.menus.util.HideableComponent;
import stonering.game.core.view.render.ui.menus.workbench.WorkbenchMenu;
import stonering.global.utils.Position;
import stonering.utils.global.StaticSkin;

/**
 * Single line in workbench menu.
 * Used to review, modify and cancel orders.
 * Item type of order cannot be changed.
 *
 * @author Alexander on 28.10.2018.
 */
public class CraftingOrderLine extends Table implements HideableComponent {
    private GameMvc gameMvc;
    private WorkbenchMenu menu;

    private boolean repeatable;
    private ItemOrder order;

    private Label statusLabel;                                  // shows status, updates on status change
    private Label itemType;                                     // shows selected item, static
    private NavigableSelectBox<String> materialselectBox;       // allows to select material for crafting
    private Label warningLabel;                                 // shown when something is not ok

    /**
     * Creates line by given order.
     */
    public CraftingOrderLine(GameMvc gameMvc, WorkbenchMenu menu, ItemOrder order) {
        super();
        this.gameMvc = gameMvc;
        this.menu = menu;
        this.order = order;

    }

    /**
     * Creates order status label.
     */
    private void createStatusLabel() {
        statusLabel = new Label("new", StaticSkin.getSkin());
        this.add(statusLabel).top();
    }

    /**
     * Creates label with item title.
     */
    private void createItemLabel() {
        String itemTitle = ItemTypeMap.getInstance().getItemType(order.getRecipe().getItemName()).getTitle();
        itemType = new Label(itemTitle, StaticSkin.getSkin()); // label with item type
        add(itemType);
    }

    /**
     * Creates selectBox for order.
     * SelectBox can have no items after this.
     */
    private void createMaterialSelectBox() {
        materialselectBox = new NavigableSelectBox<>();
        add(materialselectBox);
        Position workbenchPosition = menu.getWorkbenchAspect().getAspectHolder().getPosition();
        materialselectBox.setItems(order.getAvailableItemList(workbenchPosition).toArray(new String[]{}));
        materialselectBox.setSelectListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                materialselectBox.getSelected();
                return super.keyDown(event, keycode);
            }
        });
    }

    /**
     * Creates label for showing messages.
     */
    private void createWarningLabel() {
        warningLabel = new Label("", StaticSkin.getSkin());                               // label with item type
        add(warningLabel);
    }

    @Override
    public void show() {
        createStatusLabel();
        createItemLabel();
        createMaterialSelectBox();
        createWarningLabel();
        menu.getOrderList().addActorAt(0, this);
        if (materialselectBox.getItems().size <= 0) {
            warningLabel.setText("no items available");
        }
        if (order.getSelectedString() != null) {
            materialselectBox.setSelected(order.getSelectedString());
            menu.getStage().setKeyboardFocus(menu.getOrderList().hasChildren() ? menu.getOrderList() : menu);
        } else {
            materialselectBox.showList();
            menu.validate();
            menu.getStage().setKeyboardFocus(materialselectBox);
        }
    }

    public void hide() {
        menu.getOrderList().removeActor(this);
        menu.getStage().setKeyboardFocus(menu.getOrderList().hasChildren() ? menu.getOrderList() : menu);
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
