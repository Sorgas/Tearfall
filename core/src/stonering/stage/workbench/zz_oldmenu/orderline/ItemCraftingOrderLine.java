package stonering.stage.workbench.zz_oldmenu.orderline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import org.jetbrains.annotations.NotNull;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.crafting.IngredientOrder;
import stonering.entity.crafting.ItemOrder;
import stonering.enums.ControlActionsEnum;
import stonering.enums.items.type.ItemTypeMap;
import stonering.stage.workbench.zz_oldmenu.WorkbenchMenuq;
import stonering.widget.Highlightable;
import stonering.util.global.StaticSkin;

import java.util.ArrayList;
import java.util.List;

import static stonering.enums.ControlActionsEnum.*;

/**
 * Order line for selected recipe. {@link ItemOrder}
 * <p>
 * Shows {@link ItemPartSelection} for each part of recipe.
 * Selections are filled with {@link stonering.entity.item.selectors.AnyMaterialTagItemSelector} by default.
 * Player can navigate between them to specify items to use in order.
 * When player cancels order creation, this line is hidden.
 *
 * @author Alexander
 */
public class ItemCraftingOrderLine extends OrderLine implements Highlightable {
    private ItemOrder order;
    private List<ItemPartSelection> selections;
    private TextButton repeatButton;
    private TextButton upButton;
    private TextButton downButton;

    public ItemCraftingOrderLine(WorkbenchMenuq menu, @NotNull ItemOrder order) {
        super(menu, "");
        this.menu = menu;
        this.order = order;
        selections = new ArrayList<>();
        createSelections();
        createAndAddControlButtons();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateHighlighting(isChildrenFocused());
    }

    private boolean isChildrenFocused() {
        for (ItemPartSelection selection : selections) {
            if(selection.isFocused()) return true;
        }
        return false;
    }

    private void createSelections() {
        leftHG.addActor(createItemLabel());
        for (IngredientOrder ingredientOrder : order.parts.values()) {
            addPartSelection(ingredientOrder);
        }
    }

    private void addPartSelection(IngredientOrder ingredientOrder) {
//        ingredientOrder.refreshSelector();
        ItemPartSelection selection = new ItemPartSelection(ingredientOrder, this);
        leftHG.addActor(selection);
        selections.add(selection);
    }

    private void createAndAddControlButtons() {
        repeatButton = addControlButton("R", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                order.setRepeated(!(order.getAmount() > 0));
                //TODO set repeated status
            }
        });
        upButton = addControlButton("R↑", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tryMoveThisLine(-1);
            }
        });
        downButton = addControlButton("F↓", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tryMoveThisLine(1);
            }
        });
    }

    private void tryMoveThisLine(int delta) {
        WorkbenchAspect aspect = menu.getWorkbenchAspect();
        aspect.swapOrders(order, delta);
        menu.getOrderList().moveItem(this, delta);
    }

    /**
     * Creates label with item name.
     */
    private Label createItemLabel() {
        String itemTitle = ItemTypeMap.getInstance().getItemType(order.recipe.itemName).title;
        return new Label(itemTitle, StaticSkin.getSkin()); // label with item type
    }

    /**
     * Input listener for order line as a whole. Presses right side buttons, cancels order modification.
     */
    private InputListener createOrderLineInputListener() {
        return new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                event.stop();
                // transitions between select boxes should be handled in them.
                switch (ControlActionsEnum.getAction(keycode)) {
                    case Z_UP: {
                        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                            if (upButton != null) upButton.toggle();
                        } else {
                            if (repeatButton != null) repeatButton.toggle();
                        }
                        return true;
                    }
                    case Z_DOWN: {
                        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                            if (downButton != null) downButton.toggle();
                            return true;
                        }
                    }
                }
                return false;
            }
        };
    }

    /**
     * Navigates between select boxes inside selections or returns to menu;
     */
    public boolean navigate(ControlActionsEnum action, ItemPartSelection selection) {
        if (action == CANCEL || action == LEFT && selection == selections.get(0)) { // quit with cancel or left on first selection.
            Actor target = menu;
            if (menu.getOrderList().hasChildren()) {
                menu.getOrderList().navigate(0);
                target = menu.getOrderList();
            }
            return menu.getStage().setKeyboardFocus(target);
        }
        if(action == RIGHT && selection == selections.get(selections.size() - 1)) return false; // nothing on right on last selection.
        int delta = action == LEFT ? -1 : 0;
        delta = action == RIGHT ? 1 : delta;
        if (delta == 0) return false; // invalid case
        int index = selections.indexOf(selection) + delta;
        return selections.get(index).navigate(action == RIGHT ? LEFT : RIGHT);
    }

    public void navigateToFirst() {
        selections.get(0).navigate(LEFT);
    }
}
