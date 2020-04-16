package stonering.stage.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import stonering.util.geometry.Position;

import static stonering.stage.renderer.BatchUtil.*;

/**
 * Provides utility methods and rules for drawing sprites.
 *
 * @author Alexander on 06.02.2019.
 */
public class SpriteDrawingUtil {
    private Batch batch;
    private BitmapFont font;

    private final float shadingStep = 0.06f;
    public final int maxZLevels = (int) (1f / shadingStep); // levels further are shaded to black
    private Color batchColor;               // default batch color without light or transparency

    public SpriteDrawingUtil(Batch batch) {
        this.batch = batch;
        font = new BitmapFont();
        batch.enableBlending();
        batchColor = new Color();
    }

    /**
     * Draws sprite on localMap position. Handles sprites of non-standard sizes.
     */
    public void drawSprite(TextureRegion sprite, AtlasesEnum atlas, Position position) {
        batch.draw(sprite, getBatchX(position.x) + atlas.X_CORRECTION, getBatchY(position.y, position.z) + atlas.Y_CORRECTION);
    }

    public void drawSprite(TextureRegion sprite, Position position) {
        drawSprite(sprite, position.x, position.y, position.z);
    }

    /**
     * Draws sprite by vector. Values can be fractional, sprite will be rendered between cells.
     */
    public void drawSprite(TextureRegion sprite, Vector3 vector) {
        drawSprite(sprite, vector.x, vector.y, vector.z);
    }

    public void drawSprite(TextureRegion sprite, float x, float y, float z) {
        batch.draw(sprite, getBatchX(x), getBatchY(y, z));
    }

    public void drawScale(TextureRegion sprite, Position position, int width, int height) {
        batch.draw(sprite, getBatchX(position.x), getBatchY(position.y, position.z), width, height);
    }

    public void writeText(String text, int x, int y, int z) {
        float screenX = getBatchX(x);
        float screenY = getBatchY(y, z);
        font.draw(batch, text, screenX, screenY);
    }

    /**
     * Draws icon upon tile with given position. sprite position will be corrected with index.
     * Icon size is 16x16, and it is drawn on top left corner of a sprite.
     */
    public void drawIcon(TextureRegion sprite, Vector3 vector, int index) {
        batch.draw(sprite, getBatchXForIcon(vector.x, index), getBatchYForIcon(vector.y, vector.z));
    }

    /**
     * Makes color transparent.
     */
    public void updateColorA(float a) {
        Color color = batch.getColor();
        batch.setColor(color.r, color.g, color.b, a);
    }

    /**
     * Shades batch color to correspond lowering z coord.
     */
    public void shadeByZ(int dz) {
        float shadedColorChannel = 1 - (dz - 4) * shadingStep;
        batchColor.set(shadedColorChannel, shadedColorChannel, shadedColorChannel, 1f);
        resetColor();
    }

    public void setColor(Color color) {
        batch.setColor(color);
    }
    
    public void resetColor() {
        batch.setColor(batchColor);
    }

    public void shadeByLight(byte lightLevel) {
        float mod = lightLevel / (float) Byte.MAX_VALUE;
        batch.setColor(batchColor.r * mod, batchColor.g * mod, batchColor.b * mod, batchColor.a);
    }
}
