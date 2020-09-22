package stonering.enums.unit.race;

/**
 * Description of humanoid creature body.
 * 
 * @author Alexander on 17.08.2020.
 */
public class CombinedAppearance {
    public String body;
    public String head;
    public String foot;
    public int bodyHeight;
    public CombinedAppearanceBodyRange bodyRange;
    public CombinedAppearanceRange headRange;
    public CombinedAppearanceRange footRange;
    
    public void process() {
        bodyRange = new CombinedAppearanceBodyRange(body);
        headRange = new CombinedAppearanceRange(head);
        footRange = new CombinedAppearanceRange(foot);
    }
}
