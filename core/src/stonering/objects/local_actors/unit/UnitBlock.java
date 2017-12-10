package stonering.objects.local_actors.unit;

/**
 * Created by Alexander on 09.12.2017.
 *
 * Proxy for Unit
 */
public class UnitBlock {
    private Unit unit;

    public UnitBlock(Unit unit) {
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}