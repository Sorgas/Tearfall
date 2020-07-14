package stonering.stage.renderer;

import java.util.Optional;

import stonering.game.GameMvc;
import stonering.game.model.system.ZoneContainer;
import stonering.util.geometry.Position;

/**
 * Draws zone in position if some present.
 *
 * @author Alexander on 6/25/2020
 */
public class ZoneDrawer extends Drawer {
    private ZoneContainer container;
    private Position cachePosition;

    public ZoneDrawer(SpriteDrawingUtil spriteUtil, ShapeDrawingUtil shapeUtil) {
        super(spriteUtil, shapeUtil);
        container = GameMvc.model().get(ZoneContainer.class);
        cachePosition = new Position();
    }

    public void draw(int x, int y, int z) {
        Optional.ofNullable(container.getZone(cachePosition.set(x, y, z)))
                .ifPresent(zone -> {
                    spriteUtil.drawSprite(zone.type.SPRITE, cachePosition);
                });
    }
}
