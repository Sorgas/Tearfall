package stonering.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import stonering.desktop.sidebar.Sidebar;
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
        setScreen(new SimpleScreen() {
            private UiStage stage1 = new UiStage();
            private UiStage stage2 = new UiStage();

            {
                stage1.interceptInput = false;
                stage2.interceptInput = false;
                stage1.addActor(createLabelContainer());
                stage2.addActor(createButtonContainer());
                Gdx.input.setInputProcessor(new InputMultiplexer(stage1, stage2));
            }

            @Override
            public void render(float delta) {
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
                stage2.act(delta);
                stage2.draw();
                stage1.act(delta);
                stage1.draw();
            }

            @Override
            public void resize(int width, int height) {
                stage1.resize(width, height);
                stage2.resize(width, height);
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

    private Container createButtonContainer() {
        TextButton button = new TextButton("qwer", StaticSkin.getSkin());
        button.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("qwer");
                return false;
            }
        });
        Container container = new Container<>(button);
        container.align(Align.right).size(300, 300).setFillParent(true);
        return container;
    }

    private Container createLabelContainer() {
        Label label = new Label("asdf", StaticSkin.getSkin());
        label.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("asdf");
                return false;
            }
        });
        Container container = new Container<>(label);
        container.align(Align.right).size(200, 200).setFillParent(true);
        container.setDebug(true, true);
        return container;
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
