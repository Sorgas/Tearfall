package stonering.game.core.view.render.ui.menus.workbench;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.local.crafting.ItemOrder;
import stonering.enums.items.Recipe;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.lists.NavigableList;
import stonering.game.core.view.render.ui.menus.util.Invokable;
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
    private SelectBox<String> material;

    private NavigableList list;

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
        list = new NavigableList();
        list.setSelectListener(event -> { // hides list and creates empty order for recipe
            if (list.getSelectedIndex() >= 0) {
                String selected = (String) list.getItems().get(list.getSelectedIndex());
                list.hide();
                createOrderLine(createOrder(recipeMap.get(selected)));
            }
            return true;
        });
        list.setShowListener(event -> { // fills list with recipes
            list.setItems(recipeMap.keySet().toArray(new String[]{}));
            return true;
        });
        list.setHideListener(event -> { // removes list from table
            this.clearChildren();
            return true;
        });
        this.add(list).left().expandX();
    }

    private ItemOrder createOrder(Recipe recipe) {

    }

    /**
     * Creates selectBoxes for crafting steps from order.
     */
    private void createOrderLine(ItemOrder order) {
        itemType = new Label(order.getType().getName(), StaticSkin.getSkin());
        material = new SelectBox<>(StaticSkin.getSkin());
        gameMvc.getModel().getItemContainer().getResourceItemList(order.getSelectors()));
        material.setItems();
        steps.add(new SelectBox<String>());
    }

    @Override
    public boolean invoke(int keycode) {
        switch (keycode) {
            case Input.Keys.R:
                moveSelection(-1);
            case Input.Keys.F:
                moveSelection(1);
            case Input.Keys.E:
                handleSelect();
            case Input.Keys.Q:
                handleCancel();
        }
        return false;
    }

    private void handleCancel() {

    }

    private void handleSelect() {
        //TODO create order line
    }

    private void moveSelection(int delta) {
        list.setSelectedIndex(NormalizeIndex(list.getSelectedIndex() + delta));
    }

    private int NormalizeIndex(int index) {
        if (index < 0) {
            return 0;
        }
        return index >= list.getItems().size ? list.getItems().size : index;
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
