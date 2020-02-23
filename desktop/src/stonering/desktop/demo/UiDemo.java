package stonering.desktop.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import stonering.entity.item.Item;
import stonering.enums.images.DrawableMap;
import stonering.enums.items.type.ItemTypeMap;
import stonering.stage.UiStage;
import stonering.util.geometry.Position;
import stonering.util.global.StaticSkin;
import stonering.util.ui.SimpleScreen;
import stonering.widget.item.StackedItemSquareButton;

import java.awt.event.WindowEvent;

/**
 * Demo with some UI elements.
 *
 * @author Alexander on 19.02.2019.
 */
public class UiDemo extends Game {
    Table table;
    StackedItemSquareButton button;

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
        Container container = new Container();
        container.setFillParent(true);
        Item item = new Item(new Position(), ItemTypeMap.instance().getItemType("pickaxe"));
        button = new StackedItemSquareButton(item);
        button.pad(30);
        container.pad(50);
        container.setActor(button);
        return container;
    }
}
