package stonering.game.view.render.stages.workbench.recipelist;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.items.recipe.Recipe;
import stonering.enums.items.recipe.RecipeMap;
import stonering.game.view.render.util.WrappedTextButton;

/**
 * Item for recipe in the {@link RecipeListSection}.
 * Keys input is handled in recipe list. Clicked as normal button.
 *
 * @author Alexander on 12.08.2019.
 */
public class RecipeItem extends WrappedTextButton {
    public final Recipe recipe;
    private RecipeListSection recipeListSection;
    public final RecipeCategoryItem category;

    public RecipeItem(Recipe recipe, RecipeListSection recipeListSection, RecipeCategoryItem category) {
        super(recipe.title);
        this.recipe = recipe;
        this.recipeListSection = recipeListSection;
        this.category = category;
        createDefaultListener();
        size(270, 35).pad(5).padLeft(25);
    }

    private void createDefaultListener() {
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                recipeListSection.createNewOrder(recipe);
            }
        });
    }
}
