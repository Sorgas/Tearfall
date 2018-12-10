package stonering.game.core.view.render.ui.menus.workbench.orderline;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import stonering.entity.local.crafting.ItemOrder;
import stonering.enums.items.ItemTypeMap;
import stonering.enums.items.Recipe;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.lists.NavigableList;
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
public class ItemCraftingOrderLine extends Container implements HideableComponent, Highlightable {
    private static final String MATERIAL_SELECT_PLACEHOLDER = "Select Material";
    GameMvc gameMvc;
    private WorkbenchMenu menu;

    private HorizontalGroup horizontalGroup;

    private boolean repeatable;
    private ItemOrder order;

    private Label statusLabel;                                  // shows status, updates on status change
    private NavigableList itemTypeList;
    private Label itemType;                                     // shows selected item, static
    private PlaceHolderSelectBox<String> materialselectBox;       // allows to select material for crafting - main element of this line.
    private Label warningLabel;                                 // shown when something is not ok

    private ItemCraftingOrderLine(GameMvc gameMvc) {
        super();
        this.gameMvc = gameMvc;
        horizontalGroup = new HorizontalGroup();
        this.setActor(horizontalGroup);
        createStatusLabel();
        setDebug(true, true);
    }

    /**
     * Creates line with empty order and puts all possible recipes into initial selection list.
     */
    public ItemCraftingOrderLine(GameMvc gameMvc, WorkbenchMenu menu) {
        this(gameMvc);
        this.menu = menu;
        this.left();
        createRecipeSelectList(new ArrayList<>(menu.getWorkbenchAspect().getRecipes()));
    }

    /**
     * Creates line with filled order.
     */
    public ItemCraftingOrderLine(GameMvc gameMvc, WorkbenchMenu menu, ItemOrder order) {
        this(gameMvc, menu);
        this.order = order;
        this.menu = menu;
        createItemLabel();
        createMaterialSelectBox();
        createWarningLabel();
    }

    /**
     * Creates order status label.
     */
    private void createStatusLabel() {
        statusLabel = new Label("new", StaticSkin.getSkin());
        horizontalGroup.addActor(statusLabel);
    }

    /**
     * Creates line with list of all workbench recipes. Sets stage focus to this.
     */
    public void createRecipeSelectList(ArrayList<Recipe> recipeList) {
        Map<String, Recipe> recipeMap = new HashMap<>();
        recipeList.forEach(recipe -> recipeMap.put(recipe.getTitle(), recipe));
        itemTypeList = new NavigableList();
        itemTypeList.setItems(recipeMap.keySet().toArray(new String[]{}));
        itemTypeList.setSelectListener(event -> {                                               // hides list and creates empty order for recipe
            if (itemTypeList.getSelectedIndex() >= 0) {
                String selectedRecipe = (String) itemTypeList.getItems().get(itemTypeList.getSelectedIndex());
                order = new ItemOrder(gameMvc, recipeMap.get(selectedRecipe));
                horizontalGroup.removeActor(itemTypeList);
                createItemLabel();
                createMaterialSelectBox();
                createWarningLabel();
                getStage().setKeyboardFocus(materialselectBox);
            }
            return true;
        });
        itemTypeList.setHideListener(event -> {                                                 // removes order
            event.stop();
            hide();
            return true;
        });
        horizontalGroup.addActor(itemTypeList);
        itemTypeList.show();
    }

    /**
     * Creates label with item title.
     */
    private void createItemLabel() {
        String itemTitle = ItemTypeMap.getInstance().getItemType(order.getRecipe().getItemName()).getTitle();
        itemType = new Label(itemTitle, StaticSkin.getSkin()); // label with item type
        horizontalGroup.addActor(itemType);
    }

    /**
     * Creates selectBox for order.
     * SelectBox can have no items after this.
     */
    private void createMaterialSelectBox() {
        materialselectBox = new PlaceHolderSelectBox<>();
        materialselectBox.setPlaceHolder(MATERIAL_SELECT_PLACEHOLDER);
        horizontalGroup.addActor(materialselectBox);
        Position workbenchPosition = menu.getWorkbenchAspect().getAspectHolder().getPosition();
        ArrayList<String> items = new ArrayList<>(order.getAvailableItemList(workbenchPosition));
        materialselectBox.setItems(items.toArray(new String[]{}));
        materialselectBox.setSelectListener(event -> {
            order.setSelectedString(materialselectBox.getSelected());
            return true;
        });
        materialselectBox.setCancelListener(event -> {
            hide();
            return true;
        });
    }

    /**
     * Creates label for showing messages.
     */
    private void createWarningLabel() {
        warningLabel = new Label("", StaticSkin.getSkin());                               // label with item type
        horizontalGroup.addActor(warningLabel);
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

    public NavigableList getItemTypeList() {
        return itemTypeList;
    }

    @Override
    public void setHighlighted(boolean value) {
        this.setBackground(new TextureRegionDrawable(
                new TextureRegion(new Texture("sprites/ui_back.png"), value ? 100 : 0, 0, 100, 100)));
    }
}
