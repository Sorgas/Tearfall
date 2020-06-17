package stonering.stage.renderer;

import static stonering.stage.renderer.AtlasesEnum.liquids;

import java.util.Optional;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * @author Alexander on 17.06.2020.
 */
public class LiquidDrawer extends Drawer {
    private final LocalMap localMap;

    public LiquidDrawer(SpriteDrawingUtil spriteUtil, ShapeDrawingUtil shapeUtil) {
        super(spriteUtil, shapeUtil);
        localMap = GameMvc.model().get(LocalMap.class);
    }

    public void drawFlat(int x, int y, int z) {
        if (z > 0 && localMap.flooding.get(x, y, z - 1) >= 7)
            drawSprite(x, y, z, liquids.getToppingTile(6, 0));
    }

    public void drawBlock(int x, int y, int z) {
        Optional.of(localMap.flooding.get(x, y, z))
                .filter(flooding -> flooding != 0)
                .map(flooding -> liquids.getBlockTile(flooding - 1, 0)) // get sprite
                .ifPresent(sprite -> drawSprite(x, y, z, sprite));
    }

    private void drawSprite(int x, int y, int z, TextureRegion sprite) {
        spriteUtil.updateColorA(0.6f);
        spriteUtil.drawSprite(sprite, x, y, z);
        spriteUtil.updateColorA(1f);
    }
}
