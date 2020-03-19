package stonering.game.model.entity_selector.tool;

import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.task.TaskContainer;
import stonering.util.geometry.Int3dBounds;

/**
 * @author Alexander on 17.03.2020
 */
public class DesignationSelectionTool extends SelectionTool {
    public DesignationTypeEnum type;

    @Override
    public void handleSelection(Int3dBounds bounds) {
        TaskContainer container = GameMvc.model().get(TaskContainer.class);
        bounds.iterate(position -> {
            container.designationSystem.submitDesignation(position, type);
        });
    }
}
