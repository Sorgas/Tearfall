package stonering.stage.renderer;

import static stonering.enums.blocks.BlockTypeEnum.SPACE;
import static stonering.stage.renderer.AtlasesEnum.liquids;

import java.util.Optional;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.liquid.LiquidContainer;

/**
 * @author Alexander on 17.06.2020.
 */
public class LiquidDrawer extends Drawer {
    private final LiquidContainer liquidContainer;
    private final LocalMap map;
    
    public LiquidDrawer(SpriteDrawingUtil spriteUtil, ShapeDrawingUtil shapeUtil) {
        super(spriteUtil, shapeUtil);
        liquidContainer = GameMvc.model().get(LiquidContainer.class);
        map = GameMvc.model().get(LocalMap.class);
    }

    public void drawFlat(int x, int y, int z) {
        if (z > 0) Optional.ofNullable(liquidContainer.getTile(x, y, z - 1))
                .filter(tile -> tile.amount >= 7)
                .ifPresent(tile -> drawSprite(x, y, z, liquids.getToppingTile(6, 0)));
    }

    public void drawBlock(int x, int y, int z) {
        int amount = liquidContainer.getAmount(x, y, z);
        if(amount > 0) {
            if(map.blockType.getEnumValue(x, y, z) == SPACE) 
                drawSprite(x, y, z, liquids.getToppingTile(5, 0));
            drawSprite(x, y, z, liquids.getBlockTile(amount - 1, 0));
        }
        
    }

    private void drawSprite(int x, int y, int z, TextureRegion sprite) {
        spriteUtil.updateColorA(0.6f);
        spriteUtil.drawSprite(sprite, x, y, z);
        spriteUtil.updateColorA(1f);
    }
}
