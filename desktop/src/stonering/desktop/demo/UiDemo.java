package stonering.desktop.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import stonering.entity.item.Item;
import stonering.enums.images.DrawableMap;
import stonering.generators.items.ItemGenerator;
import stonering.stage.UiStage;
import stonering.util.global.StaticSkin;
import stonering.util.ui.SimpleScreen;
import stonering.widget.item.SingleItemSquareButton;
import stonering.widget.util.WrappedLabel;

/**
 * Demo with some UI elements.
 * TODO make tree with expanding on hover
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
                Container<Tree> container = createContainer3();
                stage.addActor(container);
                stage.addListener(new InputListener() {
                    @Override
                    public boolean keyDown(InputEvent event, int keycode) {
                        if (keycode == Input.Keys.ESCAPE) Gdx.app.exit();
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
        Container container = new Container();
        Image image = new Image();
        image.setDrawable(DrawableMap.ICON.getDrawable("roast_mealqwer"));
        Item item = new ItemGenerator().generateItem("pickaxe", "iron", null);
        SingleItemSquareButton button = new SingleItemSquareButton(item, StaticSkin.getColorDrawable(StaticSkin.background));
        container.setActor(button);
        container.size(200, 200);
        container.setFillParent(true);
        return container;
    }

    private Container<Tree> createContainer2() {
        Tree tree = createTree();
        tree.setIndentSpacing(40);
        tree.setIconSpacing(0,0);
        tree.setPadding(0);
        Container container = new Container(tree);
        container.align(Align.topLeft);
        container.setFillParent(true);
        return container;
    }

    private Container createContainer3() {
        HorizontalGroup group = new HorizontalGroup();
        WrappedLabel label = new WrappedLabel("qwer");
        label.size(100);
        group.addActor(label);
        WrappedLabel label2 = new WrappedLabel("qwer");
        label2.size(200);
        group.addActor(label2);
        WrappedLabel label3 = new WrappedLabel("qwer");
        label3.size(300);
        group.addActor(label3);
        Container container = new Container(group);
        container.align(Align.topLeft);
        container.setFillParent(true);
        return container;
    }

    private Tree createTree() {
        Tree<Tree.Node, String> tree = new Tree(StaticSkin.getSkin());
        tree.getStyle().plus.setMinWidth(30);
        tree.getStyle().plus.setMinHeight(30);
        tree.getStyle().minus.setMinWidth(30);
        tree.getStyle().minus.setMinHeight(30);
        tree.getStyle().background = DrawableMap.REGION.getDrawable("default");
        tree.setPadding(5);
        Tree.Node node = null;
        for (int i = 0; i < 5; i++) {
            Label label = new Label("category " + (i + 1), StaticSkin.getSkin());
            node = new Tree.Node<>(label) {};
            tree.add(node);
            Tree.Node node2 = null;
            for (int j = 0; j < 5; j++) {
                Label label2 = new Label("item " + (j + 1), StaticSkin.getSkin());
                Container<Label> container = new Container<>(label2);
                container.width(200);
                node2 = new Tree.Node(container) {};
                node.add(node2);
            }
            for (int k = 0; k < 5; k++) {
                Label label3 = new Label("item " + (k + 1), StaticSkin.getSkin());
                Container<Label> container2 = new Container<>(label3);
                container2.width(200);
                Tree.Node node3 = new Tree.Node(container2) {};
                node2.add(node3);
            }
        }
        return tree;
    }
}
