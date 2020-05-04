package stonering.stage.workbench.recipelist;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;

import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.enums.ControlActionsEnum;
import stonering.enums.items.recipe.Recipe;
import stonering.stage.workbench.MenuSection;
import stonering.stage.workbench.WorkbenchMenu;
import stonering.util.global.StaticSkin;
import stonering.widget.NavigableTree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shows list of workbench recipes.
 * Creates new orders.
 * <p>
 * Controls:
 * W, S: navigation in tree.
 * E, D: expand category, create new order.
 * A: collapse {@see handleCollapse}.
 * Q: quit to order list.
 *
 * @author Alexander on 12.08.2019.
 */
public class RecipeTreeSection extends MenuSection {
    private static final float sectionWidth = 300;
    private NavigableTree recipeTree;
    private Map<String, List<Recipe>> recipeMap; // category name to recipe names
    public final WorkbenchMenu menu;

    public RecipeTreeSection(String title, WorkbenchAspect aspect, WorkbenchMenu menu) {
        super(title);
        this.menu = menu;
        recipeMap = new HashMap<>();
        recipeMap.put("all", aspect.recipes); // TODO divide recipes into categories
        fillTree();
        createListeners();
    }

    /**
     * Creates buttons for categories. These cannot be hidden.
     */
    private void fillTree() {
        recipeTree = new NavigableTree(StaticSkin.getSkin());
        recipeTree.getStyle().plus.setMinHeight(50);
        recipeTree.getStyle().plus.setMinWidth(50);
        recipeTree.getStyle().minus.setMinHeight(50);
        recipeTree.getStyle().minus.setMinWidth(50);
        float categoryNodeWidth = sectionWidth - recipeTree.getStyle().plus.getMinWidth();
        float recipeNodeWidth = categoryNodeWidth - recipeTree.getIndentSpacing();
        for (String category : recipeMap.keySet()) {
            RecipeCategoryNode categoryNode = new RecipeCategoryNode(category, categoryNodeWidth);
            recipeTree.add(categoryNode);
            for (Recipe recipe : recipeMap.get(category)) {
                categoryNode.add(new RecipeNode(recipe, this, recipeNodeWidth));
            }
        }
        this.add(recipeTree);
    }

    private void createListeners() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (ControlActionsEnum.getAction(keycode)) {
                    case UP:
                    case DOWN:
                    case LEFT:
                    case RIGHT:
                    case SELECT:
                        recipeTree.accept(ControlActionsEnum.getAction(keycode));
                        return true;
                    case CANCEL:
                        getStage().setKeyboardFocus(menu.orderListSection); // go to order list
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    public void createNewOrder(Recipe recipe) {
        recipeTree.setOverNode(null);
        menu.orderListSection.createOrder(recipe);
    }

    private void navigateTree() {

    }
}
