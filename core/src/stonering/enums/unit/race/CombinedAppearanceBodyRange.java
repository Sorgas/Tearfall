package stonering.enums.unit.race;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Alexander on 22.09.2020.
 */
public class CombinedAppearanceBodyRange extends CombinedAppearanceRange {
    public final List<Integer> width = new ArrayList<>();
    
    public CombinedAppearanceBodyRange(String param) {
        super(param);
        String[] params = param.split("/");
        Arrays.stream(params[3].split(":"))
                .map(Integer::parseInt)
                .forEach(width::add);
    }
}
