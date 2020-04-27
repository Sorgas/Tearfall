package stonering.screen.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;

public class FpsCounter implements Disposable {
    private SpriteBatch batch;
    private BitmapFont font;
    private long lastSecond;
    private int counter;
    private int fps;
    private boolean enabled = true;
    
    public FpsCounter() {
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    public void init() {
        lastSecond = TimeUtils.millis();
        counter = 0;
        fps = 0;
    }
    
    public void render() {
        if(!enabled) return;
        update();
        batch.begin();
        font.draw(batch, "FPS: " + fps, 0, Gdx.graphics.getHeight() - 10);
        batch.end();
    }
    
    private void update() {
        counter++;
        if((TimeUtils.millis() - lastSecond) >= 1000) {
            lastSecond += 1000;
            fps = counter;
            counter = 0;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
