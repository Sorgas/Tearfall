package stonering.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import stonering.desktop.sidebar.Sidebar;
import stonering.entity.item.Item;
import stonering.enums.items.type.ItemType;
import stonering.enums.items.type.raw.RawItemType;
import stonering.enums.materials.MaterialMap;
import stonering.stage.UiStage;
import stonering.screen.SimpleScreen;
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
        Screen screen = new SimpleScreen();
        setScreen(new SimpleScreen() {
            private UiStage stage;
            {
                stage = new UiStage();
                stage.addActor(createContainer());
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
        });
    }

    private Container createContainer() {
        Container fillContainer = new Container<>(new Sidebar<>(createList(), Align.right, 0.8f));
        fillContainer.setDebug(true, true);
        fillContainer.setFillParent(true);
        fillContainer.align(Align.right);
        return fillContainer;
    }

    private ScrollPane createList() {
        Table table = new Table();
        for (int i = 0; i < 20; i++) {
            table.add(new ItemCardButton(createItem(), 3)).pad(5).row();
        }
        ScrollPane pane = new ScrollPane(table);
        pane.setScrollingDisabled(true, false);
        return pane;
    }

    private Item createItem() {
        RawItemType rawType = new RawItemType();
        rawType.name = "chunk";
        rawType.title = "chunk";
        rawType.atlasXY = new int[]{12,0};
        ItemType type = new ItemType(rawType);
        Item item = new Item(null, type);
        item.setMaterial(MaterialMap.instance().getMaterial("meat").id);
        item.setOrigin("deer");
        return item;
    }
}
