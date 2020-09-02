package stonering.stage.entity_menu.building.workbench.recipelist;

import static stonering.enums.ControlActionsEnum.CANCEL;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.enums.ControlActionsEnum;
import stonering.enums.items.recipe.Recipe;
import stonering.stage.entity_menu.building.MenuSection;
import stonering.stage.entity_menu.building.workbench.WorkbenchMenu;
import stonering.util.logging.Logger;
import stonering.util.lang.StaticSkin;
import stonering.widget.NavigableTree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shows list of workbench recipes.
 * Creates new orders.
 * <p>
 * Controls:
 * WASD: navigation in tree.
 * E: expand category / create new order.
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
        createTree();
        createListeners();
    }

    /**
     * Creates buttons for categories. These cannot be hidden.
     */
    private void createTree() {
        recipeTree = new NavigableTree<>(StaticSkin.getSkin());
        recipeTree.getStyle().plus.setMinHeight(50);
        recipeTree.getStyle().plus.setMinWidth(50);
        recipeTree.getStyle().minus.setMinHeight(50);
        recipeTree.getStyle().minus.setMinWidth(50);
        float categoryNodeWidth = sectionWidth - recipeTree.getStyle().plus.getMinWidth();
        float recipeNodeWidth = categoryNodeWidth - recipeTree.getIndentSpacing();
        recipeMap.keySet().stream()
                .map(category -> new RecipeCategoryNode(category, categoryNodeWidth))
                .peek(recipeTree::add)
                .forEach(categoryNode -> {
                    recipeMap.get(categoryNode.category).stream()
                            .map(recipe -> new RecipeNode(recipe, this, recipeNodeWidth))
                            .forEach(categoryNode::add);
                });
        recipeTree.setSelectionConsumer(node -> {
            if (node instanceof RecipeCategoryNode) {
                RecipeCategoryNode categoryNode = (RecipeCategoryNode) node;
                categoryNode.setExpanded(!categoryNode.isExpanded()); // expand/collapse categories
            } else if (node instanceof RecipeNode) {
                createNewOrder(((RecipeNode) node).recipe);
            } else {
                Logger.UI.logError("Invalid node type in recipe tree.");
            }
        });
        this.add(recipeTree).left().maxWidth(300);
    }

    private void createListeners() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                ControlActionsEnum action = ControlActionsEnum.getAction(keycode);
                if (action == CANCEL) {
                    menu.setFocus(menu.orderListSection); // go to order list
                } else {
                    recipeTree.accept(action);
                }
                return true;
            }
        });
    }

    public void createNewOrder(Recipe recipe) {
        recipeTree.setOverNode(null);
        menu.orderListSection.createOrder(recipe);
    }

    public String getHint() {
        return "WASD: navigate, E: create order, Q: to order list.";
    }
}
