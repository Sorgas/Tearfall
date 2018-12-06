package stonering.game.core.view.render.ui.menus.workbench.orderline;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.enums.items.Recipe;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.lists.NavigableList;
import stonering.game.core.view.render.ui.menus.util.HideableComponent;
import stonering.game.core.view.render.ui.menus.workbench.WorkbenchMenu;
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
public class RecipeSelectOrderLine extends Table implements HideableComponent {
    private WorkbenchMenu menu;

    private Label statusLabel;
    private NavigableList itemTypeList;

    /**
     * Creates line with empty order and puts all possible recipes into initial selection list.
     */
    public RecipeSelectOrderLine(GameMvc gameMvc, WorkbenchMenu menu) {
        this.menu = menu;
        setDebug(true, true);
        this.left();
        this.pad(0,5,0,0);
        this.defaults().padRight(5);
        createStatusLabel();
        createRecipeSelectList(new ArrayList<>(menu.getWorkbenchAspect().getRecipes()));
    }


    @Override
    /**
     * Shows this line in menu and focuses it.
     */
    public void show() {
        menu.getOrderList().addActorAt(0, this);
        getStage().setKeyboardFocus(itemTypeList);
    }

    /**
     * Hides this line from menu.
     */
    public void hide() {
        menu.getOrderList().removeActor(this);
        menu.getStage().setKeyboardFocus(menu.getOrderList().hasChildren() ? menu.getOrderList() : menu);
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
                String selected = (String) itemTypeList.getItems().get(itemTypeList.getSelectedIndex());
                hide();
                menu.createOrderLineForRecipe(recipeMap.get(selected));
            }
            return true;
        });
        itemTypeList.setHideListener(event -> {                                                 // removes order
            event.stop();
            hide();
            return true;
        });
        add(itemTypeList).left().top().expandX();
        itemTypeList.show();
    }

    /**
     * Creates order status label.
     */
    private void createStatusLabel() {
        statusLabel = new Label("new", StaticSkin.getSkin());
        this.add(statusLabel).top();
    }
}
