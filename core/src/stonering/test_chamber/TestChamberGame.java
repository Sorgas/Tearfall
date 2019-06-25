package stonering.test_chamber;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import stonering.test_chamber.screen.TestChamberScreen;

public class TestChamberGame extends Game {
    private TestChamberScreen screen;
    private BitmapFont font;

    @Override
    public void create() {
        createFont();
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
}
