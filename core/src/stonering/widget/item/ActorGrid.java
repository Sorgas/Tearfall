package stonering.widget.item;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;

/**
 * Table extension, that provides convenient methods for filling and modifying greed like structure.
 * Stores all added actors both in cells and two dimensional array for supporting modifying behavior.
 * Size in cells is specified on creation. TODO recreate table to allow changing size.
 * Table::defaults() should be set before fill().
 * 
 * @author Alexander on 25.02.2020.
 */
public class ActorGrid<T extends Actor> extends Table {
    protected Cell<T>[][] gridCells;
    protected int cellWidth;
    protected int cellHeight;

    public ActorGrid(int cellWidth, int cellHeight) {
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        gridCells = new Cell[cellWidth][cellHeight];
    }

    public void init() {
        for (int y = 0; y < cellHeight; y++) {
            for (int x = 0; x < cellWidth; x++) {
                gridCells[x][y] = add(); // create table cell and save its reference
            }
            row();
        }
    }

    public boolean addActorToGrid(T actor) {
        Cell<T> cell = findFirstFreeCell();
        if(cell == null) return false; // grid is full
        cell.setActor(actor);
        return true;
    }

    public T getActor(int x, int y) {
        return gridCells[x][y].getActor();
    }
    
    private Cell<T> findFirstFreeCell() {
        for (int y = 0; y < cellHeight; y++) {
            for (int x = 0; x < cellWidth; x++) {
                if(gridCells[x][y].getActor() == null) return gridCells[x][y]; 
            }
        }
        return null;
    }
}