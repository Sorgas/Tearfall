package stonering.game.view.render.stages.renderer;

import stonering.game.model.EntitySelector;
import stonering.game.model.lists.UnitContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Int3dBounds;
import stonering.game.view.tilemaps.LocalTileMap;

/**
 * @author Alexander on 19.03.2019.
 */
public class ZoneRenderer extends Renderer {
    private LocalMap localMap;
    private LocalTileMap localTileMap;
    private EntitySelector selector;
    private UnitContainer unitContainer;
    private Int3dBounds visibleArea;

    public ZoneRenderer(DrawingUtil drawingUtil, Int3dBounds visibleArea) {
        super(drawingUtil);
        this.visibleArea = visibleArea;
    }

    @Override
    public void render() {

    }
}
