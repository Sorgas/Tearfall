package stonering.generators.worldgen.generators.drainage;

import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.WorldMap;
import stonering.generators.worldgen.generators.AbstractGenerator;
import stonering.global.utils.Position;
import stonering.global.utils.Vector;

import java.util.*;

/**
 * Created by Alexander on 14.03.2017.
 *
 * Generates river.
 * For rivers additional elevation map (smoothed) is used. The map of slope vectors are based on it.
 * Rivers start from points close to mountains pikes, distributed with interspaces.
 * River has its own vector, which is modified by slope vectors on every passing cell.
 *
 */
public class RiverGenerator extends AbstractGenerator {
	private Random random;
	private WorldMap map;
	private int width;
	private int height;
	private int riverCount;
	private float[][] slopeInclination;
	private float[][] elevationBuffer;
	private List<Position> cells;

	public RiverGenerator(WorldGenContainer container) {
		super(container);
	}

	private void extractContainer(WorldGenContainer container) {
		random = container.getConfig().getRandom();
		width = container.getConfig().getWidth();
		height = container.getConfig().getHeight();
		riverCount = (int) (width * height * container.getLandPart() / container.getConfig().getRiverDensity());
		System.out.println(container.getLandPart() + "  " + riverCount);
		slopeInclination = new float[width][height];
		elevationBuffer = new float[width][height];
		cells = new ArrayList<>();
	}

	@Override
	public boolean execute() {
		extractContainer(container);
		System.out.println("generating rivers");
		map = container.getMap();
		smoothElevation(8);
		initSlopes();
//		countRiverStart();
//		for (Iterator<Position> iterator = cells.iterator(); iterator.hasNext(); ) {
//			Position riverStart = iterator.next();
//			runRiver(riverStart.getX(), riverStart.getY(), 200, 4);
//		}
		return false;
	}

	private void countRiverStart() {
		TreeSet<Position> sortedCells = new TreeSet<>(new ElevationComparator());
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				sortedCells.add(new Position(x, y, Math.round(elevationBuffer[x][y])));
			}
		}
		int count = 0;
		cells.clear();
		for (Iterator<Position> iterator = sortedCells.iterator(); count < riverCount && iterator.hasNext(); ) {
			Position riverStart = iterator.next();
			boolean rejected = false;
			for (Iterator<Position> cellsIterator = cells.iterator(); cellsIterator.hasNext(); ) {
				if (isNear(riverStart, cellsIterator.next(), 18)) {
					rejected = true;
					break;
				}
			}
			if (!rejected) {
				cells.add(riverStart);
				count++;
			}
		}
	}

	private void runRiver(int x, int y, int maxLength, int branchingDepth) {
		int i = 0;
		int seaLevel = container.getConfig().getSeaLevel() - 1;
		int savedAngle = 0;
		if(!inMap(x,y)) return;
		Vector riverVector = new Vector(x, y, container.getSlopeAngles(x,y), 2.0f);
		int turningCounter = 0;
		while (i < maxLength && container.getElevation(x, y) > seaLevel && inMap(x, y)) {

			float curElevation = elevationBuffer[x][y]; // getting elevation in current point

			if (turningCounter == 0) {  // starting river turn
				turningCounter = random.nextInt(14);
			}

			Vector slopeVector = new Vector(0, 0, container.getSlopeAngles(x,y), 1); // getting slope vector
			riverVector = riverVector.sum(slopeVector); // applying slope to river
			riverVector.setLength(riverVector.getLength() / 2); // decreasing river speed
			if (turningCounter != 0) { // turning river
				int mod = Math.round(Math.copySign(1, turningCounter - 7));
				riverVector.setAngle((riverVector.getAngle() + 15 * mod + 360) % 360);
				turningCounter--;
			}
			// converting river angle to x45, and saving difference
			riverVector.setAngle((riverVector.getAngle() + savedAngle + 360) % 360);
			int targetAngle = ((int) ((riverVector.getAngle() + 22.5f + 360) % 360) / 45);
			targetAngle *= 45;
			savedAngle = (int) ((riverVector.getAngle() - targetAngle + 360) % 360);

			//branching river
			if (i > 6 && branchingDepth > 0) {
				if (random.nextInt(100) < 15) {
					targetAngle = (targetAngle + 45) % 360;
					savedAngle = 0;
					int branchAngle = (targetAngle - 90 + 360) % 360;
					int bx = x + getXProject(branchAngle);
					int by = y + getYProject(branchAngle);
					riverVector.setLength(2);
					branchingDepth--;
					runRiver(bx, by, maxLength, branchingDepth);
				}
			}

			riverVector.setAngle(targetAngle);
			map.addRiverVector(new Vector(x,y, x + getXProject(targetAngle),y + getYProject(targetAngle)));  // set river in current point
			x += getXProject(targetAngle); // getting next river point
			y += getYProject(targetAngle);
			if (!inMap(x, y) || map.getRivers().containsKey(new Position(x,y,0)) || (elevationBuffer[x][y] - curElevation > 0.3f)) {
				break;
			}
			i++;
		}
	}

	private boolean isNear(Position pos1, Position pos2, float limit) {
		double distance = countDistance(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY());
		return distance < limit;
	}

	private int getXProject(float angle) {
		if ((angle < 62.5) || (angle > 292.5)) return 1;
		if ((angle > 112.5) && (angle < 247.5)) return -1;
		return 0;
	}

	private int getYProject(float angle) {
		if (angle > 22.5 && angle < 137.5) return 1;
		if (angle > 202.5 && angle < 337.5) return -1;
		return 0;
	}

	private boolean inMap(int x, int y) {
		if (x < 0 || x >= width) return false;
		if (y < 0 || y >= height) return false;
		return true;
	}

	private void initSlopes() {
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				countSlopeCharacteristics(x, y);
	}

	private void countSlopeCharacteristics(int x, int y) {
		int minX = x - 1;
		int maxX = x + 2;
		int minY = y - 1;
		int maxY = y + 2;
		if (minX < 0) minX = 0;
		if (maxX > width) maxX = width;
		if (minY < 0) minY = 0;
		if (maxY > height) maxY = height;
		float centerElevation = elevationBuffer[x][y];
		float minElevation = centerElevation;
		float xProject = 0;
		float yProject = 0;
		for (int i = minX; i < maxX; i++) {
			for (int j = minY; j < maxY; j++) {
				if (elevationBuffer[i][j] < minElevation) {
					minElevation = elevationBuffer[i][j];
					xProject = (i - x) * (centerElevation - elevationBuffer[i][j]);
					yProject = (j - y) * (centerElevation - elevationBuffer[i][j]);
				}
			}
		}

		double angle;
		if (xProject != 0) {
			angle = (Math.toDegrees(Math.atan(yProject / xProject)) + 360) % 360;
			if (xProject < 0) {
				angle = (angle + 180) % 360;
			}
		} else {
			angle = yProject > 0 ? 90 : 270;
		}
		container.setSlopeAngles(x, y, Math.round((float) angle));
		slopeInclination[x][y] = countDistance(0, 0, xProject, yProject);
	}

	private float countDistance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	private void smoothElevation(int iterations) {
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				elevationBuffer[x][y] = container.getElevation(x, y);
		float[][] innerElevationBuffer = new float[width][height];
		for (int i = 0; i < iterations; i++) {
			for (int x = 0; x < width; x++)
				for (int y = 0; y < height; y++)
					innerElevationBuffer[x][y] = countMiddleElevation(x, y, 3);
			for (int x = 0; x < width; x++)
				for (int y = 0; y < height; y++)
					elevationBuffer[x][y] = innerElevationBuffer[x][y];
		}
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				container.setDebug(x, y, elevationBuffer[x][y]);
	}

	private float countMiddleElevation(int x, int y, int radius) {
		int minX = x - radius;
		int maxX = x + radius + 1;
		int minY = y - radius;
		int maxY = y + radius + 1;
		float sum = 0;
		if (minX < 0) minX = 0;
		if (maxX > width) maxX = width;
		if (minY < 0) minY = 0;
		if (maxY > height) maxY = height;
		for (int i = minX; i < maxX; i++) {
			for (int j = minY; j < maxY; j++) {
				sum += elevationBuffer[i][j];
			}
		}
		return sum / ((maxX - minX) * (maxY - minY));
	}

	private class ElevationComparator implements Comparator<Position> {
		@Override
		public int compare(Position o1, Position o2) {
			int value = 0;
			if (o1 != null && o2 != null) {
				value = o2.getZ() - o1.getZ();
				if (value == 0) {
					value = 1;
				}
			}
			return value;
		}
	}
}