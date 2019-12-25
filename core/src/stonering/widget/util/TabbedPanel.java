package stonering.widget.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.util.global.Pair;
import stonering.util.global.StaticSkin;

import java.util.HashMap;
import java.util.Map;

/**
 * Panel with button group and content pane.
 * Toggling buttons switches content int the pane.
 *
 * @author Alexander on 23.12.2019.
 */
public abstract class TabbedPanel extends VerticalGroup {
    private int contentWidth;
    private int contantHeight;
    private Map<Integer, Pair<Button, Container>> mapping;
    private HorizontalGroup group;
    ButtonGroup<Button> buttonGroup;
    Container<Container> container; // contains dynamic content

    public TabbedPanel() {
        mapping = new HashMap<>();
        createTable();
    }

    private void createTable() {
        addActor(group = new HorizontalGroup());
        addActor(container = new Container<>());
    }

    /**
     * Adds tab to panel. Content is added to wrapping container and always sized to panel's content size.
     */
    public void addTab(String caption, Container content, int hotkey) {
        Button button = new TextButton(caption, StaticSkin.getSkin());
        mapping.put(hotkey, new Pair<>(button, content));
        group.addActor(button);
        buttonGroup.add(button);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectTab(hotkey);
            }
        });
    }

    public void selectTab(int hotkey) {
        Pair<Button, Container> pair = mapping.get(hotkey);
        if(pair == null) return;
        container.setActor(mapping.get(hotkey).getValue());
//        buttonGroup.setChecked();
    }
}
