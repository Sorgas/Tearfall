package stonering.stage.entity_menu.unit;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

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
        listTable.defaults().height(25).pad(5);
        listTable.align(Align.topLeft);
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
        Table rowTable = new Table();
        rowTable.add(new Label(jobName, StaticSkin.skin())).width(200);
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
                    aspect.enabledJobs.add(jobName);
                } else {
                    aspect.enabledJobs.remove(jobName);
                }
            }
        });
        rowTable.add(checkBox).width(25);
        rowTable.setBackground(StaticSkin.generator.generate(StaticSkin.backgroundFocused));
        listTable.add(rowTable).row();
    }
}
