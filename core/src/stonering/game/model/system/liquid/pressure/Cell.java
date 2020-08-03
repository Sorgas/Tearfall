package stonering.game.model.system.liquid.pressure;

import com.badlogic.gdx.graphics.Color;

/**
 * @author Alexander on 03.08.2020.
 */
public class Cell {
    public float liquid;
    private boolean settled;
    public int settleCount;

    // Neighboring cells
    public Cell top;
    public Cell bottom;
    public Cell left;
    public Cell right;

    public static final Color LIGHT_COLOR = new Color(0.6f, 0.8f, 1f, 1f);
    public static final Color DARK_COLOR = new Color(0, 0.5f, 0.9f, 1f);

    private static final boolean SHOW_FLOW = false;
    private static final boolean RENDER_FLOATING_LIQUID = true;
    private static final boolean RENDER_DOWN_FLOWING_LIQUID = true;
    
    public CellType type = CellType.BLANK;

    // Shows flow direction of cell
    public int bitmask;
    public final boolean[] flowDirections = new boolean[4];

    public float size;

    public boolean isSettled() {
        return settled;
    }

    public void setSettled(boolean settled) {
        this.settled = settled;
        if(!settled) settleCount = 0;
    }


    public void setType(CellType type) {
        this.type = type;
        if (type == CellType.SOLID) {
            liquid = 0;
        }
        unsettleNeighbors();
    }


    public void addLiquid(float amount) {
        liquid += amount;
        setSettled(false);
    }

    public void unsettleNeighbors() {
        if (top != null)
            top.setSettled(false);
        if (bottom != null)
            bottom.setSettled(false);
        if (left != null)
            left.setSettled(false);
        if (right != null)
            right.setSettled(false);
    }

    public void update() {
        // Update bitmask based on flow directions
        bitmask = 0;
        if (flowDirections [FlowDirection.TOP.id]) bitmask += 1;
        if (flowDirections [FlowDirection.RIGHT.id]) bitmask += 2;
        if (flowDirections [FlowDirection.BOTTOM.id]) bitmask += 4;
        if (flowDirections [FlowDirection.LEFT.id]) bitmask += 8;

        if (SHOW_FLOW) {
            // Show flow direction of this cell
            //FlowSprite.sprite = FlowSprites [Bitmask];
        } else {
            //FlowSprite.sprite = FlowSprites [0];
        }

        // Set size of Liquid sprite based on liquid value
        size = Math.min(1, liquid);

        // Optional rendering flags
        if (!RENDER_FLOATING_LIQUID) {
            // Remove "Floating" liquids
            if (bottom != null && bottom.type != CellType.SOLID && bottom.liquid <= 0.99f) {
                size = 0;
            }
        }
        if (RENDER_DOWN_FLOWING_LIQUID) {
            // Fill out cell if cell above it has liquid
            if (type == CellType.BLANK && top != null && (top.liquid > 0.05f || top.bitmask == 4)) {
                size = 1;
            }
        }
    }
}
