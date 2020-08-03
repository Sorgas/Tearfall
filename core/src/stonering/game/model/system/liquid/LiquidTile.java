package stonering.game.model.system.liquid;

/**
 * @author Alexander on 17.06.2020.
 */
public class LiquidTile {
    public int liquid;
    public int amount;
    public boolean tpStable = false;
    public boolean stable = false;
    
    public LiquidTile(int liquid, int amount) {
        this.liquid = liquid;
        this.amount = amount;
    }
}
