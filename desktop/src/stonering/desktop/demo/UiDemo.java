package stonering.desktop.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import stonering.desktop.demo.sidebar.Sidebar;
import stonering.entity.item.Item;
import stonering.enums.items.type.ItemType;
import stonering.enums.items.type.raw.RawItemType;
import stonering.enums.materials.MaterialMap;
import stonering.stage.UiStage;
import stonering.util.global.StaticSkin;
import stonering.util.ui.SimpleScreen;
import stonering.widget.lists.ItemCardButton;

/**
 * Demo with some UI elements.
 *
 * @author Alexander on 19.02.2019.
 */
public class UiDemo extends Game {
    private ScrollPane pane;

    public static void main(String[] args) {
        new LwjglApplication(new UiDemo());
    }

    @Override
    public void create() {
        setScreen(new SimpleScreen() {
            private UiStage stage = new UiStage();

            {
                stage.interceptInput = false;
                stage.addActor(createLabelContainer());
                Gdx.input.setInputProcessor(stage);
            }

            @Override
            public void render(float delta) {
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
//                pane.scrollTo(0, 0, 0,100);
                pane.scrollTo(500, 0, 100, 0, true, false);
                stage.act(delta);
                stage.draw();

            }

            @Override
            public void resize(int width, int height) {
                stage.resize(width, height);
            }
        });
    }

    private Container createLabelContainer() {
        Container<ScrollPane> container = new Container(pane = new ScrollPane(createList()));
        container.size(300, 300);
        container.setFillParent(true);
        container.fill();
        container.align(Align.right);
        container.setDebug(true, true);
        return container;
    }

    private Container createList() {
        Table table = new Table();
        for (int i = 0; i < 20; i++) {

            table.add(new TextButton(String.valueOf(i), StaticSkin.getSkin())).size(50, 50).fillX();
        }
        ScrollPane pane = new ScrollPane(table);
        pane.setScrollingDisabled(true, false);
        table.debugAll();
        return new Container(pane);
    }
}
