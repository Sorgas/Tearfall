package stonering.widget.unit;

import com.badlogic.gdx.scenes.scene2d.ui.*;

import stonering.entity.unit.aspects.job.JobSkillAspect;
import stonering.entity.unit.aspects.job.SkillValue;
import stonering.enums.unit.Job;
import stonering.util.lang.StaticSkin;

/**
 * Widget for displaying job state of a unit.
 * Shows level of associated skill, job name, progress to next level, attribute bonus.
 * Allows to toggle job for unit.
 *
 * @author Alexander on 04.09.2020.
 */
public class SkilledJobWidget extends JobWidget {

    public SkilledJobWidget(Job job, JobSkillAspect aspect) {
        super(job, aspect);
        SkillValue state = aspect.skills.get(job.skill);
        levelLabel.setText(state.level()); // numeric level
        ProgressBar progress = new ProgressBar(0f, 1f, 0.01f, false, StaticSkin.skin()); // exp
        progress.setValue(state.nextLevelProgress());
        labelStack.add(progress);
        labelStack.add(new Label(job.name, StaticSkin.skin())); // caption
    }
}
