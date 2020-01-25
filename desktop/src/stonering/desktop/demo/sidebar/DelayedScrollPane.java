package stonering.desktop.demo.sidebar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

/**
 * Panes saves start position of touch and starts scrolling, only when pointer leaves specified box around start position.
 *
 * @author Alexander on 25.01.2020
 */
public class DelayedScrollPane extends ScrollPane {
    private int boxSize;
    private float dragStartX;
    private float dragStartY;
    private String direction;

    public DelayedScrollPane(Actor widget, int boxSize) {
        super(widget);
        this.boxSize = boxSize;
        createListeners();
        setSmoothScrolling(false);
    }

    private void createListeners() {
        addCaptureListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touch");
                dragStartX = x;
                dragStartY = y;
                super.touchDown(event, x, y, pointer, button);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                System.out.println((dragStartX - x) + " " + (dragStartY - y));
                if (Gdx.input.isTouched() && Math.abs(x - dragStartX) < boxSize && Math.abs(y - dragStartY) < boxSize) {
                    System.out.println("blocked");
                    event.stop();
                    return;
                }
                if ("".equals(direction)) {
                    if (Math.abs(x - dragStartX) > boxSize) {
                        System.out.println(direction = "horizontal");
                    }
                }
                if ("".equals(direction)) {
                    if (Math.abs(y - dragStartY) > boxSize) {
                        System.out.println(direction = "vertical");
                    }
                }
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("release");
                dragStartX = -100;
                dragStartY = -100;
                direction = "";
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }
}
