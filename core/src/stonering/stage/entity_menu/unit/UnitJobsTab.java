package stonering.stage.entity_menu.unit;

import java.util.Arrays;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.job.JobsAspect;
import stonering.enums.unit.Job;
import stonering.enums.unit.JobMap;
import stonering.util.global.StaticSkin;

/**
 * This tab shows list of all available jobs and allows to assign jobs to unit.
 *
 * @author Alexander on 02.07.2020.
 */
public class UnitJobsTab extends Table {
    private JobsAspect aspect;

    public UnitJobsTab(Unit unit) {
        aspect = unit.get(JobsAspect.class);
        aspect.enabledJobs.forEach(job -> addRowForJob(job, true));
        Arrays.stream(JobMap.values())
                .filter(job -> aspect.enabledJobs.contains(job))
                .forEach(job -> addRowForJob(job, false));
//         list assigned jobs
//         list other jobs
        defaults().width(300).top().left();
        Label label = new Label("Equipped items", StaticSkin.skin());
        label.setAlignment(Align.center);
        add(label).height(100).center();
        label = new Label("Hauled items", StaticSkin.skin());
        label.setAlignment(Align.center);
        add(label).height(100).center().row();
//        add(createHauledList()).height(775);
        left();
    }

    private void addRowForJob(Job job, boolean enabled) {
        if(job == null) return;
        add(new Label(job.name, StaticSkin.skin()));
        CheckBox checkBox = new CheckBox(null, StaticSkin.getSkin());
        checkBox.setChecked(enabled);
        checkBox.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(checkBox.isChecked()) {
                    aspect.enabledJobs.add(job);
                } else {
                    aspect.enabledJobs.remove(job);
                }
            }
        });
        add(checkBox).row();
    }
}
