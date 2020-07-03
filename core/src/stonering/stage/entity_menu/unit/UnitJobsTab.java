package stonering.stage.entity_menu.unit;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.job.JobsAspect;
import stonering.enums.unit.JobMap;
import stonering.util.global.StaticSkin;

/**
 * This tab shows list of all available jobs and allows to assign jobs to unit.
 *
 * @author Alexander on 02.07.2020.
 */
public class UnitJobsTab extends Table {
    private JobsAspect aspect;
    private ScrollPane pane;
    private Table listTable;
    
    public UnitJobsTab(Unit unit) {
        // header
        add(new Label("Assign jobs to unit.", StaticSkin.skin())).height(80).growX().row();
        
        listTable = new Table();
        add(pane = new ScrollPane(listTable)).grow();
        aspect = unit.get(JobsAspect.class);
        aspect.enabledJobs.forEach(job -> addRowForJob(job, true));
        JobMap.all().stream()
                .map(job -> job.name)
                .filter(jobName -> !aspect.enabledJobs.contains(jobName))
                .forEach(jobName -> addRowForJob(jobName, false));
        top();
        setDebug(true, true);
    }

    private void addRowForJob(String jobName, boolean enabled) {
        if(jobName == null) return;
        listTable.add(new Label(jobName, StaticSkin.skin()));
        CheckBox checkBox = new CheckBox(null, StaticSkin.getSkin());
        checkBox.setChecked(enabled);
        checkBox.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(checkBox.isChecked()) {
                    aspect.enabledJobs.add(jobName);
                } else {
                    aspect.enabledJobs.remove(jobName);
                }
            }
        });
        listTable.add(checkBox).row();
    }
}
