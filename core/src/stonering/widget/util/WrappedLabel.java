package stonering.widget.util;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import stonering.util.global.StaticSkin;

/**
 * @author Alexander on 24.08.2019.
 */
public class WrappedLabel extends Container<Label> {
    public final Label label;

    public WrappedLabel(String text) {
        setActor(label = new Label(text, StaticSkin.getSkin()));
    }

    public void setText(String text) {
        label.setText(text);
    }
}
