package stonering.stage.unit;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import stonering.entity.job.Task;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.entity.unit.aspects.RenderAspect;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.job.SkillAspect;
import stonering.util.global.StaticSkin;
import stonering.widget.item.ItemLabel;

import java.util.Comparator;

/**
 * Shows picture, name, current task, tool, best skill, and needs state.
 *
 * @author Alexander on 20.12.2019.
 */
public class UnitImageColumn extends Table {

    public UnitImageColumn(Unit unit) {
        createTable(unit);
    }

    private void createTable(Unit unit) {
        add(new Label("unit name", StaticSkin.getSkin())).colspan(2).row();
        add(new Image(unit.getAspect(RenderAspect.class).region)).colspan(2).row();
        //TODO equipped tool/weapon
        add(new Label("activity:", StaticSkin.getSkin()));
        add(new Label(getUnitCurrentTask(unit), StaticSkin.getSkin())).row();

        add(new Label("tools:", StaticSkin.getSkin()));
        unit.getAspect(EquipmentAspect.class).getEquippedTools().forEach(item -> add(new ItemLabel(item)).colspan(2).row());

        add(new Label(getUnitBestSkill(unit), StaticSkin.getSkin())).row();

        add(new UnitNeedsWidget(unit));
        setWidth(300);
    }

    private String getUnitCurrentTask(Unit unit) {
        Task task = unit.getAspect(PlanningAspect.class).task;
        return task != null ? task.name : "Doing nothing";
    }

    private String getUnitBestSkill(Unit unit) {
        return unit.getAspect(SkillAspect.class).skills.values().stream()
                .max(Comparator.comparingInt(skill -> skill.state.getLevel())).map(skillValue -> skillValue.skill).orElse("Peasant");
    }
}
