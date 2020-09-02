package stonering.widget;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import stonering.util.lang.Initable;
import stonering.util.lang.Pair;
import stonering.util.lang.StaticSkin;
import stonering.widget.util.KeyNotifierListener;

/**
 * Table with row of buttons, separator row and content section.
 *
 * @author Alexander on 5/13/2020
 */
public class TabbedPane extends Table implements Initable {
    private final Table buttonTable;
    private final ButtonGroup<TextButton> buttonGroup = new ButtonGroup<>();
    private final Container<Actor> contentContainer;
    
    private final Map<String, Pair<TextButton, Actor>> contentMap = new HashMap<>();
    private final List<String> tabList = new ArrayList<>();
    private String selectedContent;

    public TabbedPane() {
        add(buttonTable = new Table()).expandX().left().row();
        buttonTable.defaults().size(100, 25).fill();
        buttonTable.left();
        add(new Image()).growX().row();
        add(contentContainer = new Container<>().fill()).grow().align(Align.topLeft);
        addListener(new InputListener() { // listener that switches tabs
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode != Input.Keys.TAB || tabList.isEmpty()) return false;
                switchTab(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? -1 : 1); // move to next or previous
                return true;
            }
        });
        addListener(new KeyNotifierListener(this::getSelected)); // listener that passes input to content
    }

    public void add(String tabTitle, Actor content) {
        tabList.add(tabTitle);
        TextButton button = new TextButton(tabTitle, StaticSkin.getSkin());
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                select(tabTitle);
            }
        });
        buttonGroup.add(button);
        buttonTable.add(button);
        Pair<TextButton, Actor> pair = new Pair<>(button, content);
        contentMap.put(tabTitle, pair);
        if (contentMap.size() == 1) select(tabTitle);
    }

    public void select(String title) {
        if (!contentMap.containsKey(title)) return;
        Actor previousActor = contentContainer.getActor();
        if (previousActor instanceof Restoreable) {
            ((Restoreable) previousActor).saveState();
        }
        Pair<TextButton, Actor> pair = contentMap.get(title);
        Actor newActor = pair.getValue();
        contentContainer.setActor(newActor);
        selectedContent = title;
        if (newActor instanceof Restoreable) {
            ((Restoreable) newActor).restoreState();
        }
    }

    public Actor getSelected() {
        return contentMap.get(selectedContent).getValue();
    }

    public void switchTab(int delta) {
        int index = tabList.indexOf(selectedContent) + delta;
        if (index < 0) index = tabList.size() - 1; // loop tabs
        if (index >= tabList.size()) index = 0;
        select(tabList.get(index));
    }
    
    @Override
    public void init() {
        if (contentContainer.getActor() instanceof Initable) ((Initable) contentContainer.getActor()).init();
    }
}
