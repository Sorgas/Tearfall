package stonering.stage.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

import stonering.GameSettings;
import stonering.util.geometry.Position;

import static stonering.stage.renderer.BatchUtil.*;

/**
 * Provides utility methods and rules for drawing sprites.
 *
 * @author Alexander on 06.02.2019.
 */
public class SpriteDrawingUtil {
    private Batch batch;
    public final BitmapFont font;
    public final float FONT_HEIGHT;
    private final float shadingStep = 0.06f;
    public final int maxZLevels = (int) (1f / shadingStep); // levels further are shaded to black
    private Color defaultColor = new Color();               // default batch color without light or transparency

    public SpriteDrawingUtil(Batch batch) {
        // TODO replace with skin with custom font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/SMW_Text_2_NC.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Math.round(24 * GameSettings.UI_SCALE.VALUE);
        font = generator.generateFont(parameter);
        generator.dispose();
        this.batch = batch;
        batch.enableBlending();
        FONT_HEIGHT = font.getCapHeight();
    }

    public void drawSprite(TextureRegion sprite, Position position) {
        drawSprite(sprite, position.x, position.y, position.z);
    }

    public void drawSprite(TextureRegion sprite, Vector3 vector) {
        drawSprite(sprite, vector.x, vector.y, vector.z);
    }

    public void drawSprite(TextureRegion sprite, float x, float y, float z) {
        batch.draw(sprite, getBatchX(x), getBatchY(y, z));
    }

    public void drawScale(TextureRegion sprite, Position position, int width, int height) {
        batch.draw(sprite, getBatchX(position.x), getBatchY(position.y, position.z), width, height);
    }

    public void draw() {

    }

    /**
     * Draws sprite with rotation around sprite center.
     */
    public void drawSpriteWithRotation(TextureRegion sprite, Vector3 vector, float rotation) {
        int width = sprite.getRegionWidth();
        int height = sprite.getRegionHeight();
        batch.draw(sprite, getBatchX(vector.x), getBatchY(vector.y, vector.z), width / 2f, height / 2f, width, height, 1, 1, rotation);
    }

    public void writeText(String text, Vector3 vector) {
        writeText(text, vector.x, vector.y, vector.z);
    }

    public void writeText(String text, Vector3 vector, int targetWidth, int align) {
        writeText(text, vector.x, vector.y, vector.z, targetWidth, align);
    }

    public void writeText(String text, float x, float y, float z) {
        writeText(text, x, y, z, 0, Align.left);
    }

    public void writeText(String text, float x, float y, float z, int targetWidth, int align) {
        float screenX = getBatchX(x);
        float screenY = getBatchY(y, z);
        font.draw(batch, text, screenX, screenY, targetWidth, align, false);
    }

    /**
     * Draws icon upon tile with given position. sprite position will be corrected with index.
     * Icon size is 16x16, and it is drawn on top left corner of a sprite.
     */
    public void drawIcon(TextureRegion sprite, Vector3 vector, int index) {
        batch.draw(sprite, getBatchXForIcon(vector.x, index), getBatchYForIcon(vector.y, vector.z));
    }

    public void updateColorA(float a) {
        Color color = batch.getColor();
        batch.setColor(color.r, color.g, color.b, a);
    }

    /**
     * Shades batch color to correspond lowering z coord.
     */
    public void shadeByZ(int dz) {
        float shadedColorChannel = 1 - (dz == 0 ? dz : dz + 3) * shadingStep;
        defaultColor.set(shadedColorChannel, shadedColorChannel, shadedColorChannel, 1f);
        resetColor();
    }

    public void setColor(Color color) {
        batch.setColor(color);
    }

    public void resetColor() {
        batch.setColor(defaultColor);
    }

    public void shadeByLight(byte lightLevel) {
        float mod = lightLevel / (float) Byte.MAX_VALUE;
        batch.setColor(defaultColor.r * mod, defaultColor.g * mod, defaultColor.b * mod, defaultColor.a);
    }
}
