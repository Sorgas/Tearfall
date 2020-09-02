package stonering.widget.lists;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import stonering.entity.Entity;
import stonering.game.GameMvc;
import stonering.util.lang.CompatibleArray;
import stonering.util.lang.StaticSkin;

import java.util.List;
import java.util.function.Consumer;

/**
 * Shows item, divided to categories.
 *
 * @author Alexander on 11.11.2018.
 */
public class ObservingList extends Window {
    private NavigableList<Entity> list;
    private Consumer<Entity> selectHandler;

    public ObservingList(List<Entity> entities, Consumer<Entity> selectHandler) {
        super("", StaticSkin.getSkin());
        this.selectHandler = selectHandler;
        fillList(entities);
        createListener();
        createWindow();
    }

    private void createWindow() {
        setSize(800, 600);
        setWidth(800);
    }

    public void fillList(List<Entity> entities) {
        list = new NavigableList<>();
        add(list).width(400).height(700);
        if (entities == null) return;
        list.getItems().addAll(new CompatibleArray<>(entities));
    }

    private void createListener() {
        addListener(
                new InputListener() {
                    @Override
                    public boolean keyDown(InputEvent event, int keycode) {
                        switch (keycode) {
                            case Input.Keys.Q: {
                                close();
                                return true;
                            }
                            case Input.Keys.E: {
                                select();
                                return true;
                            }
                        }
                        return list.fire(event);
                    }
                }
        );
    }

    private void select() {
        selectHandler.accept(list.getSelected());
    }

    private void close() {
        GameMvc.view().removeStage(getStage());
    }
}
