package stonering.enums.unit.body.raw;

import stonering.enums.unit.body.BodyTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Raw object for {@link BodyTemplate}. Loaded from json and then processed into {@link BodyTemplate}.
 *
 * @author Alexander_Kuzyakov on 02.07.2019.
 */
public class RawBodyTemplate {
    public String name;
    public List<String> needs;
    public List<String> desiredSlots = new ArrayList<>();
    public List<RawBodyPart> body;
    public List<List<String>> slots = new ArrayList<>();
}
