package stonering.game.view.render.ui.lists;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import stonering.entity.local.AspectHolder;
import stonering.entity.local.zone.Zone;
import stonering.game.GameMvc;
import stonering.util.global.CompatibleArray;
import stonering.util.global.StaticSkin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Shows items, divided to categories.
 *
 * @author Alexander on 11.11.2018.
 */
public class ObservingList extends Window {
    private NavigableList<AspectHolder> list;
    private Consumer<AspectHolder> selectHandler;


    public ObservingList(Consumer<AspectHolder> selectHandler) {
        this(null, selectHandler);
    }

    public ObservingList(List<AspectHolder> aspectHolders, Consumer<AspectHolder> selectHandler) {
        super("qwer", StaticSkin.getSkin());
        this.selectHandler = selectHandler;
        fillList(aspectHolders);
        createListener();
        createWindow();
    }

    private void createWindow() {
        setSize(800, 600);
        setWidth(800);
    }

    public void fillList(List<AspectHolder> aspectHolders) {
        list = new NavigableList<>();
        add(list).width(400).height(700);
        if (aspectHolders == null) return;
        list.getItems().addAll(new CompatibleArray<>(aspectHolders));

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
        GameMvc.getInstance().getView().removeStage(getStage());
    }
}
