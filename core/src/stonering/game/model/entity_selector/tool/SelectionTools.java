package stonering.game.model.entity_selector.tool;

/**
 * @author Alexander_Kuzyakov on 16.03.2020.
 */
public class SelectionTools {
    public static final SelectEntitySelectionTool SELECT = new SelectEntitySelectionTool();
    public static final DesignationSelectionTool DESIGNATION = new DesignationSelectionTool();
    public static final DesignateBuildingSelectionTool BUILDING = new DesignateBuildingSelectionTool();
    public static final ZoneSelectionTool ZONE = new ZoneSelectionTool();
    public static final ZoneUpdateSelectionTool ZONE_UPDATE = new ZoneUpdateSelectionTool();
    public static final ConstructionDesignationTool CONSTRUCTION = new ConstructionDesignationTool();
}
