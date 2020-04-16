package stonering.entity.job.designation;

import stonering.entity.RenderAspect;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.enums.designations.DesignationsTileMapping;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.geometry.Position;

/**
 * Designation of order to be stored and rendered on map. Exact order specified by type field.
 *
 * @author Alexander Kuzyakov
 */
public class OrderDesignation extends Designation {

    public OrderDesignation(Position position, DesignationTypeEnum type) {
        super(position, type);
        get(RenderAspect.class).region = AtlasesEnum.ui_tiles.getBlockTile(DesignationsTileMapping.getAtlasX(type.CODE), 0); // set sprite of designation type
    }
}
