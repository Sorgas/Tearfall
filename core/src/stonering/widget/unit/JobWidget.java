package stonering.widget.unit;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import stonering.entity.unit.aspects.job.JobSkillAspect;
import stonering.enums.unit.Job;
import stonering.util.lang.StaticSkin;

/**
 * Allows player to toggle job for unit.
 *
 * @author Alexander on 9/6/2020
 */
public abstract class JobWidget extends Table {
    protected final Stack levelStack; // level value and highlighter
    protected final Stack labelStack; // job name and progress
    private final Container<Label> highlighter;
    protected final Label levelLabel;
    private final Job job;
    private final JobSkillAspect aspect;

    public JobWidget(Job job, JobSkillAspect aspect) {
        this.job = job;
        this.aspect = aspect;
        add(levelStack = new Stack()).size(30).fill();
        add(labelStack = new Stack()).grow();
        levelStack.add(highlighter = new Container<>());
        levelStack.add(levelLabel = new Label("", StaticSkin.skin())); // numeric level
        levelLabel.setAlignment(Align.center);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (aspect.enabledJobs.contains(job.name)) {
                    aspect.enabledJobs.remove(job.name);
                } else {
                    aspect.enabledJobs.add(job.name);
                }
                updateHighlighter();
                return true;
            }
        });
        updateHighlighter();
    }

    private void updateHighlighter() {
        highlighter.setBackground(aspect.enabledJobs.contains(job.name) ? StaticSkin.getColorDrawable(Color.LIGHT_GRAY) : null);
    }
}
