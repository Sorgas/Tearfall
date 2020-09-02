package stonering.stage.entity_menu.building.workbench.recipelist;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import stonering.enums.items.recipe.Recipe;
import stonering.util.lang.StaticSkin;

/**
 * Node of single recipe in {@link RecipeTreeSection}.
 * TODO add icons and amount of required item types *
 * @author Alexander on 25.04.2020
 */
public class RecipeNode extends Tree.Node {
    private static final String HINT_TEXT = "WS: navigate recipes ED: add order A: to category Q: to orders";
    private Container<Table> container;
    private Table table;
    public final Recipe recipe;
    private RecipeTreeSection recipeTreeSection;
    private TextButton addButton;
    private Drawable backgroundIdle;
    private Drawable backgroundFocused;

    public RecipeNode(Recipe recipe, RecipeTreeSection recipeTreeSection, float width) {
        super(new Container<Table>());
        this.recipe = recipe;
        this.recipeTreeSection = recipeTreeSection;
        createLayout(width);
        createListeners();
    }

    private void createLayout(float width) {
        container = (Container<Table>) getActor();
        Table table = new Table();
        Label label = new Label(recipe.title, StaticSkin.getSkin());
        addButton = new TextButton(">", StaticSkin.getSkin());
        table.add(label).padLeft(20).expand();
        table.add(addButton).width(42).pad(4).expandY().fill();
//        backgroundFocused = StaticSkin.generator.generate(StaticSkin.)
//        backgroundIdle = StaticSkin.generator.generate(StaticSkin.)

        table.setBackground(backgroundFocused);
        container.size(width, 50);
        container.setActor(table);
    }

    private void createListeners() {
        addButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                recipeTreeSection.createNewOrder(recipe);
            }
        });
        container.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {

            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
            }
        });
    }
}
