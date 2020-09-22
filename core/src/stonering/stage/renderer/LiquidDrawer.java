package stonering.stage.renderer;

import static stonering.enums.blocks.BlockTypeEnum.SPACE;
import static stonering.stage.renderer.atlas.AtlasesEnum.liquids;

import java.util.Optional;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.liquid.LiquidContainer;
import stonering.game.model.system.liquid.LiquidTile;

/**
 * @author Alexander on 17.06.2020.
 */
public class LiquidDrawer extends Drawer {
    private final LiquidContainer liquidContainer;
    private final LocalMap map;
    private final Color stable = Color.OLIVE;
    private final Color unstable = Color.PINK;
    public boolean debugMode = false;

    public LiquidDrawer(SpriteDrawingUtil spriteUtil, ShapeDrawingUtil shapeUtil) {
        super(spriteUtil, shapeUtil);
        liquidContainer = GameMvc.model().get(LiquidContainer.class);
        map = GameMvc.model().get(LocalMap.class);
    }

    public void drawFlat(int x, int y, int z) {
        if (z <= 0) return;
        Optional.ofNullable(liquidContainer.getTile(x, y, z - 1))
                .filter(tile -> tile.amount >= 7)
                .ifPresent(tile -> drawSprite(x, y, z, tile, liquids.getToppingTile(6, 0)));
    }

    public void drawBlock(int x, int y, int z) {
        Optional.ofNullable(liquidContainer.getTile(x, y, z))
                .filter(tile -> tile.amount > 0)
                .ifPresent(tile -> {
                    if (map.blockType.getEnumValue(x, y, z) == SPACE)
                        drawSprite(x, y, z, tile, liquids.getToppingTile(5, 0));
                    drawSprite(x, y, z, tile, liquids.getBlockTile(tile.amount - 1, 0));
                });
    }

    private void drawSprite(int x, int y, int z, LiquidTile tile, TextureRegion sprite) {
        if (debugMode) spriteUtil.setColor(tile.tpStable ? stable : unstable);
        spriteUtil.updateColorA(0.6f);
        spriteUtil.drawSprite(sprite, x, y, z);
        spriteUtil.resetColor();
    }
}
