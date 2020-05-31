package stonering.stage.unit;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import stonering.entity.job.Task;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.TaskAspect;
import stonering.entity.RenderAspect;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.job.SkillAspect;
import stonering.util.global.StaticSkin;
import stonering.widget.item.ItemLabel;

import java.util.Comparator;
import java.util.Optional;

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
        defaults().growX();
        add(new Label("unit name", StaticSkin.getSkin())).colspan(2).row();
        add(createImageContainer(unit)).colspan(2).expandY().row();
        //TODO equipped tool/weapon
        add(new Label("activity:", StaticSkin.getSkin()));
        add(new Label(getUnitCurrentTask(unit), StaticSkin.getSkin())).row();

        add(new Label("tools:", StaticSkin.getSkin()));
        unit.get(EquipmentAspect.class).getEquippedTools().forEach(item -> add(new ItemLabel(item)).colspan(2).row());

        add(new Label(getUnitBestSkill(unit), StaticSkin.getSkin())).row();

        add(new UnitNeedsWidget(unit)).colspan(2);
        setWidth(300);
    }

    private String getUnitCurrentTask(Unit unit) {
        return Optional.ofNullable(unit.get(TaskAspect.class))
                .map(aspect -> aspect.task)
                .map(task -> task.name)
                .orElse("Doing nothing");
    }

    private String getUnitBestSkill(Unit unit) {
        return Optional.ofNullable(unit.get(SkillAspect.class))
                .map(aspect -> aspect.skills.values().stream())
                .flatMap(stream -> stream.max(Comparator.comparingInt(skill -> skill.state.getLevel())))
                .map(skillValue -> skillValue.skill)
                .orElse("Peasant");
    }

    private Container<Image> createImageContainer(Unit unit) {
        Image image = new Image(unit.get(RenderAspect.class).region);
        return new Container<>(image).size(200);
    }
}
