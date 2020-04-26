package stonering.stage.workbench.recipelist;

import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.utils.Align;

import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.enums.items.recipe.Recipe;
import stonering.stage.workbench.MenuSection;
import stonering.stage.workbench.WorkbenchMenu;
import stonering.util.global.StaticSkin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shows list of workbench recipes.
 * Creates new orders.
 * <p>
 * Controls:
 * E, D: expand category, create new order.
 * A: collapse {@see handleCollapse}.
 * W, S: navigation.
 * Q: quit to order list.
 *
 * @author Alexander on 12.08.2019.
 */
public class RecipeListSection extends MenuSection {
    private Tree recipeTree;
    private Map<String, List<Recipe>> recipeMap; // category name to recipe names
    public final WorkbenchMenu menu;

    public RecipeListSection(WorkbenchAspect aspect, WorkbenchMenu menu) {
        this.menu = menu;
        recipeMap = new HashMap<>();
        recipeMap.put("all", aspect.recipes); // TODO divide recipes into categories
        fillTree();
        align(Align.topLeft);
    }

    /**
     * Creates buttons for categories. These cannot be hidden.
     */
    private void fillTree() {
        recipeTree = new Tree(StaticSkin.getSkin());
        for (String category : recipeMap.keySet()) {
            RecipeCategoryNode categoryNode = new RecipeCategoryNode(category);
            recipeTree.add(categoryNode);
            for (Recipe recipe : recipeMap.get(category)) {
                categoryNode.add(new RecipeNode(recipe, this));
            }
        }
        this.add(recipeTree);
    }

    public void createNewOrder(Recipe recipe) {
        recipeTree.setOverNode(null);
        menu.orderListSection.createOrder(recipe);
    }
}
