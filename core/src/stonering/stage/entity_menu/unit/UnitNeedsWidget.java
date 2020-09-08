package stonering.stage.entity_menu.unit;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.enums.images.DrawableMap;
import stonering.enums.unit.need.NeedEnum;
import stonering.widget.util.NeedStateProgressBar;

/**
 * Displays hunger, thirst, and fatigue of a unit.
 * Consists of column with needs icons and progress bars.
 *
 * @author Alexander on 20.12.2019.
 */
public class UnitNeedsWidget extends Table {

    public UnitNeedsWidget(Unit unit) {
        NeedAspect aspect = unit.get(NeedAspect.class);
        addBar("hunger:", NeedEnum.FOOD, aspect);
        addBar("thirst:", NeedEnum.WATER, aspect);
        addBar("fatigue:", NeedEnum.REST, aspect);
    }

    private void addBar(String drawableName, NeedEnum parameter, NeedAspect aspect) {
        Drawable drawable = DrawableMap.ICON.getDrawable(drawableName);
        Image image = new Image(drawable);
        add(image).size(40);
        add(new NeedStateProgressBar(aspect.needs.get(parameter))).growX().row();
    }
}
