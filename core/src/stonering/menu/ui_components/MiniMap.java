package stonering.menu.ui_components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.generators.worldgen.WorldMap;
import stonering.generators.worldgen.world_objects.Edge;
import stonering.generators.worldgen.world_objects.Mountain;
import stonering.generators.worldgen.world_objects.Plate;
import stonering.global.utils.Position;

/**
 * Created by Alexander on 19.04.2017.
 * <p>
 * UI component which renders minimap
 */
public class MiniMap extends Table {
    private WorldMap map;
    private TileChooser tileChooser;
    private float tileScale = 1f;
    private int tileSize = 8;
    private Position focus = new Position(0, 0, 0);
    private Position size = new Position(0, 0, 0);
    private ShapeRenderer shapeRenderer;

    public MiniMap(Texture tiles) {
        super();
        tileChooser = new TileChooser(tiles);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        drawTiles(batch);
        drawDebug(batch);
    }

    private void drawTiles(Batch batch) {
        if (map != null) {
            updateSize();
            int xStart = Math.max(focus.getX() - (size.getX() / 2), 0);
            xStart = Math.min(xStart, map.getWidth() - size.getX());
            int yStart = Math.max(focus.getY() - (size.getY() / 2), 0);
            yStart = Math.min(yStart, map.getHeight() - size.getY());
            for (int x = 0; x < size.getX(); x++) {
                for (int y = 0; y < size.getY(); y++) {
                    drawTile(batch, tileChooser.getTile(xStart + x, yStart + y), x, y);
                }
            }
            drawTile(batch, tileChooser.getCross(), focus.getX() - xStart, focus.getY() - yStart);
        }
    }

    private void drawDebug(Batch batch) {
        batch.end();
        if (map != null) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            for (int x = 0; x < map.getWidth(); x++) {
                for (int y = 0; y < map.getHeight(); y++) {
//                    shapeRenderer.setColor(getColorByRainFall(map.getRainfall(x, y));
//                    shapeRenderer.rect(350 + x * 16, y * 8, 8, 8);
//                    shapeRenderer.flush();
                    float seaLevel = 1.5f;
                    float elevation = map.getElevation(x, y) - seaLevel;
                    if (elevation > 0) {
                        if (elevation > 1) {
                            float white = (elevation) / 3 + 0.15f;
                            shapeRenderer.setColor(new Color(white, white, white, 0));
                        } else {
                            float green = (elevation) / 4 + 0.15f;
                            shapeRenderer.setColor(new Color(0, green, 0, 0));
                        }
                    } else {
                        float blue = (seaLevel + elevation) / seaLevel;
                        shapeRenderer.setColor(new Color(0, 0, blue, 0));
                    }
                    shapeRenderer.rect(358 + x * 2, 100 + y * 2, 2, 2);

//                    shapeRenderer.setColor(new Color((map.getSummerTemperature(x, y) + 40) / 80f, 0, 0, 0));
//                    shapeRenderer.rect(658 + x * 2, 100 + y * 2, 2, 2);
//                    shapeRenderer.setColor(new Color((map.getWinterTemperature(x, y) + 40) / 80f, 0, 0, 0));
//                    shapeRenderer.rect(958 + x * 2, 100 + y * 2, 2, 2);
                    shapeRenderer.setColor(new Color(map.getRainfall(x, y) / 100f, 0, 0, 0));
                    shapeRenderer.rect(958 + x * 2, 100 + y * 2, 2, 2);
                    shapeRenderer.flush();
                }
            }
//            shapeRenderer.setColor(1, 0, 0, 1);
//            for (Plate plate : map.getPlates()) {
//                for (Edge edge : plate.getEdges()) {
//                    for (Mountain mountain : edge.getMountains()) {
//                        for (Position corner : mountain.getCorners()) {
//                            shapeRenderer.line(358 + corner.getX() * 2,
//                                    100 + corner.getY() * 2,
//                                    358 + mountain.getTop().getX() * 2,
//                                    100 + mountain.getTop().getY() * 2);
//                        }
//                    }
//                }
//            }
            shapeRenderer.end();
        }
        batch.begin();
    }

    private Color getColorByRainFall(float rainfall) {
        return new Color(rainfall / 450.f, 0, 0, 0);
    }

    public void updateSize() {
        size.setX((int) (getWidth() / tileSize / tileScale));
        if (map != null && size.getX() > map.getWidth()) {
            size.setX(map.getWidth());
        }
        size.setY((int) (getHeight() / tileSize / tileScale));
        if (map != null && size.getY() > map.getHeight()) {
            size.setY(map.getHeight());
        }
    }

    public void setFocus(int x, int y) {
        focus.setX(x);
        focus.setY(y);
    }

    public void moveFocus(int dx, int dy) {
        if (map != null) {
            focus.setX(focus.getX() + dx);
            focus.setY(focus.getY() + dy);
            if (focus.getX() < 0)
                focus.setX(0);
            if (focus.getX() >= map.getWidth())
                focus.setX(map.getWidth() - 1);
            if (focus.getY() < 0)
                focus.setY(0);
            if (focus.getY() >= map.getHeight())
                focus.setY(map.getHeight() - 1);
        }
    }

    public void setMap(WorldMap map) {
        if (map != null) {
            this.map = map;
            tileChooser.setMap(map);
            focus.setX(map.getWidth() / 2);
            focus.setY(map.getHeight() / 2);
            updateSize();
        }
    }

    @Override
    public float getPrefWidth() {
        if (map != null) {
            return map.getWidth() * tileScale * tileSize;
        } else {
            return super.getPrefWidth();
        }
    }

    @Override
    public float getPrefHeight() {
        if (map != null) {
            return map.getHeight() * tileScale * tileSize;
        } else {
            return super.getPrefHeight();
        }
    }

    private void drawTile(Batch batch, TextureRegion tile, int x, int y) {
        batch.draw(tile, getX() + x * tileSize * tileScale, getY() + y * tileSize * tileScale);
    }

    public Position getFocus() {
        return focus.clone();
    }
}
