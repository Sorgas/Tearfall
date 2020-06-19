package stonering.game.model.entity_selector.aspect;

import com.sun.istack.NotNull;
import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.game.model.entity_selector.tool.SelectionTools;
import stonering.game.model.entity_selector.tool.SelectionTool;
import stonering.util.logging.Logger;

/**
 * Contains {@link SelectionTool} picked for selector.
 *
 * @author Alexander on 10.01.2020
 */
public class SelectionAspect extends Aspect {
    public SelectionTool tool = SelectionTools.SELECT;
    
    public SelectionAspect(Entity entity) {
        super(entity);
    }
    
    public void set(@NotNull SelectionTool tool) {
        if(tool == null) Logger.UI.logError("Assigning null tool to entity selector.");
        this.tool = tool;
        tool.apply();
    }
}
