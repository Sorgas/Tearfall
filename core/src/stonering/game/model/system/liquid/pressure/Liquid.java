package stonering.game.model.system.liquid.pressure;

import java.util.Arrays;

/**
 * @author Alexander on 03.08.2020.
 */
public class Liquid {
    // Max and min cell liquid values
    int MaxValue = 7;

    // Extra liquid a cell can store than the cell above it
    float MaxCompression = 0.25f;

    // Lowest and highest amount of liquids allowed to flow per iteration
    int MinFlow = 1;
    int MaxFlow = 4;

    // Adjusts flow speed (0.0f - 1.0f)
    float FlowSpeed = 1f;

    // Keep track of modifications to cell liquid values
    float[][] diffs;

    public void initialize(Cell[][] cells) {
        diffs = new float[cells.length][cells[0].length];
    }

    // Run one simulation step
    public void simulate(Cell[][] cells, int width, int height) {
        float flow;
        
        // Reset the diffs array
        for (int x = 0; x < width; x++) {
            Arrays.fill(diffs[x], 0);
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Get reference to Cell and reset flow
                final Cell cell = cells[x][y];
                Arrays.fill(cell.flowDirections, false);

                // Validate cell
                if (cell.type == CellType.SOLID || cell.liquid < 0) {
                    cell.liquid = 0;
                    continue;
                }
                if (cell.liquid == 0 || cell.isSettled()) continue;

                // Keep track of how much liquid this cell started off with
                float startValue = cell.liquid;
                float remainingValue = cell.liquid;

                // Flow to bottom cell
                if (cell.bottom != null && cell.bottom.type == CellType.BLANK) {
                    // Determine rate of flow
                    flow = calculateVerticalFlowValue(cell.liquid, cell.bottom) - cell.bottom.liquid;
                    if (cell.bottom.liquid > 0 && flow > MinFlow)
                        flow *= FlowSpeed;

                    // Constrain flow
                    flow = Math.max(flow, 0);
                    if (flow > Math.min(MaxFlow, cell.liquid)) flow = Math.min(MaxFlow, cell.liquid);

                    // Update temp values
                    if (flow != 0) {
                        remainingValue -= flow;
                        diffs[x][y] -= flow;
                        diffs[x][y + 1] += flow;
                        cell.flowDirections[FlowDirection.BOTTOM.id] = true;
                        cell.bottom.setSettled(false);
                    }
                }
                // Check to ensure we still have liquid in this cell
                if (remainingValue < 0) {
                    diffs[x][y] -= remainingValue;
                    continue;
                }

                // Flow to left cell
                if (cell.left != null && cell.left.type == CellType.BLANK) {

                    // Calculate flow rate
                    flow = (remainingValue - cell.left.liquid) / 4f;
                    if (flow > MinFlow)
                        flow *= FlowSpeed;

                    // constrain flow
                    flow = Math.max(flow, 0);
                    if (flow > Math.min(MaxFlow, remainingValue))
                        flow = Math.min(MaxFlow, remainingValue);

                    // Adjust temp values
                    if (flow != 0) {
                        remainingValue -= flow;
                        diffs[x][y] -= flow;
                        diffs[x - 1][y] += flow;
                        cell.flowDirections[(int) FlowDirection.LEFT.id] = true;
                        cell.left.setSettled(false);
                    }
                }

                // Check to ensure we still have liquid in this cell
                if (remainingValue < 0) {
                    diffs[x][y] -= remainingValue;
                    continue;
                }

                // Flow to right cell
                if (cell.right != null && cell.right.type == CellType.BLANK) {
                    // calc flow rate
                    flow = (remainingValue - cell.right.liquid) / 3f;
                    if (flow > MinFlow)
                        flow *= FlowSpeed;

                    // constrain flow
                    flow = Math.max(flow, 0);
                    if (flow > Math.min(MaxFlow, remainingValue))
                        flow = Math.min(MaxFlow, remainingValue);

                    // Adjust temp values
                    if (flow != 0) {
                        remainingValue -= flow;
                        diffs[x][y] -= flow;
                        diffs[x + 1][y] += flow;
                        cell.flowDirections[(int) FlowDirection.RIGHT.id] = true;
                        cell.right.setSettled(false);
                    }

                }

                // Check to ensure we still have liquid in this cell
                if (remainingValue < 0) {
                    diffs[x][y] -= remainingValue;
                    continue;
                }

                // Flow to Top cell
                if (cell.top != null && cell.top.type == CellType.BLANK) {

                    flow = remainingValue - calculateVerticalFlowValue(remainingValue, cell.top);
                    if (flow > MinFlow)
                        flow *= FlowSpeed;

                    // constrain flow
                    flow = Math.max(flow, 0);
                    if (flow > Math.min(MaxFlow, remainingValue))
                        flow = Math.min(MaxFlow, remainingValue);

                    // Adjust values
                    if (flow != 0) {
                        remainingValue -= flow;
                        diffs[x][y] -= flow;
                        diffs[x][y - 1] += flow;
                        cell.flowDirections[(int) FlowDirection.TOP.id] = true;
                        cell.top.setSettled(false);
                    }
                }

                // Check to ensure we still have liquid in this cell
                if (remainingValue < 0) {
                    diffs[x][y] -= remainingValue;
                    continue;
                }

                // Check if cell is settled
                if (startValue == remainingValue) {
                    cell.settleCount++;
                    if (cell.settleCount >= 10) {
                        Arrays.fill(cell.flowDirections, false);
                        cell.setSettled(true);
                    }
                } else {
                    cell.unsettleNeighbors();
                }
            }
        }

        // Update Cell values

        flushDiffs(cells, width, height);
    }

    private void flushDiffs(Cell[][] cells, int width, int height) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y].liquid += diffs[x][y];
                if (cells[x][y].liquid < 0) {
                    cells[x][y].liquid = 0;
                    cells[x][y].setSettled(false); // default empty cell to
                    // unsettled
                }
            }
        }
    }
    
    // Calculate how much liquid should flow to destination with pressure
    private float calculateVerticalFlowValue(float remainingLiquid, Cell destination) {
        float sum = remainingLiquid + destination.liquid;
        float value = 0;

        if (sum <= MaxValue) {
            value = MaxValue;
        } else if (sum < 2 * MaxValue + MaxCompression) {
            value = (MaxValue * MaxValue + sum * MaxCompression) / (MaxValue + MaxCompression);
        } else {
            value = (sum + MaxCompression) / 2f;
        }

        return value;
    }
}
