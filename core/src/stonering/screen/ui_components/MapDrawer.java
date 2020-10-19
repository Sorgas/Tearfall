package stonering.screen.ui_components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import stonering.entity.world.TectonicPlate;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.entity.world.WorldMap;
import stonering.entity.world.Edge;
import stonering.stage.menu.worldgen.WorldGenMenu;
import stonering.util.geometry.Position;

import java.util.Iterator;
import java.util.List;

/**
 * @author Alexander Kuzyakov on 16.04.2017.
 * <p>
 * Can perform detailed render of world map for debug purposes
 */
public class MapDrawer {
    private WorldGenContainer container;
    private WorldGenMenu menu;
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
        List<TectonicPlate> plates = container.getTectonicPlates();
        for (Iterator<TectonicPlate> iterator = plates.iterator(); iterator.hasNext(); ) {
            TectonicPlate plate = iterator.next();
            Position center = plate.getCenter();
            drawPoint(center.x, center.y);
            for (Iterator<Edge> edgeIterator = plate.getEdges().iterator(); edgeIterator.hasNext(); ) {
                Edge edge = edgeIterator.next();
                drawLine(edge.getPoint1().x, edge.getPoint1().y, edge.getPoint2().x, edge.getPoint2().y);
            }
//            Vector vector = plate.getSpeedVector();
//            Position endPoint = vector.getEndPoint();
//            drawLine(Math.round(vector.x), Math.round(vector.y),
//                    Math.round(endPoint.x), Math.round(endPoint.y));
        }
    }

    private void drawMountains() {
//        List<Edge> edges = container.getEdges();
//        for (Iterator<Edge> iterator = edges.iterator(); iterator.hasNext(); ) {
//            Edge edge = iterator.next();
//            List<Mountain> mountains = edge.getMountains();
//            for (Iterator<Mountain> mountainIterator = mountains.iterator(); mountainIterator.hasNext(); ) {
//                Mountain mountain = mountainIterator.next();
//                Position top = mountain.getTop();
//                drawPoint(top.x, top.y);
//                for (Iterator<Position> cornerIterator = mountain.getCorners().iterator(); cornerIterator.hasNext(); ) {
//                    Position corner = cornerIterator.next();
//                    drawLine(top.x, top.y, corner.x, corner.y);
//                }
//            }
//        }
    }

    private void drawValleys() {
//        List<Edge> edges = container.getEdges();
//        for (Iterator<Edge> iterator = edges.iterator(); iterator.hasNext(); ) {
//            Edge edge = iterator.next();
//            List<Mountain> valleys = edge.getValleys();
//            for (Iterator<Mountain> valleyIterator = valleys.iterator(); valleyIterator.hasNext(); ) {
//                Mountain valley = valleyIterator.next();
//                Position top = valley.getTop();
//                drawPoint(top.x, top.y);
//                for (Iterator<Position> cornerIterator = valley.getCorners().iterator(); cornerIterator.hasNext(); ) {
//                    Position corner = cornerIterator.next();
//                    drawLine(top.x, top.y, corner.x, corner.y);
//                }
//            }
//        }
    }

    private void drawElevation() {
        int elevationMod = 16;
        WorldMap map = container.getMap();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                float el = map.getElevation(x, y);
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
        for (int x = 0; x < map.width; x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                float elevation = map.getElevation(x, y);
                if (elevation < container.config.seaLevel) {
                    if (useTiles) {
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
                if (map.getElevation(x, y) < container.config.seaLevel) {
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
//				if (map.getRivers().containsKey(new Position(x,y,0))) {
//					if(useTiles) {
//
//					} else {
//						shapeRenderer.setColor(0, 0, 1, 1);
//						drawPoint(x, y);
//					}
//				}
            }
        }
    }

    private void drawTemperature() {
        for (int x = 0; x < container.config.width; x++) {
            for (int y = 0; y < container.config.height; y++) {
                if (container.getSummerTemperature(x, y) < -12) {
                    shapeRenderer.setColor(1, 1, 1, 1); //
                } else if (container.getSummerTemperature(x, y) < -0) {
                    shapeRenderer.setColor(0.3f, 0.7f, 1, 1); //
                } else if (container.getSummerTemperature(x, y) < 8) {
                    shapeRenderer.setColor(0.3f, 1, 0.6f, 1); //
                } else if (container.getSummerTemperature(x, y) < 22) {
                    shapeRenderer.setColor(0.8f, 1, 0.2f, 1);
                } else if (container.getSummerTemperature(x, y) < 27) {
                    shapeRenderer.setColor(1, 0.8f, 0, 1);
                } else {
                    shapeRenderer.setColor(1, 0.4f, 0, 1);
                }
//				shapeRenderer.setColor(0.01f * (container.getSummerTemperature(x, y) + 40), 0, 0, 0.5f);
                drawPoint(x + 500, y);
            }
        }
    }

    private void drawDebug() {
        for (int x = 0; x < container.getMap().getWidth(); x++) {
            for (int y = 0; y < container.getMap().getHeight(); y++) {
                shapeRenderer.setColor(0, 0.5f * (container.getSummerTemperature(x, y) + 0.1f), 0, 1);
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

    public void setModel(WorldGenMenu menu) {
        this.menu = menu;
    }
}
