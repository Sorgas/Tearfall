package stonering.game.view.render.ui.menus.workbench.orderline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import org.jetbrains.annotations.NotNull;
import stonering.entity.local.building.aspects.WorkbenchAspect;
import stonering.entity.local.crafting.ItemOrder;
import stonering.entity.local.crafting.ItemPartOrder;
import stonering.entity.local.item.selectors.ItemSelector;
import stonering.enums.items.type.ItemTypeMap;
import stonering.game.view.render.ui.lists.PlaceHolderSelectBox;
import stonering.game.view.render.ui.menus.workbench.WorkbenchMenu;
import stonering.util.global.StaticSkin;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows {@link ItemPartSelection} for each part of recipe.
 * Selections are filled with {@link stonering.entity.local.item.selectors.AnyMaterialTagItemSelector} by default.
 * Player can navigate between them to specify items to use in order.
 * When player cancels order creation, this line is hidden.
 *
 * @author Alexander
 */
public class ItemCraftingOrderLine extends OrderLine {
    private ItemOrder order;
    private List<PlaceHolderSelectBox<ItemSelector>> partSelectBoxes;

    private TextButton repeatButton;
    private TextButton upButton;
    private TextButton downButton;

    public ItemCraftingOrderLine(WorkbenchMenu menu, @NotNull ItemOrder order) {
        super(menu, "");
        this.menu = menu;
        this.order = order;
        partSelectBoxes = new ArrayList<>();
        initLine();
    }

    /**
     * Creates select box for recipe if no order is given, or fills line from order.
     */
    private void initLine() {
        leftHG.addActor(createItemLabel());
        for (ItemPartOrder itemPartOrder : order.getParts()) {   // select boxes for item parts
            tryAddPartSelectBox(itemPartOrder);
        }
        focusedSelectBox = partSelectBoxes.get(0); // part select boxes should never be empty, otherwise recipe is invalid
        createAndAddControlButtons();                            // buttons for managing order
    }

    /**
     * Creates and adds SB for itemPartOrder, or updates warning label if no item found.
     */
    private void tryAddPartSelectBox(ItemPartOrder itemPartOrder) {
        itemPartOrder.refreshSelectors(menu.getWorkbench().getPosition());
        PlaceHolderSelectBox<ItemSelector> materialSelectBox = new ItemPartOrderSelectBox(itemPartOrder);
        leftHG.addActor(materialSelectBox);
        focusedSelectBox = materialSelectBox;
        partSelectBoxes.add(materialSelectBox);
    }

    private InputListener createTouchListener() {
        ItemCraftingOrderLine line = this;
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                getStage().setKeyboardFocus(line);
                return true;
            }
        };
    }

    /**
     * Handles update of given SB. If order becomes defined, saves it to WB.
     */
    private void handleMaterialSelection(int index) {
        order.getParts().get(index).setSelected(partSelectBoxes.get(index).getSelected()); // update item part order
        if (!order.isDefined()) return;
        Logger.TASKS.logDebug("Order " + order.getRecipe().name + " added to " + menu.getWorkbench());
        menu.getWorkbenchAspect().addOrder(order);
    }

    /**
     * Moves focus for delta SB, creates next SB if needed, exits to orderList if no SB available.
     */
    private void handleOrderLineNavigation(int previousIndex, int delta) {
        int index = previousIndex + Integer.signum(delta);
        if (index < 0) {                                // was first SB
            goToListOrMenu();
        } else if (index >= partSelectBoxes.size()) {    // was last SB
            if (order.getParts().size() - 1 > previousIndex) {         // order has more parts
                tryAddPartSelectBox(order.getParts().get(index));
            } else { // no mere parts, exit
                goToListOrMenu();
            }
        } else { // normal transition
            focusedSelectBox = partSelectBoxes.get(index);
        }
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
        aspect.swapOrders(aspect.getEntries().indexOf(order), delta);
        menu.getOrderList().moveItem(this, delta);
    }

    /**
     * Creates label with item title.
     */
    private Label createItemLabel() {
        String itemTitle = ItemTypeMap.getInstance().getItemType(order.getRecipe().itemName).title;
        return new Label(itemTitle, StaticSkin.getSkin()); // label with item type
    }

    /**
     * Returns focus to order list or menu, removes current order, if it's not defined.
     */
    private void goToListOrMenu() {
        Actor target = menu;
        if (menu.getOrderList().hasChildren()) {
            menu.getOrderList().navigate(0);
            target = menu.getOrderList();
        }
        menu.getStage().setKeyboardFocus(target);
        if (order == null || !order.isDefined()) { // row with not started or incomplete order
            Logger.UI.logDebug("Removing incomplete order from list.");
            hide();
        }
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
                switch (keycode) {
                    case Input.Keys.R: {
                        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                            if (repeatButton != null) repeatButton.toggle();
                        } else {
                            if (upButton != null) upButton.toggle();
                        }
                        return true;
                    }
                    case Input.Keys.F: {
                        if (downButton != null) downButton.toggle();
                        return true;
                    }
                }
                return false;
            }
        };
    }
}
