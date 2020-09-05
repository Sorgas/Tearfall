package stonering.widget.unit;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import stonering.entity.unit.aspects.job.JobSkillAspect;
import stonering.enums.unit.Job;
import stonering.util.lang.StaticSkin;

/**
 * Allows player to toggle job for unit.
 *
 * @author Alexander on 9/6/2020
 */
public abstract class JobWidget extends Table {
    private final Stack stack;
    private final Container<Label> dimmer;

    public JobWidget(Job job, JobSkillAspect aspect) {
        add(stack = new Stack()).grow();
        stack.add(dimmer = new Container<>(new Label("", StaticSkin.skin())));
        stack.add(table(job, aspect));
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(aspect.enabledJobs.contains(job.name)) {
                    aspect.enabledJobs.remove(job.name);
                    dimmer.setBackground(StaticSkin.getColorDrawable(StaticSkin.shade));
                } else {
                    aspect.enabledJobs.add(job.name);
                    dimmer.setBackground(null);
                }
                return true;
            }
        });
    }

    protected abstract Table table(Job job, JobSkillAspect jobSkillAspect);
}
