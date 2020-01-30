package stonering.desktop.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import stonering.enums.images.DrawableMap;
import stonering.stage.UiStage;
import stonering.util.global.StaticSkin;
import stonering.util.ui.SimpleScreen;

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
                Gdx.input.setInputProcessor(stage);
            }

            @Override
            public void render(float delta) {
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
//                pane.scrollTo(0, 0, 0,100);
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
        Container<Image> container = new Container();
        container.setFillParent(true);
        Drawable drawable = DrawableMap.getTextureDrawable("ui/item_slot.png");
        drawable.setMinHeight(200);
        drawable.setMinWidth(200);
        Image image = new Image(drawable);
//        image.setSize(200, 200);
//        image.invalidateHierarchy();
//        image.sizeBy(100);
//        image.setScale(3);
//        image.pack();

        container.setActor(image);
//        container.size(200, 300);
        container.setDebug(true, true);
        return container;
    }
}
