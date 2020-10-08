package stonering.stage.entity_menu.unit;

import com.badlogic.gdx.scenes.scene2d.ui.*;

import stonering.entity.job.Task;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.job.TaskAspect;
import stonering.entity.RenderAspect;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.job.JobSkillAspect;
import stonering.entity.unit.aspects.job.SkillValue;
import stonering.enums.unit.JobMap;
import stonering.util.lang.StaticSkin;
import stonering.widget.item.ItemLabel;

import java.util.Comparator;
import java.util.Objects;

/**
 * Shows picture, name, current task, tool, best skill, and needs state.
 * TODO equipped tool
 * 
 * @author Alexander on 20.12.2019.
 */
public class UnitImageColumn extends Table {

    public UnitImageColumn(Unit unit) {
        createTable(unit);
    }

    private void createTable(Unit unit) {
        defaults().growX();
        add(new Label("unit name", StaticSkin.skin())).row(); // TODO name generation

        add(new Label("race, " + getUnitProfession(unit), StaticSkin.skin())).row(); // TODO race
        
        add(createImageContainer(unit)).row();
        
        //activity
        add(new Label(getUnitCurrentTask(unit), StaticSkin.skin())).row();
        
        add(createToolsList(unit.get(EquipmentAspect.class))).row();

        add(new UnitNeedsWidget(unit));
        setBackground(StaticSkin.getColorDrawable(StaticSkin.backgroundFocused));
    }
    
    private String getUnitCurrentTask(Unit unit) {
        return unit.optional(TaskAspect.class)
                .map(aspect -> aspect.task)
                .map(Task::toString)
                .orElse("Doing nothing");
    }

    private String getUnitProfession(Unit unit) {
        return unit.get(JobSkillAspect.class).skills.values().stream()
                .filter(value -> value.level() > 0)
                .max(Comparator.comparingInt(SkillValue::level))
                .map(value -> JobMap.bySkill(value.skillName))
                .map(job -> job.name)
                .orElse("Peasant");
    }

    private Container<Image> createImageContainer(Unit unit) {
        
        Image image = new Image(unit.get(RenderAspect.class).region);
        return new Container<>(image).size(200, 300);
    }

    private Table createToolsList(EquipmentAspect aspect) {
        Table table = new Table();
        aspect.grabSlotStream()
                .map(slot -> slot.grabbedItem)
                .filter(Objects::nonNull)
                .filter(item -> item.type.tool != null)
                .forEach(item -> table.add(new ItemLabel(item)).growX().row()); // add table row for each tool item
        return table;
    }
}
