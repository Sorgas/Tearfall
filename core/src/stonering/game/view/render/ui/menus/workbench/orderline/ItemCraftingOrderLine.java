package stonering.game.view.render.ui.menus.workbench.orderline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import stonering.entity.local.building.aspects.WorkbenchAspect;
import stonering.entity.local.crafting.ItemOrder;
import stonering.entity.local.crafting.ItemPartOrder;
import stonering.entity.local.item.selectors.ItemSelector;
import stonering.entity.local.item.selectors.SimpleItemSelector;
import stonering.enums.ControlActionsEnum;
import stonering.enums.OrderStatusEnum;
import stonering.enums.items.type.ItemTypeMap;
import stonering.enums.items.recipe.Recipe;
import stonering.game.view.render.ui.images.DrawableMap;
import stonering.game.view.render.ui.lists.PlaceHolderSelectBox;
import stonering.game.view.render.ui.menus.util.HideableComponent;
import stonering.game.view.render.ui.menus.util.HintedActor;
import stonering.game.view.render.ui.menus.workbench.WorkbenchMenu;
import stonering.util.geometry.Position;
import stonering.util.global.StaticSkin;
import stonering.util.global.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This line shows list of available crafting recipes.
 * When player selects one of them, this line changes itself to crafting order line.
 * When players cancels order creation, this line hides.
 *
 * @author Alexander
 */
public class ItemCraftingOrderLine extends Table implements HideableComponent, HintedActor {
    private static final String LINE_HINT = "order line hint";
    private static final String BACKGROUND_NAME = "workbench_order_line";

    private WorkbenchMenu menu;
    private ItemOrder order;

    private HorizontalGroup leftHG;                             // contains select boxes for item parts.
    private HorizontalGroup rightHG;                            // contains control buttons.
    private StatusIcon statusIcon;                              // shows status, updates on status change
    private PlaceHolderSelectBox<Recipe> recipeSelectBox;
    private List<PlaceHolderSelectBox<ItemSelector>> partSelectBoxes;
    private Label warningLabel;                                 // shown when something is not ok

    private TextButton deleteButton;
    private TextButton repeatButton;
    private TextButton upButton;
    private TextButton downButton;

    private PlaceHolderSelectBox focusedSelectBox; // manual focus for select boxes of this line
    private HighlightHandler highlightHandler;

    /**
     * Creates line with filled order.
     */
    public ItemCraftingOrderLine(WorkbenchMenu menu, @Nullable ItemOrder order) {
        super();
        this.menu = menu;
        this.order = order;
        partSelectBoxes = new ArrayList<>();
        createTable();
        initLine();
    }

    /**
     * Creates table with default actors (status label, warning label).
     */
    private void createTable() {
        left();
        add(new StatusIcon(OrderStatusEnum.WAITING));
        add(leftHG = new HorizontalGroup());
        add(createWarningLabel());
        add().expandX();
        add(rightHG = new HorizontalGroup());
        defaults().prefHeight(30);
        addListener(createOrderLineInputListener());
        leftHG.right();
    }

    /**
     * Applies highlighting if this line is focused.
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        if ((getStage().getKeyboardFocus() == this) != highlightHandler.value)
            highlightHandler.accept(highlightHandler.value);
    }

    /**
     * Creates select box for recipe if no order is given, or fills line from order.
     */
    private void initLine() {
        if (order == null) {
            leftHG.addActor(createRecipeSelectBox(new ArrayList<>(menu.getWorkbenchAspect().getRecipes())));
            focusedSelectBox = recipeSelectBox;
        } else {
            leftHG.addActor(createItemLabel());                      // item name from order
            for (ItemPartOrder itemPartOrder : order.getParts()) {   // select boxes for item parts
                tryAddPartSelectBox(itemPartOrder);
            }
            focusedSelectBox = partSelectBoxes.get(0);
            createAndAddControlButtons();                            // buttons for managing order
        }
        highlightHandler = new HighlightHandler();                   // changes images image
    }

    /**
     * Creates selectBox with list of all workbench recipes. After selection of recipe, this select box is replaced with material selection.
     */
    public PlaceHolderSelectBox createRecipeSelectBox(ArrayList<Recipe> recipeList) {
        Map<String, Recipe> recipeMap = new HashMap<>();
        recipeList.forEach(recipe -> recipeMap.put(recipe.title, recipe));                                 // create mapping of recipes to select box lines.
        recipeSelectBox = new PlaceHolderSelectBox<>(new Recipe("Select item"));
        recipeSelectBox.setItems(recipeList.toArray(new Recipe[]{}));
        recipeSelectBox.getListeners().insert(0, new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (ControlActionsEnum.getAction(keycode)) {
                    case RIGHT:
                    case SELECT: { // opens list or saves selected value
                        if (recipeSelectBox.getList().getStage() != null) { // list is shown
                            recipeSelectBox.hideList();
                            if (!recipeSelectBox.getSelected().equals(recipeSelectBox.getPlaceHolder())) { // placeholder is selected
                                order = new ItemOrder(recipeSelectBox.getSelected());
                                leftHG.removeActor(recipeSelectBox);
                                leftHG.addActorAfter(statusIcon, createItemLabel());
                                tryAddPartSelectBox(order.getParts().get(0)); // add SB or warning label
                            } else { // not a valid case
                                warningLabel.setText("Item not selected");
                            }
                        } else {  // open list
                            showSelectBoxList(recipeSelectBox);
                        }
                        return true;
                    }
                    case LEFT:
                    case CANCEL: {
                        goToListOrMenu();
                        return true;
                    }
                }
                return super.keyDown(event, keycode);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                recipeSelectBox.navigate(1);
                return true;
            }
        });
        recipeSelectBox.getList().addListener(createTouchListener());
        return recipeSelectBox;
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
                menu.updateMenuHint(line);
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

    /**
     * Shows list of given select box. PlaceHolder is removed after this.
     *
     * @param selectBox
     */
    private void showSelectBoxList(PlaceHolderSelectBox selectBox) {
        selectBox.navigate(0);
        selectBox.showList();
        selectBox.getList().toFront();
    }

    private void createAndAddControlButtons() {
        if (rightHG.hasChildren()) return;
        deleteButton = new TextButton("X", StaticSkin.getSkin());
        deleteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menu.getWorkbenchAspect().removeOrder(order);
                hide();
                goToListOrMenu();
            }
        });
        repeatButton = new TextButton("R", StaticSkin.getSkin());
        repeatButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                order.setRepeated(!(order.getAmount() > 0));
//                        statusIcon  //TODO set repeated status
            }
        });
        upButton = new TextButton("R↑", StaticSkin.getSkin());
        upButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tryMoveThisLine(-1);
            }
        });
        downButton = new TextButton("F↓", StaticSkin.getSkin());
        downButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tryMoveThisLine(1);
            }
        });
        rightHG.addActor(repeatButton);
        rightHG.addActor(upButton);
        rightHG.addActor(downButton);
        rightHG.addActor(deleteButton);
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
                if (focusedSelectBox != null && focusedSelectBox.notify(event, false)) return false;
                switch (keycode) {
                    case Input.Keys.X: { // delete order from screen and workbench
                        if (deleteButton != null) deleteButton.toggle();
                        return true;
                    }
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
     * Creates label for showing messages.
     */
    private Label createWarningLabel() {
        warningLabel = new Label("", StaticSkin.getSkin());                               // label with item type
        return warningLabel;
    }

    /**
     * Shows this line in its screen.
     */
    @Override
    public void show() {
        menu.getOrderList().addActorAt(0, this);
    }

    /**
     * Hides this line from screen. Stage focus not changed.
     */
    public void hide() {
        menu.getOrderList().removeActor(this);
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
        menu.updateMenuHint(target);
        if (order == null || !order.isDefined()) { // row with not started or incomplete order
            Logger.UI.logDebug("Removing incomplete order from list.");
            hide();
        }
    }

    @Override
    public String getHint() {
        return LINE_HINT;
    }

    private class HighlightHandler implements Consumer<Boolean> {
        boolean value;

        @Override
        public void accept(Boolean value) {
            Drawable drawable = DrawableMap.getInstance().getDrawable(BACKGROUND_NAME + (value ? ":focused" : ""));
            this.value = value;
            if (drawable == null) return;
            ItemCraftingOrderLine.this.setBackground(drawable);
        }
    }
}
