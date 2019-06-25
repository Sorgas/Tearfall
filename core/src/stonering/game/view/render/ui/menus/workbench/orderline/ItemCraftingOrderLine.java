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
import stonering.entity.local.item.selectors.SimpleItemSelector;
import stonering.enums.items.type.ItemTypeMap;
import stonering.game.view.render.ui.lists.PlaceHolderSelectBox;
import stonering.game.view.render.ui.menus.workbench.WorkbenchMenu;
import stonering.util.geometry.Position;
import stonering.util.global.StaticSkin;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * When players cancels order creation, this line hides.
 *
 * @author Alexander
 */
public class ItemCraftingOrderLine extends OrderLine {
    private static final String LINE_HINT = "order line hint";

    private ItemOrder order;
    private List<PlaceHolderSelectBox<ItemSelector>> partSelectBoxes;

    private TextButton repeatButton;
    private TextButton upButton;
    private TextButton downButton;

    private PlaceHolderSelectBox focusedSelectBox; // manual focus for select boxes of this line

    /**
     * Creates line with filled order.
     */
    public ItemCraftingOrderLine(WorkbenchMenu menu, @NotNull ItemOrder order) {
        super(menu, LINE_HINT);
        this.menu = menu;
        this.order = order;
        partSelectBoxes = new ArrayList<>();
        initLine();
    }

    /**
     * Creates select box for recipe if no order is given, or fills line from order.
     */
    private void initLine() {
        leftHG.addActor(createItemLabel());                      // item name from order
        for (ItemPartOrder itemPartOrder : order.getParts()) {   // select boxes for item parts
            tryAddPartSelectBox(itemPartOrder);
        }
        focusedSelectBox = partSelectBoxes.get(0);
        createAndAddControlButtons();                            // buttons for managing order
    }


    /**
     * Creates and adds SB for itemPartOrder, or updates warning label if no item found.
     */
    private void tryAddPartSelectBox(ItemPartOrder itemPartOrder) {
        itemPartOrder.refreshSelectors(menu.getWorkbench().getPosition());
        List<ItemSelector> itemSelectors = itemPartOrder.getItemSelectors();
        if (itemSelectors.isEmpty()) {
            warningLabel.setText("No item for " + itemPartOrder.getName() + " available");
        } else {
            PlaceHolderSelectBox<ItemSelector> materialSelectBox = createMaterialSelectBox(itemPartOrder, itemSelectors);
            leftHG.addActor(materialSelectBox);
            focusedSelectBox = materialSelectBox;
            partSelectBoxes.add(materialSelectBox);
        }
    }

    /**
     * Creates selectBox for selecting material for item part.
     * If no item is available,
     * SelectBox can have no item after this (if no item available on map).
     */
    private PlaceHolderSelectBox createMaterialSelectBox(ItemPartOrder itemPartOrder, List<ItemSelector> itemSelectors) {
        int currentIndex = order.getParts().indexOf(itemPartOrder);
        PlaceHolderSelectBox<ItemSelector> materialSelectBox = new PlaceHolderSelectBox<>(new SimpleItemSelector("Select Material"));
        Position workbenchPosition = menu.getWorkbench().getPosition();
        itemPartOrder.refreshSelectors(workbenchPosition);
        if (itemPartOrder.isSelectedPossible()) {   // selected is null or is in array
            materialSelectBox.setItems(itemSelectors);
            materialSelectBox.setSelected(itemPartOrder.getSelected());
        } else {
            itemSelectors.add(itemPartOrder.getSelected());
            materialSelectBox.setItems(itemSelectors);
            materialSelectBox.setSelected(itemPartOrder.getSelected());
            //TODO add red status
        }
        materialSelectBox.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                // TODO update status and warning labels
                switch (keycode) {
                    // confirms selected option, if dropdown is open. Otherwise, opens dropdown. no transition
                    case Input.Keys.E: {
                        if (materialSelectBox.getList().getStage() != null) {
                            handleMaterialSelection(currentIndex);
                            materialSelectBox.hideList();
                        } else {
                            showSelectBoxList(materialSelectBox);
                        }
                        return true;
                    }
                    // confirms selected option, if dropdown is open. Then, moves to next or previous select box, or creates it, or exits to list
                    // (recipe selection is unavalable at this point)
                    case Input.Keys.A:
                    case Input.Keys.D: {
                        if (materialSelectBox.getList().getStage() != null) { // select and move
                            handleMaterialSelection(currentIndex);
                            materialSelectBox.hideList();
                            handleOrderLineNavigation(currentIndex, (keycode == Input.Keys.D ? 1 : -1)); // move to one SB left or right.
                        } else if (materialSelectBox.getPlaceHolder().equals(materialSelectBox.getSelected())) { // show list if nothing was selected
                            showSelectBoxList(materialSelectBox);
                        } else {
                            handleOrderLineNavigation(currentIndex, (keycode == Input.Keys.D ? 1 : -1)); // move to one SB left or right.
                        }
                        return true;
                    }
                    // hides dropdown and goes to list. if order is not finished, cancels it.
                    case Input.Keys.Q: {
                        materialSelectBox.hideList();
                        goToListOrMenu();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                materialSelectBox.navigate(0);
                return true;
            }
        });
        materialSelectBox.getList().addListener(createTouchListener());
        return materialSelectBox;
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
        if (rightHG.hasChildren()) return;
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
        getStage().setKeyboardFocus(target);
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
