package stonering.torefine.localgen.localmap;

import stonering.torefine.game.objects.GameObject;
import stonering.utils.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Core class of model. represents one unit op space on local map
 */
public class MapCell {
	private static final long serialVersionUID = -925294699394718208L;
	private Position position;
	private boolean wall;
	private boolean floor;
	private int cellTypeId;
	private boolean walkPassable;
	private boolean flyPassable;
	private List<GameObject> content;

	/**
	 * Constructor of MapCell
	 * @param position position on local map
	 * @param id id of tile in model
	 * @param floor if there is a floor in cell
	 * @param wall if there is a wall in cell
	 */
	public MapCell(Position position, int id, boolean floor, boolean wall) {
		this.position = position;
		this.cellTypeId = id;
		this.floor = floor;
		this.wall = wall;
		this.content = new ArrayList<GameObject>();
	}

	/**
	 * constructor for cloning existing Cell
	 * @param cell cell to clone
	 */
	public MapCell(MapCell cell) {
		this.position = new Position(cell.getPosition().getX(), cell.getPosition().getY(), cell.getPosition().getZ());
		this.wall = cell.isWall();
		this.floor = cell.isFloor();
		this.walkPassable = cell.isWalkPassable();
		this.flyPassable = cell.isFlyPassable();
		this.cellTypeId = cell.getCellTypeId();
		this.content = cell.getContent().subList(0, cell.getContent().size());
	}
	
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public boolean isWall() {
		return wall;
	}
	public void setWall(boolean wall) {
		this.wall = wall;
	}
	public boolean isFloor() {
		return floor;
	}
	public void setFloor(boolean floor) {
		this.floor = floor;
	}
	public int getCellTypeId() {
		return cellTypeId;
	}
	public void setCellTypeId(int cellTypeId) {
		this.cellTypeId = cellTypeId;
	}
	public boolean isWalkPassable() {
		return walkPassable;
	}
	public void setWalkPassable(boolean walkPassable) {
		this.walkPassable = walkPassable;
	}
	public boolean isFlyPassable() {
		return flyPassable;
	}
	public void setFlyPassable(boolean flyPassable) {
		this.flyPassable = flyPassable;
	}
	public List<GameObject> getContent() {
		return content;
	}
	public void addContent(GameObject object) {
		content.add(object);
	}
}