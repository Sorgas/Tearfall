package stonering.game.core.view.render.stages.base;

import stonering.game.core.model.EntitySelector;
import stonering.game.core.model.lists.UnitContainer;
import stonering.game.core.model.local_map.LocalMap;
import stonering.game.core.view.render.util.Int3DBounds;
import stonering.game.core.view.tilemaps.LocalTileMap;

/**
 * @author Alexander on 19.03.2019.
 */
public class ZoneRenderer extends Renderer {
    private LocalMap localMap;
    private LocalTileMap localTileMap;
    private EntitySelector selector;
    private UnitContainer unitContainer;
    private Int3DBounds visibleArea;

    public ZoneRenderer(DrawingUtil drawingUtil, Int3DBounds visibleArea) {
        super(drawingUtil);
        this.visibleArea = visibleArea;
    }

    @Override
    public void render() {

    }
}
