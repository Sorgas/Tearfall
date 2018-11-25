package stonering.game.core.view.render.ui.menus.workbench;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.local.crafting.ItemOrder;
import stonering.enums.items.Recipe;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.menus.util.Invokable;
import stonering.utils.global.StaticSkin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Single line in workbench menu.
 *
 * @author Alexander on 28.10.2018.
 */
public class CraftingOrderLine extends Table implements Invokable {
    private GameMvc gameMvc;
    private WorkbenchMenu menu;

    private Map<String, Recipe> recipeMap;
    private boolean repeatable;
    private ItemOrder itemOrder;

    private Label itemType;
    private SelectBox<String> material;

    private com.badlogic.gdx.scenes.scene2d.ui.List list;

    /**
     * Creates line by given order.
     */
    public CraftingOrderLine(GameMvc gameMvc, WorkbenchMenu menu, ItemOrder order) {
        super(StaticSkin.getSkin());
        this.gameMvc = gameMvc;
        this.menu = menu;
    }

    /**
     * Creates line with empty order and puts all possible recipes into initial selection list.
     */
    public CraftingOrderLine(GameMvc gameMvc, WorkbenchMenu menu, List<Recipe> recipes) {
        super(StaticSkin.getSkin());
        this.gameMvc = gameMvc;
        this.menu = menu;
        createRecipeSelectList(recipes);
    }

    /**
     * Creates list of all workbench recipe.
     */
    public void createRecipeSelectList(List<Recipe> recipeList) {
        recipeMap = new HashMap<>();
        list = new com.badlogic.gdx.scenes.scene2d.ui.List(StaticSkin.getSkin());
        recipeList.forEach(recipe -> {
            recipeMap.put(recipe.getName(), recipe);
        });
        list.setItems(recipeMap.keySet().toArray(new String[]{}));
        createTable();
    }

    /**
     * Creates selectBoxes for crafting steps from order.
     */
    private void createOrderLine() {
        itemType = new Label(itemOrder.getType().getName(), StaticSkin.getSkin());
        material = new SelectBox<>(StaticSkin.getSkin());
        gameMvc.getModel().getItemContainer().getResourceItemList());
        material.setItems();
        steps.add(new SelectBox<String>());
    }

    private void createTable() {
        this.add(list).left().expandX();
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

    @Override
    public boolean invoke(int keycode) {
        return false;
    }

    public ItemOrder getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(ItemOrder itemOrder) {
        this.itemOrder = itemOrder;
    }
}
