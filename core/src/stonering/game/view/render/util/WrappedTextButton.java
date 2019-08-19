package stonering.game.view.render.util;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import stonering.util.global.StaticSkin;

/**
 * {@link TextButton} wrapped in {@link Container}.
 * Can be sized, and aligned within {@link VerticalGroup}.
 *
 * @author Alexander on 18.08.2019.
 */
public class WrappedTextButton extends Container<TextButton> {
    protected final TextButton button;

    public WrappedTextButton(String text) {
        button = new TextButton(text, StaticSkin.getSkin());
        setActor(button);
    }

    public void toggle() {
        button.toggle();
    }
}
