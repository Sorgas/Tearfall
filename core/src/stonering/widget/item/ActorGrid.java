package stonering.widget.item;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Table extension, that provides convenient methods for filling and modifying greed like structure.
 * Stores all added actors both in cells and two dimensional array for supporting modifying behavior.
 * Size in cells is specified on creation. TODO recreate table to allow changing size.
 * 
 * @author Alexander on 25.02.2020.
 */
public class ActorGrid extends Table {
    private Cell[][] gridCells;
    private int cellWidth;
    private int cellHeight;

    public ActorGrid(int cellWidth, int cellHeight) {
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        gridCells = new Cell[cellWidth][cellHeight];
        for (int y = 0; y < cellHeight; y++) {
            for (int x = 0; x < cellWidth; x++) {
                gridCells[x][y] = add(); // create table cell and save its reference
            }
            row();
        }
    }
    
    public boolean addActorToGrid(Actor actor) {
        Cell cell = findFirstFreeCell();
        if(cell == null) return false; // grid is full
        cell.setActor(actor);
        return true;
    }

    public Cell getCell(int x, int y) {
        return gridCells[x][y];
    }
    
    private Cell findFirstFreeCell() {
        for (int y = 0; y < cellHeight; y++) {
            for (int x = 0; x < cellWidth; x++) {
                if(gridCells[x][y].getActor() == null) return gridCells[x][y]; 
            }
        }
        return null;
    }
}
