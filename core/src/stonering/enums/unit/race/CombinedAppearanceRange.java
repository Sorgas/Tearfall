package stonering.enums.unit.race;

/**
 * @author Alexander on 22.09.2020.
 */
public class CombinedAppearanceRange {
    public int y;
    public int maleVariants;
    public int femaleVariants;

    public CombinedAppearanceRange(String param) {
        String[] params = param.split("/");
        y = Integer.parseInt(params[0]);
        maleVariants = Integer.parseInt(params[1]);
        femaleVariants = Integer.parseInt(params[2]);
    }
}
