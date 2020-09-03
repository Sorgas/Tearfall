package stonering.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import stonering.enums.images.DrawableMap;
import stonering.enums.unit.health.CreatureAttributeEnum;
import stonering.util.lang.StaticSkin;

/**
 * @author Alexander on 9/4/2020
 */
public class CreatureAttributeLabel extends Table {
    public CreatureAttributeLabel(CreatureAttributeEnum attribute, int base, int modifier) {
        add(new Image(DrawableMap.ICON.getDrawable(attribute.ICON))).size(25);
        add(new Label(attribute.NAME + " " + base, StaticSkin.skin()));
        if (modifier > 0) {
            add(new Label(" +" + modifier, StaticSkin.skin())); // TODO make green text
        } else if (modifier < 0) {
            add(new Label(" " + modifier, StaticSkin.skin())); // TODO make red text
        }
    }
}
