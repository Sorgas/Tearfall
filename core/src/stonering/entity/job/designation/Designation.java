package stonering.entity.job.designation;

import static stonering.enums.action.TaskStatusEnum.CANCELED;
import static stonering.enums.action.TaskStatusEnum.COMPLETE;
import static stonering.enums.blocks.BlockTypeEnum.*;

import com.badlogic.gdx.graphics.Color;

import stonering.entity.Entity;
import stonering.entity.RenderAspect;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.task.TaskContainer;
import stonering.stage.renderer.atlas.AtlasesEnum;
import stonering.util.geometry.Position;
import stonering.entity.job.Task;

/**
 * Class for storing in {@link TaskContainer} to be rendered on local map.
 * Has its position, type for tile choosing and task.
 * Task is created in {@link stonering.game.model.system.task.DesignationSystem}
 *
 * @author Alexander Kuzyakov on 26.06.2018
 */
public class Designation extends Entity {
    public DesignationTypeEnum type;
    public Task task;

    public Designation(Position position, DesignationTypeEnum type) {
        super(position);
        this.type = type;
        add(createRenderAspect());
    }

    private RenderAspect createRenderAspect() {
        byte blockType = GameMvc.model().get(LocalMap.class).blockType.get(position);
        int atlasY = blockType == FLOOR.CODE
                || blockType == DOWNSTAIRS.CODE
                || blockType == FARM.CODE
                || blockType == SPACE.CODE ? 1 : 0;
        RenderAspect render = new RenderAspect(AtlasesEnum.ui_tiles.getBlockTile(type.SPRITE_X, atlasY)); // set sprite of designation type
        render.color = Color.YELLOW;
        return render;
    }

    /**
     * Designation is finished if its task status is COMPLETE or CANCELED.
     */
    public boolean isFinished() {
        return task != null && (task.status == CANCELED || task.status == COMPLETE);
    }
}
