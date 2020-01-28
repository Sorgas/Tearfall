package stonering.stage.unit;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

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
        HealthAspect aspect = unit.getAspect(HealthAspect.class);
        add(new Image(DrawableMap.instance().getIconDrawable("hunger:")));
        add(new HealthParameterStateProgressBar(aspect.parameters.get(HealthParameterEnum.HUNGER))).row();
        add(new Image(DrawableMap.instance().getIconDrawable("thirst:")));
        add(new HealthParameterStateProgressBar(aspect.parameters.get(HealthParameterEnum.THIRST))).row();
        add(new Image(DrawableMap.instance().getIconDrawable("fatigue:")));
        add(new HealthParameterStateProgressBar(aspect.parameters.get(HealthParameterEnum.FATIGUE))).row();
    }
}
