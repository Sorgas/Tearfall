package stonering.desktop.demo.sidebar;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import stonering.util.lang.StaticSkin;

/**
 * Demo widget with fixed size and labels in the corners.
 *
 * @author Alexander on 23.12.2019.
 */
public class DemoWidget extends Table {

    public DemoWidget() {
        setWidth(200);
        setHeight(200);
        Label label = new Label("TL", StaticSkin.getSkin());
        label.setAlignment(Align.topLeft);
        add(label).size(100, 100);
        label = new Label("TR", StaticSkin.getSkin());
        label.setAlignment(Align.topRight);
        add(label).size(100, 100).row();
        label = new Label("BL", StaticSkin.getSkin());
        label.setAlignment(Align.bottomLeft);
        add(label).size(100, 100);
        label = new Label("BR", StaticSkin.getSkin());
        label.setAlignment(Align.bottomRight);
        add(label).size(100, 100);
        setDebug(true, true);
    }
}
