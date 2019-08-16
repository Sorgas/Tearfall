package stonering.game.view.render.stages.workbench.newmenu.recipelist;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.crafting.ItemOrder;
import stonering.enums.ControlActionsEnum;
import stonering.enums.items.recipe.Recipe;
import stonering.game.view.render.ui.menus.util.NavigableVerticalGroup;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shows recipes divided into categories.
 * Manages and toggles categories and recipes buttons.
 * Creates new orders.
 *
 * @author Alexander on 12.08.2019.
 */
public class RecipeList extends NavigableVerticalGroup {
    private Map<String, List<String>> recipeMap;

    public RecipeList(WorkbenchAspect aspect) {
        fillCategoryMap(aspect);
        createCategoryItems();
        createListeners();
        keyMapping.put(Input.Keys.D, ControlActionsEnum.SELECT);

    }

    private void fillCategoryMap(WorkbenchAspect aspect) {
        recipeMap = new HashMap<>();
        for (Recipe recipe : aspect.getRecipes()) {
            if (!recipeMap.containsKey(recipe.category)) recipeMap.put(recipe.category, new ArrayList<>());
            recipeMap.get(recipe.category).add(recipe.name);
        }
    }

    /**
     * Cerates buttons for categories. These cannot be hidden.
     */
    private void createCategoryItems() {
        for (String categoryName : recipeMap.keySet()) {
            addActor(new RecipeCategoryItem(categoryName, this, recipeMap.get(categoryName)));
        }
    }

    /**
     * Shows and hides recipes of category.
     */
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
     * Creates new {@link ItemOrder} and adds it to order list of this workbench.
     */
    public void createNewOrder(String recipeName) {
        //TODO create new order, add to list, show in right pane
        setSelectedIndex(-1);
    }

    private void createListeners() {
        setSelectListener(new InputListener() { // toggles categories and recipes buttons
            @Override
            public boolean handle(Event e) {
                Actor actor = getSelectedElement();
                if (actor instanceof RecipeCategoryItem || actor instanceof RecipeItem) {
                    ((TextButton) actor).toggle();
                } else {
                    Logger.UI.logError("Recipe list contains invalid item " + actor.toString()); // invalid case
                }
                return true;
            }
        });
        setCancelListener(new InputListener() {
            @Override
            public boolean handle(Event e) { // quits to order list from any item
                //TODO
                return true;
            }
        });
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode != Input.Keys.A) return false;
                Actor actor = getSelectedElement();
                if (actor instanceof RecipeCategoryItem) { // collapse category
                    RecipeCategoryItem category = (RecipeCategoryItem) actor;
                    if (category.isExpanded()) category.toggle();
                } else if (actor instanceof RecipeItem) { // go to category
                    setSelectedIndex(getChildren().indexOf(((RecipeItem) actor).category, true));
                }
                return true;
            }
        });
    }

    @Override
    public boolean navigate(int delta) {
        //TODO update recipe/category preview
        return super.navigate(delta);
    }
}
