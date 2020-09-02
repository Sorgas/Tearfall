package stonering.widget.item;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import stonering.entity.item.Item;
import stonering.util.lang.StaticSkin;

/**
 * Adds a checkbox to item button.
 *
 * @author Alexander on 27.03.2020
 */
public class CheckableSingleItemSquareButton extends SingleItemSquareButton {

    public CheckableSingleItemSquareButton(Item item, Drawable background) {
        super(item, background);
        Container<CheckBox> container = wrapWithContainer(new CheckBox("", StaticSkin.getSkin()), 10);
        container.align(Align.bottomLeft);
    }
}
