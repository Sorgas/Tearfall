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
public class RecipeTreeSection extends MenuSection {
    private static final float sectionWidth = 300;
    private Tree recipeTree;
    private Map<String, List<Recipe>> recipeMap; // category name to recipe names
    public final WorkbenchMenu menu;

    public RecipeTreeSection(String title, WorkbenchAspect aspect, WorkbenchMenu menu) {
        super(title);
        this.menu = menu;
        recipeMap = new HashMap<>();
        recipeMap.put("all", aspect.recipes); // TODO divide recipes into categories
        fillTree();
        // create listeners
        align(Align.topLeft);
    }

    /**
     * Creates buttons for categories. These cannot be hidden.
     */
    private void fillTree() {
        recipeTree = new Tree(StaticSkin.getSkin());
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

    public void createNewOrder(Recipe recipe) {
        recipeTree.setOverNode(null);
        menu.orderListSection.createOrder(recipe);
    }
}
