package stonering.widget;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Functionally similar to {@link CheckBox},
 *
 * @author Alexander on 05.04.2020
 */
public class Checker extends Image {
    private int state;
    private Drawable checked, unchecked, semichecked;

    public Checker() {
        super();
        addListener(new InputListener() {

        });
    }
}
