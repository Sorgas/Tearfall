package stonering.util.sprite;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;

import stonering.util.lang.Pair;

/**
 * Combines multiple {@link TextureRegion}s into one.
 * 
 * @author Alexander on 06.10.2020.
 */
public class TextureRegionCombiner {

    public TextureRegion combine(List<Pair<TextureRegion, Vector2>> regions) {
        return combine(regions, Color.WHITE);
    }

    public TextureRegion combine(List<Pair<TextureRegion, Vector2>> regions, Color tinting) {
        FrameBuffer buffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        SpriteBatch batch = new SpriteBatch();
        Vector2 totalSize = new Vector2();
        buffer.begin();
        Gdx.gl20.glClearColor(0, 0, 0, 0); // set transparent background
        batch.begin();
        batch.setColor(tinting);
        for (Pair<TextureRegion, Vector2> pair : regions) {
            batch.draw(pair.getKey(), pair.getValue().x, pair.getValue().y);
            totalSize.x = Math.max(totalSize.x, pair.getKey().getRegionWidth() + pair.getValue().x);
            totalSize.y = Math.max(totalSize.y, pair.getKey().getRegionHeight() + pair.getValue().y);
        }

        batch.end();
        buffer.end();

        TextureRegion region = new TextureRegion(buffer.getColorBufferTexture(), 0, 0, totalSize.x, totalSize.y);
        region.flip(false, true);
        return region;
    }
}
