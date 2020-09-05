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
    }

    protected Table table(Job job, JobSkillAspect jobSkillAspect) {
        Table table = new Table();
        SkillValue state = jobSkillAspect.skills.get(job.skill);
        table.add(new Label(state.level() + "", StaticSkin.skin())); // numeric level
        Stack progressStack = new Stack();
        ProgressBar progress =new ProgressBar(0f, 1f, 0.01f, false, StaticSkin.skin()); // exp
        progress.setValue(state.nextLevelProgress());
        progressStack.add(progress);
        progressStack.add(new Label(job.name, StaticSkin.skin())); // caption
        table.add(progressStack).fill().left().size(200, 25);
        //TODO attr bonus
        return table;
    }
}
