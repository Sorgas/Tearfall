package stonering.game.core.view.render.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;

/**
 * @author Alexander Kuzyakov on 14.06.2018.
 */
public class VerticalButtonGroup<T extends Button> extends Table {
    private ArrayList<T> buttons;

    private void updateButtons() {
        buttons.forEach(button -> {
            this.add(button);
            this.row();
        });
    }

    public ArrayList<T> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<T> buttons) {
        this.buttons = buttons;
        clearChildren();
        updateButtons();
    }
}
