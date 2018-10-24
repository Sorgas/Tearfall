package stonering.entity.local.crafting;

/**
 * Adds amount of material, returned on braking.
 *
 * @author Alexander on 24.10.2018.
 */
public class BrakeableComponentStep extends CommonComponentStep  {

    private float brakingRatio;
    private String brakingResult;

    public float getBrakingRatio() {
        return brakingRatio;
    }

    public void setBrakingRatio(float brakingRatio) {
        this.brakingRatio = brakingRatio;
    }

    public String getBrakingResult() {
        return brakingResult;
    }

    public void setBrakingResult(String brakingResult) {
        this.brakingResult = brakingResult;
    }
}
