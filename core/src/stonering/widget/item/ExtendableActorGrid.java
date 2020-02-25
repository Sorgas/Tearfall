package stonering.widget.item;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Actor grid that can be extended. Due to {@link Table} realization can only add entire rows.
 * 
 * @author Alexander on 25.02.2020.
 */
public class ExtendableActorGrid<T extends Actor> extends ActorGrid<T> {

    public ExtendableActorGrid(int cellWidth, int cellHeight) {
        super(cellWidth, cellHeight);
    }
    
    public void addRows(int number) {
        Cell[][] newArray = new Cell[cellWidth][cellHeight + number];
        System.arraycopy(gridCells, 0, newArray, 0, gridCells.length);
        gridCells = newArray;
        for (int y = cellHeight; y < cellHeight + number; y++) {
            for (int x = 0; x < cellWidth; x++) {
                gridCells[x][y] = add();
            }
            row();
        }
    }
}
