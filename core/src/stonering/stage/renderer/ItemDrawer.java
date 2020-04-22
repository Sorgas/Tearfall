package stonering.stage.renderer;

import java.util.List;

import com.badlogic.gdx.math.Vector3;

import stonering.entity.RenderAspect;
import stonering.entity.item.Item;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.util.geometry.Position;

/**
 * Draws items laying on the ground. Multiple items on the same tile are drawn with offsets.
 *
 * @author Alexander on 21.04.2020
 */
public class ItemDrawer extends Drawer {
    private final ItemContainer container;
    private final Vector3 cacheVector;
    private final float FLOOR_CORRECTION = AtlasesEnum.blocks.TOPPING_HEIGHT / (float) AtlasesEnum.blocks.DEPTH;

    public ItemDrawer(SpriteDrawingUtil spriteUtil, ShapeDrawingUtil shapeUtil) {
        super(spriteUtil, shapeUtil);
        container = GameMvc.model().get(ItemContainer.class);
        cacheVector = new Vector3();
    }

    public void draw(Position position) {
        List<Item> items = container.getItemsInPosition(position);
        if(items.isEmpty()) return;
        cacheVector.set(position.x, position.y + FLOOR_CORRECTION, position.z);
        switch(items.size()) {
            case 1:
                draw1(items.get(0));
                break;
            case 2:
                draw2(items.get(0), items.get(1));
                break;
            default:
                drawMany(items.get(0), items.get(1), items.get(2));
        }
        if(items.size() > 3) spriteUtil.writeText("...", position.x, position.y, position.z);
    }

    private void draw1(Item item) {
        cacheVector.add(0.25f, 0.25f, 0);
        spriteUtil.drawSprite(item.get(RenderAspect.class).region, cacheVector);
    }

    private void draw2(Item item1, Item item2) {
        cacheVector.add(0, 0.25f, 0);
        spriteUtil.drawSprite(item1.get(RenderAspect.class).region, cacheVector);
        cacheVector.add(0.5f, 0, 0);
        spriteUtil.drawSprite(item2.get(RenderAspect.class).region, cacheVector);
    }

    private void drawMany(Item item1, Item item2, Item item3) {
        spriteUtil.drawSprite(item1.get(RenderAspect.class).region, cacheVector);
        cacheVector.add(0.5f, 0, 0);
        spriteUtil.drawSprite(item2.get(RenderAspect.class).region, cacheVector);
        cacheVector.add(-0.25f, 0.5f, 0);
        spriteUtil.drawSprite(item2.get(RenderAspect.class).region, cacheVector);
    }
}
