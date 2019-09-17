package stonering.stage.workbench.recipelist;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.enums.ControlActionsEnum;
import stonering.enums.items.recipe.Recipe;
import stonering.stage.workbench.WorkbenchMenu;
import stonering.stage.workbench.orderlist.OrderListSection;
import stonering.enums.images.DrawableMap;
import stonering.widget.Highlightable;
import stonering.widget.NavigableVerticalGroup;
import stonering.widget.util.WrappedTextButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shows recipes divided into categories.
 * Manages and toggles categories and recipes buttons.
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
public class RecipeListSection extends NavigableVerticalGroup implements Highlightable {
    private Map<String, List<String>> recipeMap;
    public final WorkbenchMenu menu;

    public RecipeListSection(WorkbenchAspect aspect, WorkbenchMenu menu) {
        this.menu = menu;
        fillCategoryMap(aspect);
        createCategoryItems();
        createListeners();
        keyMapping.put(Input.Keys.D, ControlActionsEnum.SELECT);
        align(Align.topLeft);
    }

    /**
     * Collects recipes from flat list of aspect and maps them to categories in a {@link HashMap}
     */
    private void fillCategoryMap(WorkbenchAspect aspect) {
        recipeMap = new HashMap<>();
        for (Recipe recipe : aspect.getRecipes()) {
            if (!recipeMap.containsKey(recipe.category)) recipeMap.put(recipe.category, new ArrayList<>());
            recipeMap.get(recipe.category).add(recipe.name);
        }
    }

    /**
     * Creates buttons for categories. These cannot be hidden.
     */
    private void createCategoryItems() {
        for (String categoryName : recipeMap.keySet()) {
            addActor(new RecipeCategoryItem(categoryName, this, recipeMap.get(categoryName)));
        }
    }

    public void updateCategory(RecipeCategoryItem category) {
        if (!getChildren().contains(category, true)) return;
        if (category.isExpanded()) {
            for (RecipeItem recipeItem : category.getRecipeItems()) {
                addActorAfter(category, recipeItem);
            }
            setSelectedIndex(getChildren().indexOf(category, true) + 1);
        } else {
            for (RecipeItem recipeItem : category.getRecipeItems()) {
                removeActor(recipeItem);
            }
            setSelectedIndex(getChildren().indexOf(category, true));
        }
    }

    /**
     * Unselects items in this list and calls {@link OrderListSection} to create order.
     */
    public void createNewOrder(Recipe recipe) {
        setSelectedIndex(-1);
        menu.orderListSection.createOrder(recipe);
    }

    private void createListeners() {
        setSelectListener(event -> { // toggles categories and recipes buttons
                    if(getSelectedElement() != null) ((WrappedTextButton) getSelectedElement()).toggle();
                    return true;
                }
        );
        setCancelListener(event -> getStage().setKeyboardFocus(menu.orderListSection)); // quits to order list from any item
        addListener(new InputListener() { // for collapsing
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                return keycode == Input.Keys.A && handleCollapse();
            }
        });
        setHighlightHandler(new CheckHighlightHandler(this) {

            @Override
            public void handle(boolean value) {
                menu.recipesHeader.setBackground(DrawableMap.instance().getDrawable("workbench_order_line" +
                        (value ? ":focused" : "")));
            }
        });
    }

    /**
     * Collapses selected actor with behaviour:
     */
    private boolean handleCollapse() {
        Actor selected = getSelectedElement();
        if (selected instanceof RecipeCategoryItem) {
            RecipeCategoryItem category = (RecipeCategoryItem) selected;
            if (category.isExpanded()) { // collapse expanded category
                category.update(false);
            } else { // collapse all categories if selected one is collapsed
                for (Actor child : getChildren()) {
                    if (child instanceof RecipeCategoryItem) {
                        ((RecipeCategoryItem) child).update(false);
                    }
                }
                setSelectedIndex(getChildren().indexOf(selected, true));
            }
        } else if (selected instanceof RecipeItem) { // go to category
            setSelectedIndex(getChildren().indexOf(((RecipeItem) selected).category, true));
        }
        return true;
    }

    @Override
    public void setSelectedIndex(int newIndex) {
        super.setSelectedIndex(newIndex);
        menu.orderDetailsSection.showItem(getSelectedElement());
    }
}
