package stonering.stage.unit;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.RenderAspect;
import stonering.util.global.StaticSkin;

/**
 * Shows picture, name, current task, tool, best skill, and needs state.
 *
 * @author Alexander on 20.12.2019.
 */
public class UnitImageColumn extends Table {

    public UnitImageColumn(Unit unit) {

    }

    private void createTable(Unit unit) {
        add(new Label("unit name", StaticSkin.getSkin())).row();
        add(new Image(unit.getAspect(RenderAspect.class).drawable)).row();
        //TODO equipped tool/weapon

    }
}
