package stonering.stage.entity_menu.unit;

import com.badlogic.gdx.scenes.scene2d.ui.*;

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
        add(new Label("activity:", StaticSkin.getSkin())).height(80);
        add(new Label(getUnitCurrentTask(unit), StaticSkin.getSkin())).height(80).row();

        add(new Label("tools:", StaticSkin.getSkin())).colspan(2).row();
        add(createToolsList(unit.get(EquipmentAspect.class))).height(120).colspan(2).row();

        add(new Label(getUnitBestSkill(unit), StaticSkin.getSkin())).colspan(2).row();

        add(new UnitNeedsWidget(unit)).colspan(2);
        setWidth(300);
        setBackground(StaticSkin.getColorDrawable(StaticSkin.backgroundFocused));
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

    private ScrollPane createToolsList(EquipmentAspect aspect) {
        Table table = new Table();
        ScrollPane pane = new ScrollPane(table);
        aspect.getEquippedTools()
                .forEach(item -> table.add(new ItemLabel(item)).growX().row());
        return pane;
    }
}
