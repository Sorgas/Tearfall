package stonering.game.core.view.ui_components;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.utils.global.StaticSkin;

/**
 * Created by Alexander on 28.12.2017.
 */
public class ToolStatus extends Table {
    private Label label;

    public ToolStatus() {
        super();
        createTable();
    }

    private void createTable() {
        this.pad(10);
        this.setFillParent(true);
        this.right().bottom();
        label = new Label("", StaticSkin.getSkin());
        this.add(label);
    }

    public void setText(String text) {
        label.setText(text);
    }
}
