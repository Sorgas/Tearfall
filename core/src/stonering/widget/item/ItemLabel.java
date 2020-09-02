package stonering.widget.item;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.item.Item;
import stonering.entity.RenderAspect;
import stonering.util.lang.StaticSkin;

/**
 * Widget for displaying item.
 * Displays item icon, main material, name, and condition words.
 *
 * @author Alexander on 28.01.2020.
 */
public class ItemLabel extends Table {

    public ItemLabel(Item item) {
        add(new Image(item.get(RenderAspect.class).region));
        add(new Label(item.title, StaticSkin.getSkin()));
    }
}
