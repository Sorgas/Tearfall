package stonering.stage.toolbar;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import stonering.enums.images.DrawableMap;
import stonering.game.GameMvc;
import stonering.stage.job_menu.UnitJobMenu;
import stonering.widget.lists.IconTextButton;

/**
 * @author Alexander on 06.07.2020.
 */
public class RightBar extends Container<Table> {
    private Table rowTable;
    private Container<Actor> menu;

    public RightBar() {
        createLayout();
        
        IconTextButton button = new IconTextButton(DrawableMap.ICON.getDrawable("units_button"), null);
        button.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menu.setActor(new UnitJobMenu());
            }
        });
        rowTable.add(button);
    }

    private void createLayout() {
        Table table = new Table();
        table.defaults().align(Align.bottomRight);
        table.add(menu = new Container<>());
        table.add(rowTable = new Table());
        rowTable.defaults().size(80, 80);
        align(Align.bottomRight);
        setActor(table);
        setFillParent(true);
    }
}
