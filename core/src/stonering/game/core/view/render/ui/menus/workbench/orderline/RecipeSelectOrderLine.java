package stonering.game.core.view.render.ui.menus.workbench.orderline;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.local.crafting.ItemOrder;
import stonering.entity.local.items.Item;
import stonering.enums.items.ItemTypeMap;
import stonering.enums.items.Recipe;
import stonering.game.core.GameMvc;
import stonering.game.core.model.lists.ItemContainer;
import stonering.game.core.view.render.ui.lists.NavigableList;
import stonering.game.core.view.render.ui.lists.NavigableSelectBox;
import stonering.game.core.view.render.ui.menus.util.Invokable;
import stonering.game.core.view.render.ui.menus.workbench.WorkbenchMenu;
import stonering.utils.global.Pair;
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
public class RecipeSelectOrderLine extends Table implements Invokable {
    private GameMvc gameMvc;
    private WorkbenchMenu menu;

    private NavigableList itemTypeList;

    /**
     * Creates line with empty order and puts all possible recipes into initial selection list.
     */
    public RecipeSelectOrderLine(GameMvc gameMvc, WorkbenchMenu menu) {
        this.gameMvc = gameMvc;
        this.menu = menu;
        createRecipeSelectList(new ArrayList<>(menu.getWorkbenchAspect().getRecipes()));
    }

    /**
     * Creates line with list of all workbench recipes.
     */
    public void createRecipeSelectList(ArrayList<Recipe> recipeList) {
        Map<String, Recipe> recipeMap = new HashMap<>();
        recipeList.forEach(recipe -> recipeMap.put(recipe.getTitle(), recipe));
        itemTypeList = new NavigableList();
        itemTypeList.setItems(recipeMap.keySet().toArray(new String[]{}));
        itemTypeList.setSelectListener(event -> { // hides list and creates empty order for recipe
            if (itemTypeList.getSelectedIndex() >= 0) {
                String selected = (String) itemTypeList.getItems().get(itemTypeList.getSelectedIndex());
                itemTypeList.hide();
//                menu.
                menu.createOrderLineForRecipe(recipeMap.get(selected));
            }
            return true;
        });
        itemTypeList.setShowListener(event -> { // adds list to table
            this.add(itemTypeList).left().expandX();
            return true;
        });
        itemTypeList.setHideListener(event -> { // removes list from table
            this.removeActor(itemTypeList);
            itemTypeList = null;
            return true;
        });
        itemTypeList.show();
    }

    @Override
    public boolean invoke(int keycode) {
        return itemTypeList.invoke(keycode);
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
}
