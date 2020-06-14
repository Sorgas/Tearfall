package stonering.stage.entity_menu.unit;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.images.DrawableMap;
import stonering.enums.unit.health.HealthParameterEnum;
import stonering.widget.util.HealthParameterStateProgressBar;

/**
 * Displays hunger, thirst, and fatigue of a unit.
 * Consists of column with needs icons and progress bars.
 *
 * @author Alexander on 20.12.2019.
 */
public class UnitNeedsWidget extends Table {
    private Unit unit;

    public UnitNeedsWidget(Unit unit) {
        this.unit = unit;
        createBars(unit);
    }

    private void createBars(Unit unit) {
        HealthAspect aspect = unit.get(HealthAspect.class);
        addBar("hunger:", HealthParameterEnum.HUNGER, aspect);
        addBar("thirst:", HealthParameterEnum.THIRST, aspect);
        addBar("fatigue:", HealthParameterEnum.FATIGUE, aspect);
    }

    private void addBar(String drawableName, HealthParameterEnum parameter, HealthAspect aspect) {
        Drawable drawable = DrawableMap.ICON.getDrawable(drawableName);
        Image image = new Image(drawable);
        add(image).size(40);
        add(new HealthParameterStateProgressBar(aspect.parameters.get(parameter))).growX().row();
    }
}