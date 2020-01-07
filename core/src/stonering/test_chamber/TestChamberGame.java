package stonering.test_chamber;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import stonering.test_chamber.screen.TestChamberScreen;
import stonering.widget.GameWithCustomCursor;

public class TestChamberGame extends GameWithCustomCursor {
    private TestChamberScreen screen;
    private BitmapFont font;

    @Override
    public void create() {
        super.create();
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
