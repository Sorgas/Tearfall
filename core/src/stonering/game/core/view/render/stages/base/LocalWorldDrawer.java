package stonering.game.core.view.render.stages.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.game.core.model.GameModel;

/**
 * Contains renderers for drawing local world and launches them in a sequence.
 *
 * Draws LocalMap. Blocks and plants are taken from LocalTileMap,
 * Buildings, units, and items are taken from LocalMap.
 *
 * TODO move color from batch to sprites.
 *
 * @author Alexander Kuzyakov on 13.06.2017.
 */
public class LocalWorldDrawer {
    private DrawingUtil drawingUtil;
    private TileRenderer tileRenderer;
    private SelectionFrameRenderer selectionFrameRenderer;
    private EntitySelectorRenderer entitySelectorRenderer;


    public LocalWorldDrawer(GameModel gameModel) {
        drawingUtil = new DrawingUtil();
        tileRenderer = new TileRenderer(gameModel, drawingUtil);
        selectionFrameRenderer = new SelectionFrameRenderer(gameModel, drawingUtil);
        entitySelectorRenderer = new EntitySelectorRenderer(gameModel, drawingUtil);
    }

    /**
     * Renders local map with all entities to single batch frame.
     */
    public void drawLocalWorld() {
        drawingUtil.begin();
        tileRenderer.render();
        entitySelectorRenderer.render();
        selectionFrameRenderer.render();
        drawingUtil.end();
    }

    //    public Vector2 translateScreenPositionToModel(Vector2 screenPos) {
//        Vector2 vector = screenPos.sub(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2)); // to click point from center
//        vector.set(vector.x / tileWidth, -vector.y / tileDepth);
//        return new Vector2((float) Math.floor(selector.getPosition().getX() + vector.x), (float) Math.floor(selector.getPosition().getY() + vector.y));
//    }


}