package stonering.menu.ui_components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.generators.worldgen.WorldMap;
import stonering.global.utils.Position;

/**
 * UI component which renders minimap
 *
 * @author Alexander Kuzyakov on 19.04.2017.
 */
public class MiniMap extends Table {
    private WorldMap map;
    private TileChooser tileChooser;
    private float tileScale = 1f;
    private int tileSize = 8;
    private Position focus = new Position(0, 0, 0);
    private Position size = new Position(0, 0, 0);
    private ShapeRenderer shapeRenderer;
    private int pixelSize = 5;
    private int baseScreenOffsetX = 385;
    private int screenOffsetY = 100;
    private boolean debugMode = true;

    public MiniMap(Texture tiles) {
        super();
        tileChooser = new TileChooser(tiles);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (map != null) {
            if (debugMode) {
                drawDebug(batch);
            } else {
                drawTiles(batch);
            }
        }
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
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawElevationDebug(0);
//        drawTemperatureDebug(map.getWidth() * pixelSize);
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        drawRiverVectorsDebug(map.getWidth() * 1 * pixelSize);
        shapeRenderer.flush();
        shapeRenderer.end();
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

    private void drawElevationDebug(int screenOffsetX) {
        if (map != null) {
            for (int x = 0; x < map.getWidth(); x++) {
                for (int y = 0; y < map.getHeight(); y++) {
                    shapeRenderer.setColor(getElevationColor(x, y));
                    shapeRenderer.rect(screenOffsetX + baseScreenOffsetX + x * pixelSize, screenOffsetY + y * pixelSize, pixelSize, pixelSize);
                }
            }
        }
    }

    private void drawTemperatureDebug(int screenOffsetX) {
        if (map != null) {
            for (int x = 0; x < map.getWidth(); x++) {
                for (int y = 0; y < map.getHeight(); y++) {
                    shapeRenderer.setColor(getTemperatureColor(x, y));
                    shapeRenderer.rect(baseScreenOffsetX + screenOffsetX + x * pixelSize, screenOffsetY + y * pixelSize, pixelSize, pixelSize);
                    shapeRenderer.setColor(getTemperatureColor(x, y));
                    shapeRenderer.rect(baseScreenOffsetX + (map.getWidth() + x) * pixelSize, screenOffsetY + y * pixelSize, pixelSize, pixelSize);
                }
            }
        }
    }

    private void drawRiverVectorsDebug(int screenOffsetX) {
        Color blue = new Color(0, 0, 1, 1);
        Color green = new Color(0, 1, 0, 1);
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Vector2 river = map.getRiver(x, y);
                Vector2 brook = map.getBrook(x, y);
                int bx = baseScreenOffsetX + screenOffsetX + x * pixelSize;
                int by = screenOffsetY + y * pixelSize;
                if (river != null) {
                    shapeRenderer.line(bx, by, bx + (river.x * pixelSize * 10), by + (river.y * pixelSize * 10), blue, blue);
                }
                if (brook != null) {
                    shapeRenderer.line(bx, by, bx + (brook.x * pixelSize), by + (brook.y * pixelSize), green, green);
                }
            }
        }
        shapeRenderer.setColor(1, 0, 0, 1);
        map.getLakes().forEach(position ->
                shapeRenderer.rect(baseScreenOffsetX + screenOffsetX + position.getX() * pixelSize,
                        screenOffsetY + position.getY() * pixelSize,
                        pixelSize, pixelSize)

        );

    }

    private Color getElevationColor(int x, int y) {
        float seaLevel = 0.5f;
        float elevation = map.getElevation(x, y) - seaLevel;
        if (elevation > 0) {
            if (map.getRiver(x, y) != null) {
                float blue = (seaLevel + elevation) / seaLevel;
                return new Color(0, 0, blue, 0);
            } else if (elevation > 0.25f) {
                float white = (elevation) / 3 + 0.15f;
                return new Color(white, white, white, 0);
            } else {
                float green = (elevation) / 4 + 0.15f;
                return new Color(0, green, 0, 0);
            }
        } else {
            float blue = (seaLevel + elevation) / seaLevel;
            return new Color(0, 0, blue, 0);
        }
    }

    private Color getTemperatureColor(int x, int y) {
        float temperature = map.getSummerTemperature(x, y);
        if (temperature < -20) { //[-35; -19]
            return new Color(0, 0, (temperature + 40) / 80f, 0);
        } else if (temperature < 0) { //[-20; -1]
            return new Color(0, 0, (temperature + 60) / 80f, 0);
        } else if (temperature < 20) { //[0; 19]
            return new Color(0, (temperature + 40) / 80f, 0, 0);
        } else { //[20; 35]
            return new Color((temperature + 40) / 80f, 0, 0, 0);
        }
    }

    public Position getFocus() {
        return focus.clone();
    }
}
