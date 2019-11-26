package stonering.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
        private boolean panStarted;
        private boolean vertical;

        public ScreenMock() {
            stage = new UiStage();
            stage.addActor(createContainer());
//            stage.addActor(createGroupContainer());
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
            ScrollPane pane = new ScrollPane(new Table());
            Table table2 = (Table) pane.getActor();
            EventListener listener = new ActorGestureListener() {
                @Override
                public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                    if(!panStarted) {
                        panStarted = true;
                        vertical = Math.abs(deltaY) > Math.abs(deltaX);
                        System.out.println(deltaX + " " + deltaY);
                    }
                    System.out.println(vertical ? "vertical" : "horizontal");
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    panStarted = false;
                    System.out.println("up");
                }
            };
            pane.setDebug(true, true);
            for (int i = 0; i < 100; i++) {
                ItemCardButton button = new ItemCardButton(createItem(), 3);
                table2.add(button).pad(5).row();
                button.addListener(listener);
            }
            Container container = new Container(pane);
            container.align(Align.right);
            pane.addListener(listener);
            container.setDebug(true, true);
            container.setFillParent(true);
            return container;
        }

        private Container createGroupContainer() {
            ButtonGroup<CheckBox> group = new ButtonGroup<>();
            Table table = new Table();
            Container<Table> container = new Container<>(table);
            for (int i = 0; i < 5; i++) {
                CheckBox checkBox = new CheckBox("box " + i, StaticSkin.getSkin());
                table.add(checkBox);
                group.add(checkBox);
            }
            container.align(Align.left);
            container.setDebug(true, true);
            container.setFillParent(true);
            return container;
        }
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
