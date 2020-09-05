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
    }

    protected Table table(Job job, JobSkillAspect jobSkillAspect) {
        Table table = new Table();
        table.add(new Label("n/a", StaticSkin.skin())); // numeric level
        table.add(new Label(job.name, StaticSkin.skin())).fill().left().size(200, 25);
        //TODO attr bonus
        return table;
    }
}
