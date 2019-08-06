package stonering.game.view.render.stages.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import stonering.util.geometry.Position;

import java.util.HashMap;
import java.util.Map;

import static stonering.game.view.render.stages.renderer.BatchUtil.*;

/**
 * Provides utility methods and rules for drawing scene.
 *
 * @author Alexander on 06.02.2019.
 */
public class DrawingUtil {
    private Batch batch;
    private BitmapFont font;

    private final float shadingStep = 0.06f;
    public final float maxZLevels = 1f / shadingStep; // levels further are shaded to black
    private Color batchColor;               // default batch color without light or transparency
    private Map<Position, TextureRegion> spriteCache;
    private Position cachePosition;

    public DrawingUtil(Batch batch) {
        this.batch = batch;
        font = new BitmapFont();
        batch.enableBlending();
        batchColor = new Color();
        spriteCache = new HashMap<>();
        cachePosition = new Position();
    }

    /**
     * Draws sprite on localMap position.
     */
    public void drawSprite(TextureRegion sprite, Position position) {
        batch.draw(sprite, getBatchX(position.x), getBatchY(position.y, position.z));
    }

    /**
     * Draws sprite by vector. Values can be fractional, sprite will be rendered between cells.
     */
    public void drawSprite(TextureRegion sprite, Vector3 vector) {
        batch.draw(sprite, getBatchX(vector.x), getBatchY(vector.y, vector.z));
    }

    public void drawScale(TextureRegion sprite, Position position, int width, int height) {
        batch.draw(sprite, getBatchX(position.x), getBatchY(position.y, position.z), width, height);
    }

    /**
     * Cuts standard tile from x y position in specified atlas.
     */
    public TextureRegion selectSprite(AtlasesEnum atlas, int x, int y) {
        if (!atlas.spriteCache.containsKey(cachePosition.set(x, y, 0))) {
            spriteCache.put(cachePosition, new TextureRegion(atlas.atlas,
                    x * TILE_WIDTH,
                    y * (BLOCK_TILE_HEIGHT) + TOPING_TILE_HEIGHT,
                    TILE_WIDTH, TILE_HEIGHT));
        }
        return spriteCache.get(cachePosition);
    }

    /**
     * Cuts tile toping from x y position in specified atlas.
     */
    public TextureRegion selectToping(AtlasesEnum atlas, int x, int y) {
        if (!atlas.spriteCache.containsKey(cachePosition.set(x, y, 1))) {
            spriteCache.put(cachePosition, new TextureRegion(atlas.atlas,
                    x * TILE_WIDTH,
                    y * BLOCK_TILE_HEIGHT,
                    TILE_WIDTH, TOPING_TILE_HEIGHT));
        }
        return spriteCache.get(cachePosition);
    }

    public void writeText(String text, int x, int y, int z) {
        float screenX = getBatchX(x);
        float screenY = getBatchY(y, z);
        font.draw(batch, text, screenX, screenY);
    }

    /**
     * Makes color transparent.
     *
     * @param a
     */
    public void updateColorA(float a) {
        Color color = batch.getColor();
        batch.setColor(color.r, color.g, color.b, a);
    }

    /**
     * Shades batch color to correspond lowering z coord.
     *
     * @param dz
     */
    public void shadeByZ(int dz) {
        float shadedColorChannel = 1 - (dz - 4) * shadingStep;
        batchColor.set(shadedColorChannel, shadedColorChannel, shadedColorChannel, 1f);
        resetColor();
    }

    public void resetColor() {
        batch.setColor(batchColor);
    }

    public void shadeByLight(byte lightLevel) {
        float mod = lightLevel / (float) Byte.MAX_VALUE;
        batch.setColor(batchColor.r * mod, batchColor.g * mod, batchColor.b * mod, batchColor.a);
    }

    public void begin() {
        batch.begin();
    }

    public void end() {
        batch.end();
    }
}
