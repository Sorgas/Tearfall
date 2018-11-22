package stonering.game.core.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.entity.local.building.validators.PositionValidator;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.global.utils.Position;

/**
 * Object selector and center for rendering.
 *
 * @author Alexander Kuzyakov on 10.12.2017.
 */
public class GameCamera {
    private GameContainer container;
    private LocalMap localMap;
    private Position camera;
    private TextureRegion textureRegion;
    private PositionValidator positionValidator;

    private Position frameStart;
    private Position frameEnd;

    public final int IDLE_STATUS = -1;
    public final int GREEN_STATUS = 0;
    public final int RED_STATUS = 1;
    private int status;

    public GameCamera(GameContainer container) {
        this.container = container;
        localMap = container.getLocalMap();
        initCamera();
    }

    private void initCamera() {
        camera = new Position(localMap.getxSize() / 2, localMap.getySize() / 2, localMap.getzSize() - 1);
        while (localMap.getBlockType(camera.getX(), camera.getY(), camera.getZ()) <= BlockTypesEnum.SPACE.getCode()) {
            camera.setZ(camera.getZ() - 1);
        }
    }

    public Position getPosition() {
        return camera;
    }

    public void moveCamera(int dx, int dy, int dz) {
        camera.setX(camera.getX() + dx);
        camera.setY(camera.getY() + dy);
        camera.setZ(camera.getZ() + dz);
        localMap.normalizePosition(camera);
        updateStatus();
    }

    public int getStatus() {
        return status;
    }

    public void updateStatus() {
        if() {

        }
        if(this.status != status) {
            textureRegion = new TextureRegion(new Texture("sprites/ui_tiles.png"), status * 64, 567, 64,96);
        }
        this.status = status;
    }

    public void resetSprite() {
        frameStart = null;
        frameEnd = null;
        updateStatus(-1);
    }

    public Position getFrameStart() {
        return frameStart;
    }

    public void setFrameStart(Position frameStart) {
        this.frameStart = frameStart;
    }

    public Position getFrameEnd() {
        return frameEnd;
    }

    public void setFrameEnd(Position frameEnd) {
        this.frameEnd = frameEnd;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public void setPositionValidator(PositionValidator positionValidator) {
        this.positionValidator = positionValidator;
    }
}
