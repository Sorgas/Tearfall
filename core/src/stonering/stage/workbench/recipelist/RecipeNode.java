package stonering.stage.workbench.recipelist;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;

import stonering.enums.items.recipe.Recipe;
import stonering.util.global.StaticSkin;
import stonering.widget.BackgroundGenerator;

/**
 * Node of single recipe in {@link RecipeListSection}.
 * @author Alexander on 25.04.2020
 */
public class RecipeNode extends Tree.Node {
    private final int NODE_WIDTH = 260;
    private static final String HINT_TEXT = "WS: navigate recipes ED: add order A: to category Q: to orders";
    public final Recipe recipe;
    private RecipeListSection recipeListSection;

    public RecipeNode(Recipe recipe, RecipeListSection recipeListSection) {
        super(new Table());
        Table table = (Table) getActor();
        Label label = new Label(recipe.title, StaticSkin.getSkin());
        table.add(label).size(NODE_WIDTH, 50).padLeft(20);
        table.setBackground(new BackgroundGenerator().generate(1,1,1,1));
        // TODO add icons and amount of required item types
        this.recipeListSection = recipeListSection;
        table.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                recipeListSection.createNewOrder(recipe);
                return true;
            }
        });
        this.recipe = recipe;
    }
}
