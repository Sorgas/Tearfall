package stonering.test_chamber;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import stonering.test_chamber.screen.TestChamberScreen;

public class TestChamberGame extends Game {
    private TestChamberScreen screen;
    private BitmapFont font;
    private Skin skin;

    @Override
    public void create() {
        createFont();
        createSkin();
        showTestSelectScreen();
    }

    private void showTestSelectScreen() {
        if(screen == null) screen = new TestChamberScreen(this);
        setScreen(screen);
    }

    private void createFont() {
        font = new BitmapFont();
        font.setColor(0.2f, 0.2f, 0.2f, 1);
    }

    private void createSkin() {
        TextureAtlas atlas = new TextureAtlas(new FileHandle("ui_skin/uiskin.atlas"));
        skin = new Skin(new FileHandle("ui_skin/uiskin.json"), atlas);
    }
}
