package stonering.desktop.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;

import stonering.entity.item.Item;
import stonering.enums.images.DrawableMap;
import stonering.generators.items.ItemGenerator;
import stonering.stage.UiStage;
import stonering.stage.workbench.recipelist.RecipeCategoryNode;
import stonering.util.geometry.Position;
import stonering.util.global.StaticSkin;
import stonering.util.ui.SimpleScreen;
import stonering.widget.BackgroundGenerator;
import stonering.widget.ButtonMenu;
import stonering.widget.NavigableVerticalGroup;
import stonering.widget.item.CheckableSingleItemSquareButton;
import stonering.widget.item.StackedItemSquareButton;

/**
 * Demo with some UI elements.
 *
 * @author Alexander on 19.02.2019.
 */
public class UiDemo extends Game {

    public static void main(String[] args) {
        new LwjglApplication(new UiDemo());
    }

    @Override
    public void create() {
        setScreen(new SimpleScreen() {
            private UiStage stage = new UiStage();

            {
                stage.interceptInput = false;
                stage.addActor(createContainer());
                stage.addListener(new InputListener() {
                    @Override
                    public boolean keyDown(InputEvent event, int keycode) {
                        if(keycode == Input.Keys.ESCAPE) Gdx.app.exit();
                        return true;
                    }
                });
                Gdx.input.setInputProcessor(stage);
                stage.setDebugAll(true);
            }

            @Override
            public void render(float delta) {
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
                stage.act(delta);
                stage.draw();
            }

            @Override
            public void resize(int width, int height) {
                stage.resize(width, height);
            }
        });
    }

    private Container createContainer() {
        RecipeCategoryNode node = new RecipeCategoryNode("Dishes");
        Container container = new Container<>(filledTree());
        container.setFillParent(true);
        return container;
    }

    private Tree filledTree() {
        Tree tree = new Tree(StaticSkin.getSkin());
        for (int i = 0; i < 5; i++) {
            RecipeCategoryNode recipe = new RecipeCategoryNode("Dishes");
            Label label = new Label("category " + (i + 1), StaticSkin.getSkin());
            tree.add(recipe);
            for (int j = 0; j < 5; j++) {
                recipe.add(new RecipeCategoryNode("Dishes"));
            }
        }
        return tree;
    }
}
