package stonering.game.model.entity_selector.tool;

/**
 * @author Alexander_Kuzyakov on 16.03.2020.
 */
public enum SelectionToolEnum {
    SELECT(new SelectEntitySelectionTool()),
    BUILDING(new DesignateBuildingSelectionTool())
    ;
    
    public final SelectionTool TOOL;

    SelectionToolEnum(SelectionTool TOOL) {
        this.TOOL = TOOL;
    }
}
