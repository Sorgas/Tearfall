package stonering.game.view.render.ui.menus.workbench.newmenu.recipelist;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.items.recipe.Recipe;

/**
 * Item for recipe in the {@link RecipeList}.
 * Keys input is handled in recipe list. Clicked as normal button.
 *
 * @author Alexander on 12.08.2019.
 */
public class RecipeItem extends TextButton {
    private Recipe recipe;

    public RecipeItem(Recipe recipe, Skin skin) {
        super(recipe.title, skin);
        this.recipe = recipe;
        createDefaultListener();
    }

    private void createDefaultListener() {
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //TODO
            }
        });
    }
}
