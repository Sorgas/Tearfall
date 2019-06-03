package stonering.game.view;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * {@link OrthographicCamera} extension which have goal posiiton and speed.
 * On update, camera moves to it's goal.
 *
 * @author Alexander_Kuzyakov on 03.06.2019.
 */
public class MovableCamera extends OrthographicCamera {
    private int goalX;
    private int goalY;
    private int speed;
    private boolean

    @Override
    public void update() {
        super.update();

    }

    public int getGoalX() {
        return goalX;
    }

    public void setGoalX(int goalX) {
        this.goalX = goalX;
    }

    public int getGoalY() {
        return goalY;
    }

    public void setGoalY(int goalY) {
        this.goalY = goalY;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
