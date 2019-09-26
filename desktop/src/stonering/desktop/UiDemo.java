package stonering.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import stonering.entity.item.Item;
import stonering.enums.items.type.ItemType;
import stonering.enums.items.type.raw.RawItemType;
import stonering.enums.materials.MaterialMap;
import stonering.stage.UiStage;
import stonering.stage.toolbar.menus.ToolbarButton;
import stonering.screen.SimpleScreen;
import stonering.util.global.StaticSkin;
import stonering.widget.lists.ItemCardButton;

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
        Screen screen = new ScreenMock();
        setScreen(screen);
    }

    private class ScreenMock extends SimpleScreen {
        private UiStage stage;
        private Container container;
        private Table table;

        public ScreenMock() {
            stage = new UiStage();
            container = createContainer();
            stage.addActor(container);
            Gdx.input.setInputProcessor(stage);
        }

        @Override
        public void render(float delta) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
            stage.act(delta);
            stage.getViewport().apply();
            stage.draw();
        }

        @Override
        public void resize(int width, int height) {
            stage.resize(width, height);
        }

        private Container createContainer() {
            ItemCardButton card = new ItemCardButton(createItem(), 3);
            table = new Table();
            Container container = new Container(table);
            container.align(Align.bottomLeft);
            table.add(new ToolbarButton("qwer1")).left().top().expand(true, true).fill().size(200, 200);
            table.add(new TextButton("qwer2", StaticSkin.getSkin())).right().top().expand(true, true);
            table.add(card);
            container.setDebug(true, true);
            return container;
        }
    }

    private Item createItem() {
        RawItemType rawType = new RawItemType();
        rawType.name = "chunk";
        rawType.title = "chunk";
        rawType.atlasXY = new int[]{1,1};
        ItemType type = new ItemType(rawType);
        Item item = new Item(null, type);
        item.setMaterial(MaterialMap.instance().getMaterial("meat").getId());
        item.setOrigin("deer");
        return item;
    }
}
