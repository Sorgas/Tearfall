package stonering.widget.item;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Table extension, that provides convenient methods for filling and modifying grid-like structure.
 * Stores all added actors both in cells and two dimensional array for supporting modifying behavior.
 * Size in cells is specified on creation. TODO recreate table to allow changing size.
 * Table::defaults() should be set before createGrid().
 *
 * @author Alexander on 25.02.2020.
 */
public class ActorGrid<T extends Actor> extends Table {
    protected List<List<Cell<T>>> rows;
    protected int cellWidth;

    public ActorGrid(int sellWidth) {
        this.cellWidth = sellWidth;
    }

    public void addActorToGrid(T actor) {
        List<Cell<T>> row = rows.get(rows.size());
        if(row.size() == cellWidth) {
            row = startNewRow();
        }
        row.add(add(actor));
    }

    private List<Cell<T>> startNewRow() {
        row();
        List<Cell<T>> row = new ArrayList<>();
        rows.add(row);
        return row;
    }

    public void clearGrid() {
        for (List<Cell<T>> row : rows) {
            for (Cell<T> cell : row) {
                cell.setActor(null);
            }
        }
    }
}
