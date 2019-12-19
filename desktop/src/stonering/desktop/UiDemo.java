package stonering.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import stonering.entity.item.Item;
import stonering.enums.items.type.ItemType;
import stonering.enums.items.type.raw.RawItemType;
import stonering.enums.materials.MaterialMap;
import stonering.stage.UiStage;
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

        public ScreenMock() {
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

        private Container createContainer() {
            ScrollPane verticalPane = new ScrollPane(createTable());
            verticalPane.setScrollingDisabled(true, false);
            Container paneContainer = new Container<>(verticalPane);
            paneContainer.align(Align.right);
            paneContainer.addListener(createListener("pane"));
            paneContainer.maxWidth(250);

            Container wideContainer = new Container<>(paneContainer);
            wideContainer.minWidth(400);
            wideContainer.maxWidth(400);
            wideContainer.align(Align.right);
            wideContainer.addListener(createListener("wide"));

            ScrollPane horizontalPane = new ScrollPane(wideContainer);
            horizontalPane.setScrollingDisabled(false, true);
            horizontalPane.setOverscroll(false, false);
            horizontalPane.setFlickScroll(true);

            Container limitContainer = new Container<>(horizontalPane);
            limitContainer.maxWidth(250);
            limitContainer.align(Align.right);
            limitContainer.addListener(createListener("limit"));

            Container fillContainer = new Container<>(limitContainer);
            fillContainer.setDebug(true, true);
            fillContainer.setFillParent(true);
            fillContainer.align(Align.right);
            fillContainer.addListener(createListener("fill"));
            return fillContainer;
        }
    }

    private InputListener createListener(String qwer) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println(qwer);
                return false;
            }
        };
    }

    private Table createTable() {
        Table table2 = new Table();
        for (int i = 0; i < 20; i++) {
            ItemCardButton button = new ItemCardButton(createItem(), 3);
            table2.add(button).pad(5).row();
        }
        return table2;
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
