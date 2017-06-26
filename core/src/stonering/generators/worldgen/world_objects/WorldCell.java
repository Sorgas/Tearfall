package stonering.generators.worldgen.world_objects;


import stonering.utils.Position;

import java.io.Serializable;

public class WorldCell implements Serializable {
    private Position position;
    private float elevation;
	private float temperature;
    private boolean isOcean;
	private boolean isRiver;


    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setPosition(int x, int y, int z) {
        position.setX(x);
        position.setY(y);
        position.setZ(z);
    }

    public float getElevation() {
        return elevation;
    }

    public void setElevation(float elevation) {
    	this.elevation = elevation;
    }

	public boolean isOcean() {
		return isOcean;
	}

	public void setOcean(boolean ocean) {
		isOcean = ocean;
	}

	public boolean isRiver() {
		return isRiver;
	}

	public void setRiver(boolean river) {
		isRiver = river;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
}
