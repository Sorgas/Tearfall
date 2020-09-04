package stonering.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import stonering.entity.unit.aspects.job.JobsAspect;
import stonering.enums.images.DrawableMap;
import stonering.enums.unit.Job;

/**
 * Widget for displaying job state of a unit.
 * Shows job name, icon, level of associated skill, progress to next level, attribute bonus.
 * Allows to toggle job for unit.
 * 
 * @author Alexander on 04.09.2020.
 */
public class UnitSkillJobWidget extends Table {
    
    public UnitSkillJobWidget(Job job, JobsAspect jobsAspect) {
        add(new Image(DrawableMap.ICON.getDrawable(job.icon))).size(25);
        add()
    }
}
