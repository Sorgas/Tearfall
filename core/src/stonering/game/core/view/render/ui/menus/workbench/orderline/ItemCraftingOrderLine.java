package stonering.game.core.view.render.ui.menus.workbench.orderline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import stonering.entity.local.building.aspects.WorkbenchAspect;
import stonering.entity.local.crafting.ItemOrder;
import stonering.entity.local.crafting.ItemPartOrder;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.items.selectors.SimpleItemSelector;
import stonering.enums.items.type.ItemTypeMap;
import stonering.enums.items.recipe.Recipe;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.background.BackgroundImagesMap;
import stonering.game.core.view.render.ui.lists.PlaceHolderSelectBox;
import stonering.game.core.view.render.ui.menus.util.HideableComponent;
import stonering.game.core.view.render.ui.menus.util.Highlightable;
import stonering.game.core.view.render.ui.menus.util.HintedActor;
import stonering.game.core.view.render.ui.menus.workbench.WorkbenchMenu;
import stonering.global.utils.Position;
import stonering.util.global.StaticSkin;
import stonering.util.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This line shows list of available crafting recipes.
 * When player selects one of them, this line changes itself to crafting order line.
 * When players cancels order creation, this line hides.
 *
 * @author Alexander
 */
public class ItemCraftingOrderLine extends Table implements HideableComponent, HintedActor, Highlightable {
    private static final String LINE_HINT = "";
    private static final SimpleItemSelector MATERIAL_SELECT_PLACEHOLDER = new SimpleItemSelector("Select Material");
    private static final Recipe RECIPE_SELECT_PLACEHOLDER = new Recipe("Select item");
    private static final String NAME = "workbench_order_line";
    private GameMvc gameMvc;
    private WorkbenchMenu menu;
    private ItemOrder order;

    private HorizontalGroup leftHG;
    private HorizontalGroup rightHG;

    private Label statusLabel;                                  // shows status, updates on status change //TODO make icon
    private PlaceHolderSelectBox<Recipe> recipeSelectBox;
    private Label itemLabel;
    private List<PlaceHolderSelectBox<ItemSelector>> partSelectBoxes;
    private Label warningLabel;                                 // shown when something is not ok
    private TextButton deleteButton;
    private TextButton repeatButton;
    private TextButton upButton;
    private TextButton downButton;

    private SelectBox focusedSelectBox;
    private ArrayList<SelectBox> selectBoxes; //TODO not used until crafting steps are added


    /**
     * Creates this table with two horizontal groups, aligned to left and right with expanding space cell between them.
     */
    private ItemCraftingOrderLine(GameMvc gameMvc) {
        super();
        this.gameMvc = gameMvc;
        this.add(leftHG = new HorizontalGroup());
        this.add().expandX();
        this.add(rightHG = new HorizontalGroup());
        this.defaults().prefHeight(30);
        leftHG.addActor(createStatusLabel());
        selectBoxes = new ArrayList<>();
        addListener(createOrderLineInputListener());
    }

    /**
     * Creates line with empty order and puts all possible recipes into initial selection list.
     */
    public ItemCraftingOrderLine(GameMvc gameMvc, WorkbenchMenu menu) {
        this(gameMvc);
        this.menu = menu;
        this.left();
        leftHG.addActor(createRecipeSelectBox(new ArrayList<>(menu.getWorkbenchAspect().getRecipes())));
        leftHG.addActor(createWarningLabel());
        focusedSelectBox = recipeSelectBox;
    }

    /**
     * Creates line with filled order.
     */
    public ItemCraftingOrderLine(GameMvc gameMvc, WorkbenchMenu menu, ItemOrder order) {
        this(gameMvc);
        this.menu = menu;
        this.order = order;
        Recipe recipe = order.getRecipe();
        leftHG.addActor(createItemLabel());
        for (ItemPartOrder itemPartOrder : order.getParts()) {
            SelectBox<ItemSelector> itemPartSelectBox = createMaterialSelectBox(itemPartOrder);
            leftHG.addActor(itemPartSelectBox);
            itemPartSelectBox.setSelected(itemPartOrder.getSelected());

        }
        leftHG.addActor(createWarningLabel());
        leftHG.right();
        createAndAddControlButtons();
    }

    /**
     * Creates order status label.
     */
    private Label createStatusLabel() {
        statusLabel = new Label("new", StaticSkin.getSkin());
        return statusLabel;
    }

    /**
     * Creates label with item title.
     */
    private Label createItemLabel() {
        String itemTitle = ItemTypeMap.getInstance().getItemType(order.getRecipe().getItemName()).getTitle();
        itemLabel = new Label(itemTitle, StaticSkin.getSkin()); // label with item type
        return itemLabel;
    }

    /**
     * Creates selectBox with list of all workbench recipes. After selection of recipe, this select box is replaced with material selection.
     */
    public PlaceHolderSelectBox createRecipeSelectBox(ArrayList<Recipe> recipeList) {
        ItemCraftingOrderLine line = this;

        Map<String, Recipe> recipeMap = new HashMap<>();
        recipeList.forEach(recipe -> recipeMap.put(recipe.getTitle(), recipe));                                 // create mapping of recipes to select box lines.
        recipeSelectBox = new PlaceHolderSelectBox<>(RECIPE_SELECT_PLACEHOLDER);
        recipeSelectBox.setItems(recipeList.toArray(new Recipe[]{}));
        recipeSelectBox.getListeners().insert(0, new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.D:
                    case Input.Keys.E: { // opens list or saves selected value
                        if (recipeSelectBox.getList().isVisible()) { // select recipe and proceed
                            recipeSelectBox.hideList();
                            if (!recipeSelectBox.getSelected().equals(recipeSelectBox.getPlaceHolder())) {
                                order = new ItemOrder(gameMvc, recipeSelectBox.getSelected());
                                leftHG.removeActor(recipeSelectBox);
                                leftHG.addActorAfter(statusLabel, createItemLabel());
                                SelectBox<ItemSelector> materialSelectBox = createMaterialSelectBox(order.getParts().get(0)); // create select box for first item part.
                                leftHG.addActorAfter(itemLabel, materialSelectBox);
                                focusedSelectBox = materialSelectBox;
                                selectBoxes.add(materialSelectBox);
                            } else { // not a valid case
                                warningLabel.setText("Item not selected");
                            }
                        } else {  // open list
                            recipeSelectBox.navigate(1);
                            recipeSelectBox.showList();
                            recipeSelectBox.getList().toFront();
                        }
                        return true;
                    }
                    case Input.Keys.A:
                    case Input.Keys.Q: {
                        hide();
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
        recipeSelectBox.getList().addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                menu.updateStageFocus(line);
                setHighlighted(true);                         // restore highlighting
                return true;
            }
        });
        return recipeSelectBox;
    }

    /**
     * Creates selectBox for selecting material for item part .
     * SelectBox can have no items after this (if no items available on map).
     */
    private PlaceHolderSelectBox createMaterialSelectBox(ItemPartOrder itemPartOrder) {
        ItemCraftingOrderLine line = this;
        PlaceHolderSelectBox<ItemSelector> materialSelectBox = new PlaceHolderSelectBox<>(MATERIAL_SELECT_PLACEHOLDER);
        Position workbenchPosition = menu.getWorkbenchAspect().getAspectHolder().getPosition();
        itemPartOrder.refreshSelectors(workbenchPosition);
        Array<ItemSelector> itemSelectors = new Array<>(itemPartOrder.getItemSelectors().toArray(new ItemSelector[]{}));

        if (itemPartOrder.isSelectedPossible()) {   // selected is null or is in array
            materialSelectBox.setItems(itemSelectors);
            materialSelectBox.setSelected(itemPartOrder.getSelected());
        } else {
            itemSelectors.add(itemPartOrder.getSelected());
            materialSelectBox.setItems(itemSelectors);
            materialSelectBox.setSelected(itemPartOrder.getSelected());
            //TODO add red status
        }

        materialSelectBox.getListeners().insert(0, new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.D:
                    case Input.Keys.E: { // select material
                        if (materialSelectBox.getList().isVisible()) {
                            itemPartOrder.setSelected(materialSelectBox.getSelected());
                            warningLabel.setText("");
                            statusLabel.setText("ok");
                            createAndAddControlButtons();
                            menu.getWorkbenchAspect().addOrder(order);
                            materialSelectBox.getListeners().removeValue(this, true);
                        }
                        return true;
                    }
                    case Input.Keys.A:
                    case Input.Keys.Q: {   // no return to recipe select, cancel order.
                        hide();
                        return true;
                    }
                }
                return false;
            }
        });
        materialSelectBox.addListener(new InputListener() { // common select box listener
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.E:
                    case Input.Keys.D: { // select item, close this list and go to next selectbox.
                        if (materialSelectBox.getList().isVisible()) {
                            itemPartOrder.setSelected(materialSelectBox.getSelected());
                            materialSelectBox.hideList();
                        } else if (keycode == Input.Keys.E) { // show list
                            materialSelectBox.navigate(1);
                            materialSelectBox.showList();
                            materialSelectBox.getList().toFront();
                        }
                        if (keycode == Input.Keys.D) goToAnotherSelectBox(1);
                        return true;
                    }
                    case Input.Keys.A: {
                        if (!goToAnotherSelectBox(-1)) goToListOrMenu();
                        return true;
                    }
                    case Input.Keys.Q: {
                        goToListOrMenu();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                materialSelectBox.navigate(1);
                return true;
            }
        });
        materialSelectBox.getList().addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                menu.updateStageFocus(line);
                setHighlighted(true);                         // restore highlighting
                return true;
            }
        });
        return materialSelectBox;
    }

    private void createAndAddControlButtons() {
        if (!rightHG.hasChildren()) {
            Table table = this;
            deleteButton = new TextButton("X", StaticSkin.getSkin());
            deleteButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    menu.getWorkbenchAspect().getEntries().remove(order);
                    menu.getOrderList().removeActor(table);
                    menu.getOrderList().navigate(0);  // normalizes index
                    menu.updateStageFocus(menu.getOrderList().hasChildren() ? menu.getOrderList() : menu);
                }
            });
            repeatButton = new TextButton("R", StaticSkin.getSkin());
            repeatButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    order.setRepeated(!order.isRepeated());
//                        statusLabel  //TODO set repeated status
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
    }

    /**
     * Input listener for order line as a whole. Presses right side buttons, cancels order modification.
     */
    private InputListener createOrderLineInputListener() {
        return new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                event.stop();
                if (focusedSelectBox == null || !focusedSelectBox.notify(event, false)) { // transitions between selectboxes should be handled in them.
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
     * Hides this line from screen.
     */
    public void hide() {
        menu.getOrderList().removeActor(this);
    }

    private void goToListOrMenu() {
        if (menu.getOrderList().hasChildren()) {
            menu.updateStageFocus(menu.getOrderList());
            menu.getOrderList().navigate(0);
        } else {
            menu.updateStageFocus(menu);
        }
    }

    /**
     * Tries to move line focus to another select box, specified by delta.
     */
    private boolean goToAnotherSelectBox(int delta) {
        if (focusedSelectBox != null) {
            focusedSelectBox.hideList();
            int index = selectBoxes.indexOf(focusedSelectBox) + delta;
            if (index < selectBoxes.size() && index >= 0) {
                focusedSelectBox = selectBoxes.get(index);
                return true;
            }
        } else {
            TagLoggersEnum.UI.logDebug("no select box focused");
        }
        return false;
    }

    @Override
    public void setHighlighted(boolean value) {
        Image image = BackgroundImagesMap.getInstance().getBackground(NAME + (value ? ":focused" : ""));
        if (image != null) {
            image.setWidth(this.getWidth());
            image.setHeight(this.getHeight());
            this.setBackground(image.getDrawable());
        }
    }

    @Override
    public String getHint() {
        return LINE_HINT;
    }
}
