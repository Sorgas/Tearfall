package stonering.enums.unit.raw;

import stonering.enums.unit.BodyTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Raw object for {@link BodyTemplate}. Loaded from json and then processed into {@link BodyTemplate}.
 *
 * @author Alexander_Kuzyakov on 02.07.2019.
 */
public class RawBodyTemplate {
    public String name;
    public List<String> defaultLayers;
    public List<String> needs;
    public List<String> limbsToCover = new ArrayList<>(); // array can be omitted in json
    public List<RawBodyPart> body;
}
