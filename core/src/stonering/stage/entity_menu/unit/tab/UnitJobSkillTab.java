package stonering.stage.entity_menu.unit.tab;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.job.JobSkillAspect;
import stonering.enums.unit.Job;
import stonering.enums.unit.JobMap;
import stonering.util.lang.StaticSkin;
import stonering.widget.unit.JobWidget;
import stonering.widget.unit.SkilledJobWidget;
import stonering.widget.unit.UnskilledJobWidget;

/**
 * This tab shows list of all available jobs and allows to assign jobs to unit.
 * Shows different widget for job with skill(has progress bar) and without skill.
 * Jobs without skill listed in separate column.
 *
 * @author Alexander on 02.07.2020.
 */
public class UnitJobSkillTab extends Table {
    private JobSkillAspect aspect;
    private Table listTable;

    public UnitJobSkillTab(Unit unit) {
        aspect = unit.get(JobSkillAspect.class);

        // header
        add(new Label("Unit skills and jobs.", StaticSkin.skin())).height(80).growX().row();
        add(listTable = new Table()).grow();
        listTable.defaults().width(200).fill();
        listTable.align(Align.topLeft);
        List<Job> skilledJobs = JobMap.skilled();
        List<Job> unskilledJobs = JobMap.unskilled();
        int rowCount = Math.max((int) Math.ceil(skilledJobs.size() / 2f), unskilledJobs.size());
        List<Job> column1 = skilledJobs.subList(0, rowCount);
        List<Job> column2 = skilledJobs.subList(rowCount, skilledJobs.size());

        for (int i = 0; i < rowCount; i++) {
            listTable.add(createWidget(i < column1.size() ? column1.get(i) : null));
            listTable.add(createWidget(i < column2.size() ? column2.get(i) : null));
            listTable.add(createWidget(i < unskilledJobs.size() ? unskilledJobs.get(i) : null));
            listTable.row();
        }
        listTable.debugAll();
    }

    private JobWidget createWidget(Job job) {
        if (job == null) return null;
        return job.skill == null
                ? new UnskilledJobWidget(job, aspect)
                : new SkilledJobWidget(job, aspect);
    }
}
