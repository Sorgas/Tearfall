package stonering.stage.toolbar.rightbar;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.NameAspect;
import stonering.entity.unit.aspects.job.JobSkillAspect;
import stonering.enums.unit.Job;
import stonering.enums.unit.JobMap;
import stonering.game.GameMvc;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.lang.StaticSkin;
import stonering.widget.util.WrappedLabel;

/**
 * Menu for assigning jobs for all units in the settlement. Shows unit/job table with checkboxes.
 * High-level skills are highlighted gold.
 * Forgetting skills are highlighted red.
 *
 * @author Alexander on 06.07.2020.
 */
public class GlobalJobsMenu extends Table {
    private List<Job> jobList;

    public GlobalJobsMenu() {
        jobList = new ArrayList<>(JobMap.all());
        createHeaders();
        row();
        fill();
        setBackground(StaticSkin.generator.generate(StaticSkin.background));
    }

    private void fill() {
        GameMvc.model().get(UnitContainer.class).objects.forEach(this::createUnitRow);
    }

    private void createHeaders() {
        add(new Label("qwer", StaticSkin.skin())).colspan(jobList.size() + 1).row();
        add();
        jobList.stream()
                .map(job -> new WrappedLabel(job.name))
                .forEach(this::add);
    }

    private void createUnitRow(Unit unit) {
        JobSkillAspect jobSkillAspect = unit.get(JobSkillAspect.class);
        String name = unit.optional(NameAspect.class)
                .map(aspect -> aspect.name)
                .orElse(unit.toString());
        add(new Label(name, StaticSkin.skin()));
        jobList.forEach(job -> {
            boolean enabled = jobSkillAspect.enabledJobs.contains(job.name);
            CheckBox checkBox = new CheckBox(null, StaticSkin.getSkin());
            checkBox.setChecked(enabled);
            checkBox.getStyle().checkboxOff.setMinWidth(25);
            checkBox.getStyle().checkboxOff.setMinHeight(25);
            checkBox.getStyle().checkboxOn.setMinWidth(25);
            checkBox.getStyle().checkboxOn.setMinHeight(25);
            checkBox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (checkBox.isChecked()) {
                        jobSkillAspect.enabledJobs.add(job.name);
                    } else {
                        jobSkillAspect.enabledJobs.remove(job.name);
                    }
                }
            });
            add(checkBox);
        });
        row();
    }
}
