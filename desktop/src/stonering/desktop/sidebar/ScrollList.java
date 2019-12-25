package stonering.desktop.sidebar;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import stonering.util.global.StaticSkin;

/**
 * @author Alexander on 26.12.2019.
 */
public class ScrollList extends ScrollPane {

    public ScrollList() {
        super(createTable());
        this.layout();
    }

    private static Table createTable() {
        Table table = new Table();
        for (int i = 0; i < 10; i++) {
            Button button = new TextButton("qwer", StaticSkin.getSkin());
            table.add(button).size(150, 50).row();
        }
        table.layout();
//        Container container = new Container(table);
//        container.layout();
//        Container contaienr2 = new Container(container);
//        contaienr2.layout();
        return table;
    }
}
