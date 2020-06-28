package stonering.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import stonering.util.global.Initable;
import stonering.util.global.Pair;
import stonering.util.global.StaticSkin;
import stonering.widget.util.KeyNotifierListener;

/**
 * Table with row of buttons, separator row and content section.
 *
 * @author Alexander on 5/13/2020
 */
public class TabbedPane extends Table implements Initable {
    public final Table buttonTable;
    public final Image separator;
    public final Container<Actor> contentContainer;
    private Map<String, Pair<TextButton, Actor>> contentMap;
    private List<String> tabList;
    private ButtonGroup<TextButton> buttonGroup;
    private String selectedContent;
    
    public TabbedPane(int width) {
        defaults().width(width);
        add(buttonTable = new Table()).left().row();
        add(separator = new Image()).fillX().row();
        add(contentContainer = new Container<>());
        buttonTable.defaults().size(100, 25).fill();
        buttonTable.left();
        contentMap = new HashMap<>();
        tabList = new ArrayList<>();
        buttonGroup = new ButtonGroup<>();
        addListener(new KeyNotifierListener(this::getSelectedContent) {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode != Input.Keys.TAB) return super.keyDown(event, keycode);
                int index = tabList.indexOf(selectedContent);
                if (index != -1) {
                    index = (index < tabList.size() - 1) ? ++index : 0; // get next or first tab
                    selectTab(tabList.get(index));
                }
                return true;
            }
        });
    }
    
    public void add(String tabTitle, Actor content) {
        tabList.add(tabTitle);
        TextButton button = new TextButton(tabTitle, StaticSkin.getSkin());
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectTab(tabTitle);
            }
        });
        buttonGroup.add(button);
        buttonTable.add(button);
        Pair<TextButton, Actor> pair = new Pair<>(button, content);
        contentMap.put(tabTitle, pair);
        if (contentMap.size() == 1) selectTab(tabTitle);
    }

    public void selectTab(String title) {
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

    public Actor getSelectedContent() {
        return contentMap.get(selectedContent).getValue();
    }
    
    @Override
    public void init() {
        if (contentContainer.getActor() instanceof Initable) ((Initable) contentContainer.getActor()).init();
    }
}
