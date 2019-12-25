package stonering.desktop.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import stonering.util.ui.SimpleScreen;
import stonering.util.global.StaticSkin;

/**
 * @author Alexander on 07.11.2019.
 */
public class GestureDemo extends Game {

    public static void main(String[] args) {
        new LwjglApplication(new GestureDemo());
    }

    @Override
    public void create() {
        this.setScreen(new DemoScreen());
    }

    private static class DemoScreen extends SimpleScreen {
        private Stage stage;

        public DemoScreen() {
            this.stage = new Stage();
            Table table = new Table();

            Label label = new Label("Gestures detected", StaticSkin.getSkin());
            label.setAlignment(Align.center);
            label.addListener(new DemoListener());
            table.add(label).expand().fill();


            label = new Label("Gestures not detected", StaticSkin.getSkin());
            label.setAlignment(Align.center);
            table.add(label).expand().fill();

            table.align(Align.bottomLeft);
            table.setFillParent(true);
            table.setDebug(true, true);
            table.setColor(Color.GRAY);

            stage.addActor(table);
            Gdx.input.setInputProcessor(stage);
        }

        @Override
        public void render(float delta) {
            stage.draw();
        }
    }

    private static class DemoListener extends ActorGestureListener {
        private boolean panStarted;
        private boolean vertical;

        @Override
        public void touchDown(InputEvent event, float x, float y, int pointer, int button) {

        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            panStarted = false;
        }

        @Override
        public void tap(InputEvent event, float x, float y, int count, int button) {
            System.out.println("tap");
        }

        @Override
        public boolean longPress(Actor actor, float x, float y) {
            System.out.println("long press");
            return true;
        }

        @Override
        public void fling(InputEvent event, float velocityX, float velocityY, int button) {
        }

        @Override
        public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
            if(!panStarted) {
                panStarted = true;
                vertical = Math.abs(deltaY) > Math.abs(deltaX);
            }
            if(vertical) {
                deltaX = 0;
            } else {
                deltaY = 0;
            }
            System.out.println(vertical ? "vertical" : "horizontal");
        }

        @Override
        public void zoom(InputEvent event, float initialDistance, float distance) {
            System.out.println("zoom");
        }

        @Override
        public void pinch(InputEvent event, Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            System.out.println("pinch");
        }
    }
}
