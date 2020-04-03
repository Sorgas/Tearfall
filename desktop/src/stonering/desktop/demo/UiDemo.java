package stonering.desktop.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
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
import stonering.util.geometry.Position;
import stonering.util.global.StaticSkin;
import stonering.util.ui.SimpleScreen;
import stonering.widget.NavigableVerticalGroup;
import stonering.widget.item.CheckableSingleItemSquareButton;
import stonering.widget.item.StackedItemSquareButton;

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

        Table table = new Table();
        for (int i = 0; i < 4; i++) {
            Table table2 = new Table();
            table2.add(new Label("qwer1123", StaticSkin.getSkin()));
            table2.add(new Label("qwer2", StaticSkin.getSkin())).expandX();
            table2.add(new Label("qwer3", StaticSkin.getSkin()));
            Container<Table> tableContainer = new Container<>(table2).fill();
//            tableContainer.size(600, 100);
            tableContainer.debugAll();
        }

        table.add(new Label("qwer", StaticSkin.getSkin()));
        table.add(new Label("123", StaticSkin.getSkin()));
        CheckBox checkBox = new CheckBox("", StaticSkin.getSkin());
        table.add(checkBox).expandX().right();
        Container<Table> container = new Container<>(table);
        container.width(200).fillX();
        container.setFillParent(true);
        return container;
    }
}
