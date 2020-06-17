package stonering.stage.renderer;

import static stonering.stage.renderer.AtlasesEnum.liquids;

import java.util.Optional;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import stonering.game.GameMvc;
import stonering.game.model.system.liquid.LiquidContainer;

/**
 * @author Alexander on 17.06.2020.
 */
public class LiquidDrawer extends Drawer {
    private final LiquidContainer liquidContainer;

    public LiquidDrawer(SpriteDrawingUtil spriteUtil, ShapeDrawingUtil shapeUtil) {
        super(spriteUtil, shapeUtil);
        liquidContainer = GameMvc.model().get(LiquidContainer.class);
    }

    public void drawFlat(int x, int y, int z) {
        if (z > 0) Optional.ofNullable(liquidContainer.getTile(x, y, z - 1))
                .filter(tile -> tile.amount >= 7)
                .ifPresent(tile -> drawSprite(x, y, z, liquids.getToppingTile(6, 0)));
    }

    public void drawBlock(int x, int y, int z) {
        Optional.ofNullable(liquidContainer.getTile(x, y, z))
                .map(tile -> tile.amount)
                .filter(amount -> amount != 0)
                .map(amount -> liquids.getBlockTile(amount - 1, 0)) // get sprite
                .ifPresent(sprite -> drawSprite(x, y, z, sprite));
    }

    private void drawSprite(int x, int y, int z, TextureRegion sprite) {
        spriteUtil.updateColorA(0.6f);
        spriteUtil.drawSprite(sprite, x, y, z);
        spriteUtil.updateColorA(1f);
    }
}
