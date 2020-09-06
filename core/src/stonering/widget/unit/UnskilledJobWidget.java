package stonering.widget.unit;

import com.badlogic.gdx.scenes.scene2d.ui.*;

import stonering.entity.unit.aspects.job.JobSkillAspect;
import stonering.enums.unit.Job;
import stonering.util.lang.StaticSkin;

/**
 * @author Alexander on 9/6/2020
 */
public class UnskilledJobWidget extends JobWidget {

    public UnskilledJobWidget(Job job, JobSkillAspect aspect) {
        super(job, aspect);
        levelLabel.setText("n/a"); // numeric level
        labelStack.add(new Label(job.name, StaticSkin.skin()));
    }
}
