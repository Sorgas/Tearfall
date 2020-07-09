package stonering.desktop.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import stonering.enums.images.DrawableMap;
import stonering.stage.util.UiStage;
import stonering.screen.util.SimpleScreen;
import stonering.widget.button.IconTextHotkeyButton;

/**
 * Demo with some UI elements.
 * TODO make tree with expanding on hover
 *
 * @author Alexander on 19.02.2019.
 */
public class UiDemo extends Game {
    private Container container2;
    IconTextHotkeyButton button;

    public static void main(String[] args) {
        new LwjglApplication(new UiDemo());
    }

    @Override
    public void create() {
        setScreen(new SimpleScreen() {
            private UiStage stage = new UiStage();

            {
                stage.interceptInput = false;
                Container container = createContainer();
                stage.addActor(container);
                stage.addListener(new InputListener() {
                    @Override
                    public boolean keyDown(InputEvent event, int keycode) {
                        if (keycode == Input.Keys.ESCAPE) {
                            Gdx.app.exit();
                        } else {
                            System.out.println(button);
                            System.out.println(button.imageCell.getPrefWidth() + " " + button.imageCell.getPrefHeight());
                            System.out.println(button.labelCell.getPrefWidth() + " " + button.labelCell.getPrefHeight());
                            System.out.println(button.getPrefWidth() + " " + button.getPrefHeight());
                            System.out.println(button.getWidth() + " " + button.getHeight());
                            container.width(container.getWidth() + 20);
                        }
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
        Table table = new Table();
        table.defaults().height(40);
        button = new IconTextHotkeyButton(DrawableMap.ICON.getDrawable("plants_menu"), "qwerqwer", "1234");
//        button.setSize(button.getPrefWidth(), button.getPrefHeight());
        table.add(button).row();
        Container container = new Container(table);
        container.setFillParent(true);
//        container.fill();
        container.setDebug(true, true);
        return container;
    }
}
