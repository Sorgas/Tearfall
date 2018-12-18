package stonering.game.core.view.render.ui.menus.workbench.orderline;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import stonering.entity.local.crafting.ItemOrder;
import stonering.enums.items.ItemTypeMap;
import stonering.enums.items.Recipe;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.lists.PlaceHolderSelectBox;
import stonering.game.core.view.render.ui.menus.util.HideableComponent;
import stonering.game.core.view.render.ui.menus.util.Highlightable;
import stonering.game.core.view.render.ui.menus.workbench.WorkbenchMenu;
import stonering.global.utils.Position;
import stonering.utils.global.StaticSkin;

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
public class ItemCraftingOrderLine extends Table implements HideableComponent, Highlightable {
    private static final String MATERIAL_SELECT_PLACEHOLDER = "Select Material";
    private GameMvc gameMvc;
    private WorkbenchMenu menu;
    private ItemOrder order;

    private HorizontalGroup leftHG;
    private HorizontalGroup rightHG;

    private Label statusLabel;                                  // shows status, updates on status change
    private PlaceHolderSelectBox<String> recipeSelectBox;
    private Label itemLabel;
    private PlaceHolderSelectBox<String> materialSelectBox;       // allows to select material for crafting - main element of this line.
    private Label warningLabel;                                 // shown when something is not ok
    private TextButton deleteButton;
    private TextButton repeatButton;
    private TextButton upButton;
    private TextButton downButton;

    /**
     * Creates this table with two horizontal groups, aligned to left and right with expanding space cell between them.
     */
    private ItemCraftingOrderLine(GameMvc gameMvc) {
        super();
        this.gameMvc = gameMvc;
        this.add(leftHG = new HorizontalGroup());
        this.add().expandX();
        this.add(rightHG = new HorizontalGroup());
        leftHG.addActor(createStatusLabel());
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
    }

    /**
     * Creates line with filled order.
     */
    public ItemCraftingOrderLine(GameMvc gameMvc, WorkbenchMenu menu, ItemOrder order) {
        this(gameMvc);
        this.menu = menu;
        this.order = order;
        leftHG.addActor(createItemLabel());
        leftHG.addActor(createMaterialSelectBox());
        materialSelectBox.setSelected(order.getSelectedString());
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
     * Creates line with list of all workbench recipes. Sets stage focus to this.
     */
    public PlaceHolderSelectBox createRecipeSelectBox(ArrayList<Recipe> recipeList) {
        Map<String, Recipe> recipeMap = new HashMap<>();
        recipeList.forEach(recipe -> recipeMap.put(recipe.getTitle(), recipe));                                 // create mapping of recipes to select box lines.
        recipeSelectBox = new PlaceHolderSelectBox<>("Select item");
        recipeSelectBox.setItems(recipeMap.keySet().toArray(new String[]{}));
        recipeSelectBox.getSelectKeys().add(Input.Keys.D);
        recipeSelectBox.getListeners().insert(0, new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.E:
                    case Input.Keys.D: {                                                                        // select recipe and go to material
                        if (recipeSelectBox.getList().isVisible()) {
                            if (!recipeSelectBox.getSelected().equals(recipeSelectBox.getPlaceHolder())) {        //  should be always true when list is visible
                                // create order for selected recipe
                                order = new ItemOrder(gameMvc, recipeMap.get(recipeSelectBox.getSelected()));
                                // replace select box with label. it will not be changed for this order.
                                leftHG.removeActor(recipeSelectBox);
                                leftHG.removeActor(warningLabel);
                                leftHG.addActor(createItemLabel());
                                // create select box for material selection.
                                leftHG.addActor(createMaterialSelectBox());
                                leftHG.addActor(createWarningLabel());
                                getStage().setKeyboardFocus(materialSelectBox);
                            } else {
                                warningLabel.setText("Item not selected");
                            }
                            recipeSelectBox.hideList();
                        } else {
                            recipeSelectBox.navigate(1);
                            recipeSelectBox.showList();
                            recipeSelectBox.getList().toFront();
                        }
                        return true;
                    }
                    case Input.Keys.Q:
                    case Input.Keys.A: {
                        hide();
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
                getStage().setKeyboardFocus(recipeSelectBox);
                return true;
            }
        });
        return recipeSelectBox;
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
     * Creates selectBox for order.
     * SelectBox can have no items after this.
     */
    private PlaceHolderSelectBox createMaterialSelectBox() {
        materialSelectBox = new PlaceHolderSelectBox<>(MATERIAL_SELECT_PLACEHOLDER);
        Position workbenchPosition = menu.getWorkbenchAspect().getAspectHolder().getPosition();
        ArrayList<String> items = new ArrayList<>(order.getAvailableItemList(workbenchPosition));
        materialSelectBox.setItems(items.toArray(new String[]{}));
        materialSelectBox.getSelectKeys().add(Input.Keys.D);
        materialSelectBox.getListeners().insert(0, new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.E:
                    case Input.Keys.D: {                                                    // complete order
                        if (materialSelectBox.getList().isVisible()) {
                            if (!materialSelectBox.getSelected().equals(materialSelectBox.getPlaceHolder())) {
                                order.setSelectedString(materialSelectBox.getSelected());
                                warningLabel.setText("");
                                statusLabel.setText("ok");
                                createAndAddControlButtons();
                                menu.getWorkbenchAspect().getOrders().add(0, order);
                                getStage().setKeyboardFocus(menu.getOrderList());
                            } else {
                                warningLabel.setText("Select material");
                            }
                        } else {
                            materialSelectBox.navigate(1);
                            materialSelectBox.showList();
                            recipeSelectBox.getList().toFront();
                        }
                        return true;
                    }
                    case Input.Keys.A: {
                        leftHG.removeActor(materialSelectBox);
                        leftHG.removeActor(itemLabel);
                        leftHG.addActor(createRecipeSelectBox(new ArrayList<>(menu.getWorkbenchAspect().getRecipes())));
                        getStage().setKeyboardFocus(recipeSelectBox);
                        return true;
                    }
                    case Input.Keys.Q: {   // no return to recipe select, cancel order.
                        hide();
                        return true;
                    }
                }
                return true;
            }
        });
        return materialSelectBox;
    }

    private void createAndAddControlButtons() {
        Table table = this;
        deleteButton = new TextButton("X", StaticSkin.getSkin());
        deleteButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menu.getWorkbenchAspect().getOrders().remove(order);
                menu.getOrderList().removeActor(table);
                return true;
            }
        });
        repeatButton = new TextButton("R", StaticSkin.getSkin());
        repeatButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                order.setRepeated(!order.isRepeated());
                return true;
            }
        });
        upButton = new TextButton("R↑", StaticSkin.getSkin());
        upButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

//                menu.getWorkbenchAspect().swapOrders(); getOrders().indexOf(order);
                return true;
            }
        });
        downButton = new TextButton("F↓", StaticSkin.getSkin());
        downButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                order.setRepeated(!order.isRepeated());
                return true;
            }
        });
        rightHG.addActor(repeatButton);
        rightHG.addActor(upButton);
        rightHG.addActor(downButton);
        rightHG.addActor(deleteButton);
    }

    /**
     * Creates label for showing messages.
     */
    private Label createWarningLabel() {
        warningLabel = new Label("", StaticSkin.getSkin());                               // label with item type
        return warningLabel;
    }

    /**
     * Shows this line in menu.
     */
    @Override
    public void show() {
        menu.getOrderList().addActorAt(0, this);
    }

    /**
     * Hides this line from menu.
     */
    public void hide() {
        menu.getOrderList().removeActor(this);
        menu.updateFocusAndBackground(menu.getOrderList().hasChildren() ? menu.getOrderList() : menu);
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public PlaceHolderSelectBox<String> getRecipeSelectBox() {
        return recipeSelectBox;
    }

    @Override
    public void setHighlighted(boolean value) {
//        this.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("sprites/ui_back.png"), value ? 100 : 0, 0, 100, 100)));
    }
}
