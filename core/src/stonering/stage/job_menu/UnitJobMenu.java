package stonering.stage.job_menu;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.NameAspect;
import stonering.entity.unit.aspects.job.JobsAspect;
import stonering.enums.unit.Job;
import stonering.enums.unit.JobMap;
import stonering.game.GameMvc;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.global.StaticSkin;
import stonering.widget.util.WrappedLabel;

/**
 * Menu for assigning jobs for all units in the settlement. Shows unit/job table with checkboxes.
 * High-level skills are highlighted gold.
 * Forgetting skills are highlighted red.
 * 
 * @author Alexander on 06.07.2020.
 */
public class UnitJobMenu extends Table {
    private List<Job> jobList;
    
    public UnitJobMenu() {
        jobList = new ArrayList<>(JobMap.all());
        createHeaders();
        row();
        fill();
    }
    
    private void fill() {
        for (Unit object : GameMvc.model().get(UnitContainer.class).objects) {
            createUnitRow(object);
            row();
        }
    }
    
    private void createHeaders() {
        add();
        for (Job job : jobList) {
            WrappedLabel label = new WrappedLabel(job.name);
            add(label);
        }
    }
    
    private void createUnitRow(Unit unit) {
        JobsAspect jobsAspect = unit.get(JobsAspect.class);
        String name = unit.getOptional(NameAspect.class).map(aspect -> aspect.name).orElse(unit.toString());
        add(new Label(name, StaticSkin.skin()));
        jobList.forEach(job -> {
            boolean enabled = jobsAspect.enabledJobs.contains(job.name);
            CheckBox checkBox = new CheckBox(null, StaticSkin.getSkin());
            checkBox.setChecked(enabled);
            checkBox.getStyle().checkboxOff.setMinWidth(25);
            checkBox.getStyle().checkboxOff.setMinHeight(25);
            checkBox.getStyle().checkboxOn.setMinWidth(25);
            checkBox.getStyle().checkboxOn.setMinHeight(25);
            checkBox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(checkBox.isChecked()) {
                        jobsAspect.enabledJobs.add(job.name);
                    } else {
                        jobsAspect.enabledJobs.remove(job.name);
                    }
                }
            });
        });
    }
}
