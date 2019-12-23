package stonering.widget.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Panel with button group and content pane.
 * Toggling buttons switches content int the pane.
 *
 * @author Alexander on 23.12.2019.
 */
public abstract class TabbedPanel <T extends Actor> extends Table {
    private Map<Button, T> buttonMapping;
    private HorizontalGroup group;
    ButtonGroup<Button> buttonGroup;
    Container<T> container;

    public TabbedPanel() {
        buttonMapping = new HashMap<>();
        createTable();
    }

    public void mapButton(Button button, T content) {
        buttonMapping.put(button, content);
        group.addActor(button);
        buttonGroup.add(button);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                container.setActor(buttonMapping.get(button));
            }
        });
    }

    private void createTable() {
        add(group = new HorizontalGroup()).row();
        add(container = new Container<>()).row();
    }
}
