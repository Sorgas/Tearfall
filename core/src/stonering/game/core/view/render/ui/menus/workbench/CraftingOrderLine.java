package stonering.game.core.view.render.ui.menus.workbench;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.local.crafting.ItemOrder;
import stonering.entity.local.items.Item;
import stonering.enums.items.Recipe;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.lists.NavigableList;
import stonering.game.core.view.render.ui.lists.NavigableSelectBox;
import stonering.game.core.view.render.ui.menus.util.Invokable;
import stonering.utils.global.Pair;
import stonering.utils.global.StaticSkin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Single line in workbench menu.
 * Used to create of modify orders.
 * Item type of order cannot be changed.
 *
 * @author Alexander on 28.10.2018.
 */
public class CraftingOrderLine extends Table implements Invokable {
    private GameMvc gameMvc;
    private WorkbenchMenu menu;

    private boolean repeatable;
    private ItemOrder order;

    private Label itemType;
    private NavigableSelectBox<String> materialselectBox;

    private NavigableList itemTypeList;

    /**
     * Creates line by given order.
     */
    public CraftingOrderLine(GameMvc gameMvc, WorkbenchMenu menu, ItemOrder order) {
        super(StaticSkin.getSkin());
        this.gameMvc = gameMvc;
        this.menu = menu;
        this.order = order;
        createOrderLine(order);
    }

    /**
     * Creates line with empty order and puts all possible recipes into initial selection list.
     */
    public CraftingOrderLine(GameMvc gameMvc, WorkbenchMenu menu) {
        super(StaticSkin.getSkin());
        this.gameMvc = gameMvc;
        this.menu = menu;
        createRecipeSelectList(new ArrayList<>(menu.getWorkbenchAspect().getRecipes()));
    }

    /**
     * Creates line with list of all workbench recipes.
     */
    public void createRecipeSelectList(ArrayList<Recipe> recipeList) {
        Map<String, Recipe> recipeMap = new HashMap<>();
        recipeList.forEach(recipe -> recipeMap.put(recipe.getName(), recipe));
        itemTypeList = new NavigableList();
        itemTypeList.setSelectListener(event -> { // hides list and creates empty order for recipe
            if (itemTypeList.getSelectedIndex() >= 0) {
                String selected = (String) itemTypeList.getItems().get(itemTypeList.getSelectedIndex());
                itemTypeList.hide();
                createOrderLine(createOrder(recipeMap.get(selected)));
            }
            return true;
        });
        itemTypeList.setShowListener(event -> { // fills list with recipes
            itemTypeList.setItems(recipeMap.keySet().toArray(new String[]{}));
            return true;
        });
        itemTypeList.setHideListener(event -> { // removes list from table
            this.clearChildren();
            itemTypeList = null;
            return true;
        });
        this.add(itemTypeList).left().expandX();
    }

    private ItemOrder createOrder(Recipe recipe) {
        return new ItemOrder(recipe);
    }

    /**
     * Creates selectBoxes for crafting steps from order.
     */
    private void createOrderLine(ItemOrder order) {
        itemType = new Label(order.getRecipe().getItemName(), StaticSkin.getSkin());
        java.util.List<Item> items = gameMvc.getModel().getItemContainer().getResourceItemListByMaterialType(order.getRecipe().getMaterial());
        Map<String, Pair<String, String>> materialsMap =  gameMvc.getModel().getItemContainer().formItemListFor(items);
        materialselectBox = new NavigableSelectBox<>();
        materialselectBox.setItems(materialsMap.keySet().toArray(new String[]{}));
    }

    @Override
    public boolean invoke(int keycode) {
        if(itemTypeList != null) {
            return itemTypeList.invoke(keycode);
        }
        if(materialselectBox != null) {
            return materialselectBox.invoke(keycode);
        }
        return false;
    }

    private void handleCancel() {

    }

    private void handleSelect() {
        //TODO create order line
    }

    private void moveSelection(int delta) {
        itemTypeList.setSelectedIndex(NormalizeIndex(itemTypeList.getSelectedIndex() + delta));
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
