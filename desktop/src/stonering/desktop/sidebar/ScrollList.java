package stonering.desktop.sidebar;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import stonering.desktop.demo.sidebar.DelayedScrollPane;
import stonering.util.global.StaticSkin;

/**
 * @author Alexander on 26.12.2019.
 */
public class ScrollList extends ScrollPane {

    public ScrollList(int align) {
        super(createTable(align));
        layout();
    }

    private static Table createTable(int align) {
        align = normalizeAlign(align);
        boolean vertical = align != Align.top && align != Align.bottom;
        Table table = new Table();
        for (int i = 0; i < 30; i++) {
            Button button = new TextButton("button " + i, StaticSkin.getSkin());
            Cell cell = table.add(button).size(vertical ? 200 : 50, vertical ? 50 : 200);
            if(vertical) cell.row();
        }
        return table;
    }

    /**
     * Handles non orthogonal {@link Align} values. Horizontal values is prioritized.
     */
    private static int normalizeAlign(int align) {
        switch (align) {
            case Align.bottom:
            case Align.top:
            case Align.left:
            case Align.right:
                return align;
            case Align.topLeft:
            case Align.bottomLeft:
                return Align.left;
            case Align.topRight:
            case Align.bottomRight:
                return Align.right;
        }
        throw new IllegalArgumentException("Value " + align + " is not part of com.badlogic.gdx.utils.Align");
    }
}
