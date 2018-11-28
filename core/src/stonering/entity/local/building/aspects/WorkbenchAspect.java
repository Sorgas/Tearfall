package stonering.entity.local.building.aspects;

import stonering.entity.local.Aspect;
import stonering.entity.local.AspectHolder;
import stonering.entity.local.building.Building;
import stonering.entity.local.crafting.ItemOrder;
import stonering.enums.items.Recipe;
import stonering.enums.items.RecipeMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Aspect for workbenches. Manages orders of workbench.
 *
 * @author Alexander on 01.11.2018.
 */
public class WorkbenchAspect extends Aspect {
    public static String NAME = "workbench";
    private List<Recipe> recipes;
    private List<ItemOrder> orders;

    public WorkbenchAspect(AspectHolder aspectHolder) {
        super(NAME, aspectHolder);
        orders = new ArrayList<>();
        recipes = new ArrayList<>();
        initRecipes();
    }

    private void initRecipes() { //ok
        ((Building) aspectHolder).getType().getRecipes().forEach(s -> recipes.add(RecipeMap.getInstance().getRecipe(s)));
    }

    public List<ItemOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<ItemOrder> orders) {
        this.orders = orders;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
