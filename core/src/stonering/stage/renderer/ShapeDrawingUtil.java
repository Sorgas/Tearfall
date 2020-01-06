package stonering.stage.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import static stonering.stage.renderer.BatchUtil.*;

/**
 * Provides utility methods for drawing shapes in tile local coordinates.
 *
 * @author Alexander on 03.01.2020.
 */
public class ShapeDrawingUtil {
    public Batch batch;
    public ShapeRenderer shapeRenderer;

    public ShapeDrawingUtil(Batch batch) {
        this.batch = batch;
        shapeRenderer = new ShapeRenderer();
    }

    public void drawRectangle(Vector3 modelPosition, int x, int y, int width, int height, Color color) {
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        int batchX = (int) getBatchX(modelPosition.x) + x;
        int batchY = (int) getBatchY(modelPosition.y, modelPosition.z) + y;
        shapeRenderer.rect(batchX, batchY, width, height);
        shapeRenderer.end();
        batch.begin();
    }
}
