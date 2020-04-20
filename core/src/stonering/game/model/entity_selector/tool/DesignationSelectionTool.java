package stonering.game.model.entity_selector.tool;

import stonering.entity.RenderAspect;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.task.TaskContainer;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.geometry.Int3dBounds;

/**
 * @author Alexander on 17.03.2020
 */
public class DesignationSelectionTool extends SelectionTool {
    private DesignationTypeEnum type;

    @Override
    public void apply() {
        selector().get(RenderAspect.class).region = AtlasesEnum.ui_tiles.getBlockTile(type.TOOL_SPRITE, 2);
    }

    public void setType(DesignationTypeEnum type) {
        this.type = type;
    }

    @Override
    public void handleSelection(Int3dBounds bounds) {
        TaskContainer container = GameMvc.model().get(TaskContainer.class);
        bounds.iterate(position -> {
            container.designationSystem.submitDesignation(position, type);
        });
    }
}