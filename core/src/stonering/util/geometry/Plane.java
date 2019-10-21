package stonering.util.geometry;

/**
 * @author Alexander Kuzyakov on 03.03.2017.
 *
 * Utility class for plane.
 * Defined by 3 points of space, represented by coefficients of plane function.
 */
public class Plane {
	private float xMod;
	private float yMod;
	private float zMod;
	private float d;

	public Plane(Position pos1, Position pos2, Position pos3) {
		xMod = (pos2.y - pos1.y) * (pos3.z - pos1.z) - (pos2.z - pos1.z) * (pos3.y - pos1.y);
		yMod = -(pos2.x - pos1.x) * (pos3.z - pos1.z) + (pos2.z - pos1.z) * (pos3.x - pos1.x);
		zMod = (pos2.x - pos1.x) * (pos3.y - pos1.y) - (pos2.y - pos1.y) * (pos3.x - pos1.x);
		d = -(yMod * pos1.y + xMod * pos1.x + zMod * pos1.z);
	}

	public float getZ(float x, float y) {
		return -(d + (xMod * x) + (yMod * y)) / zMod;
	}

	public float getX(float y, float z) {
		return -(d + (zMod * z) + (yMod * y)) / xMod;
	}

	public float getY(float x, float z) {
		return -(d + (xMod * x) + (zMod * z)) / yMod;
	}
}
