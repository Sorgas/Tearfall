package stonering.menu.ui_components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import stonering.menu.worldgen.WorldGenModel;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.WorldMap;
import stonering.generators.worldgen.world_objects.Edge;
import stonering.generators.worldgen.world_objects.Mountain;
import stonering.generators.worldgen.world_objects.Plate;
import stonering.global.utils.Position;
import stonering.global.utils.Vector;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Alexander on 16.04.2017.
 *
 * Can perform detailed render of world map for debug purposes
 */
public class MapDrawer {
	private WorldGenModel model;
	private WorldGenContainer container;
	private float tileSize = 2f;
	private float tileScale = 1f;
	private int tileOffsetX = 400;
	private int tileOffsetY = 20;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch spriteBatch;
	private Texture tiles;
	private boolean useTiles;

	public MapDrawer(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, Texture tiles) {
		this.shapeRenderer = shapeRenderer;
		this.spriteBatch = spriteBatch;
		this.tiles = tiles;
	}

	private void drawPlates() {
		List<Plate> plates = container.getPlates();
		for (Iterator<Plate> iterator = plates.iterator(); iterator.hasNext(); ) {
			Plate plate = iterator.next();
			Position center = plate.getCenter();
			drawPoint(center.getX(), center.getY());
			for (Iterator<Edge> edgeIterator = plate.getEdges().iterator(); edgeIterator.hasNext(); ) {
				Edge edge = edgeIterator.next();
				drawLine(edge.getPoint1().getX(), edge.getPoint1().getY(), edge.getPoint2().getX(), edge.getPoint2().getY());
			}
			Vector vector = plate.getSpeedVector();
			Position endPoint = vector.getEndPoint();
			drawLine(vector.getX(), vector.getY(), endPoint.getX(), endPoint.getY());
		}
	}

	private void drawMountains() {
		List<Edge> edges = container.getEdges();
		for (Iterator<Edge> iterator = edges.iterator(); iterator.hasNext(); ) {
			Edge edge = iterator.next();
			List<Mountain> mountains = edge.getMountains();
			for (Iterator<Mountain> mountainIterator = mountains.iterator(); mountainIterator.hasNext(); ) {
				Mountain mountain = mountainIterator.next();
				Position top = mountain.getTop();
				drawPoint(top.getX(), top.getY());
				for (Iterator<Position> cornerIterator = mountain.getCorners().iterator(); cornerIterator.hasNext(); ) {
					Position corner = cornerIterator.next();
					drawLine(top.getX(), top.getY(), corner.getX(), corner.getY());
				}
			}
		}
	}

	private void drawValleys() {
		List<Edge> edges = container.getEdges();
		for (Iterator<Edge> iterator = edges.iterator(); iterator.hasNext(); ) {
			Edge edge = iterator.next();
			List<Mountain> valleys = edge.getValleys();
			for (Iterator<Mountain> valleyIterator = valleys.iterator(); valleyIterator.hasNext(); ) {
				Mountain valley = valleyIterator.next();
				Position top = valley.getTop();
				drawPoint(top.getX(), top.getY());
				for (Iterator<Position> cornerIterator = valley.getCorners().iterator(); cornerIterator.hasNext(); ) {
					Position corner = cornerIterator.next();
					drawLine(top.getX(), top.getY(), corner.getX(), corner.getY());
				}
			}
		}
	}

	private void drawElevation() {
		int elevationMod = 16;
		WorldMap map = container.getMap();
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				float el = map.getElevation(x,y);
				if (useTiles) {
					if (el < 9) {
						if (el < 3) {
							drawTile(x, y, 2, 1);
						} else {
							drawTile(x, y, 2, 0);
						}
					} else {
						if (el < 15) {
							drawTile(x, y, 1, 1);
						} else {
							drawTile(x, y, 1, 0);
						}
					}
				} else {
					if (el < 9) {
						shapeRenderer.setColor(0.00f * (el + elevationMod), 0.015f * (el + elevationMod), 0.00f * (el + elevationMod), 1f);
					} else {
						el -= 12;
						float grey = 0.021f * (el + elevationMod);
						shapeRenderer.setColor(grey, grey, grey, 1f);
					}
					drawPoint(x, y);
				}
			}
		}
	}

	private void drawOceans() {
		WorldMap map = container.getMap();
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				int elevation = map.getElevation(x,y);
				if (elevation < container.getConfig().getSeaLevel()) {
					if(useTiles) {
						if (elevation > -5) {
							drawTile(x, y, 0, 0);
						} else {
							drawTile(x, y, 0, 1);
						}
					} else {
						shapeRenderer.setColor(0, 0, 0.03f * (15 + elevation), 1f);
						drawPoint(x, y);
					}

				}
			}
		}
	}

	private void drawSlopes() {
		WorldMap map = container.getMap();
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				if (map.getElevation(x,y) < container.getConfig().getSeaLevel()) {
					shapeRenderer.setColor(0, 0, 0.005f * Math.abs((container.getSlopeAngles(x, y) % 360) - 180), 1);
				} else {
					shapeRenderer.setColor(0, 0.005f * Math.abs((container.getSlopeAngles(x, y) % 360) - 180), 0, 1);
				}
				drawPoint(x, y);
			}
		}
	}

	private void drawRivers() {
		WorldMap map = container.getMap();
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				if (map.getRivers().containsKey(new Position(x,y,0))) {
					if(useTiles) {

					} else {
						shapeRenderer.setColor(0, 0, 1, 1);
						drawPoint(x, y);
					}
				}
			}
		}
	}

	private void drawLakes() {
		for (Iterator<Position> iterator = container.getLakes().iterator(); iterator.hasNext(); ) {
			Position pos = iterator.next();
			shapeRenderer.setColor(1, 0, 0, 1);
			drawPoint(pos.getX(), pos.getY());
		}
	}

	private void drawTemperature() {
		for (int x = 0; x < container.getConfig().getWidth(); x++) {
			for (int y = 0; y < container.getConfig().getHeight(); y++) {
				if (container.getTemperature(x, y) < -12) {
					shapeRenderer.setColor(1, 1, 1, 1); //
				} else if (container.getTemperature(x, y) < -0) {
					shapeRenderer.setColor(0.3f, 0.7f, 1, 1); //
				} else if (container.getTemperature(x, y) < 8) {
					shapeRenderer.setColor(0.3f, 1, 0.6f, 1); //
				} else if (container.getTemperature(x, y) < 22) {
					shapeRenderer.setColor(0.8f, 1, 0.2f, 1);
				} else if (container.getTemperature(x, y) < 27) {
					shapeRenderer.setColor(1, 0.8f, 0, 1);
				} else {
					shapeRenderer.setColor(1, 0.4f, 0, 1);
				}
//				shapeRenderer.setColor(0.01f * (container.getTemperature(x, y) + 40), 0, 0, 0.5f);
				drawPoint(x + 500, y);
			}
		}
	}

	private void drawDebug() {
		for (int x = 0; x < container.getMap().getWidth(); x++) {
			for (int y = 0; y < container.getMap().getHeight(); y++) {
				shapeRenderer.setColor(0, 0.5f * (container.getTemperature(x, y) + 0.1f), 0, 1);
				drawPoint(x, y);
			}
		}
	}

	private void drawLine(int x1, int y1, int x2, int y2) {
		shapeRenderer.line(x1 * tileSize + tileSize / 2 + tileOffsetX, y1 * tileSize + tileSize / 2 + tileOffsetY,
				x2 * tileSize + tileSize / 2 + tileOffsetX, y2 * tileSize + tileSize / 2 + tileOffsetY);
	}

	private void drawPoint(int x, int y) {
		shapeRenderer.rect(x * tileSize + tileOffsetX, y * tileSize + tileOffsetY,
				tileSize, tileSize);
	}

	private void drawTile(int x, int y, int tileX, int tileY) {
		spriteBatch.draw(new TextureRegion(tiles, tileX * 8, tileY * 8, 8, 8), tileOffsetX + x * 8 * tileScale, tileOffsetY + y * 8 * tileScale, 8 * tileScale, 8 * tileScale);
	}

	public void setModel(WorldGenModel model) {
		this.model = model;
	}
}
