package stonering.desktop.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import stonering.screen.util.SimpleScreen;
import stonering.stage.renderer.atlas.AtlasesEnum;
import stonering.stage.util.UiStage;
import stonering.widget.button.IconTextHotkeyButton;

/**
 * @author Alexander on 01.10.2020.
 */
public class FrameBufferDemo extends Game {
    IconTextHotkeyButton button;

    public static void main(String[] args) {
        new LwjglApplication(new FrameBufferDemo());
    }

    @Override
    public void create() {
        TextureRegion region = createTexture();
        setScreen(new SimpleScreen() {
            private UiStage stage = new UiStage();

            {
                stage.interceptInput = false;
                stage.addListener(new InputListener() {
                    @Override
                    public boolean keyDown(InputEvent event, int keycode) {
                        if (keycode == Input.Keys.ESCAPE) Gdx.app.exit();
                        return true;
                    }
                });
                Gdx.input.setInputProcessor(stage);
                stage.setDebugAll(true);
            }

            @Override
            public void render(float delta) {
                Gdx.gl.glClearColor(1, 0, 0, 1);
                Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
                stage.act(delta);
                stage.draw();
                stage.getBatch().begin();
                stage.getCamera().update();
                stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
                stage.getBatch().draw(region, 0, 0);
                stage.getBatch().end();
            }

            @Override
            public void resize(int width, int height) {
                stage.resize(width, height);
            }
        });
    }

    private TextureRegion createTexture() {
        TextureRegion qwer1 = AtlasesEnum.plants.getBlockTile("domestic", 2, 0);
        TextureRegion qwer2 = AtlasesEnum.plants.getBlockTile("domestic", 1, 0);
        FrameBuffer buffer = new FrameBuffer(Pixmap.Format.RGBA8888, 640, 480, false);
        SpriteBatch batch = new SpriteBatch();
        buffer.begin();
        Gdx.gl20.glClearColor(0, 0, 0, 0);
        batch.begin();
        batch.setColor(Color.WHITE);
        
        batch.draw(qwer1, 0, 0);
        batch.draw(qwer2, 16, 16);
        
        batch.end();
        buffer.end();
        TextureRegion region = new TextureRegion(buffer.getColorBufferTexture());
        TextureRegion region1 = new TextureRegion(region, 0, 0, 50, 200);
        region1.flip(false, true);
        System.out.println(region1.getRegionWidth());
        return region1;
    }
}
