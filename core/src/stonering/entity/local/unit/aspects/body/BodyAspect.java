package stonering.entity.local.unit.aspects.body;

import stonering.entity.local.Aspect;
import stonering.entity.local.unit.Unit;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Holds creature's body parts and connections between them.
 *
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class BodyAspect extends Aspect {
    public static String NAME = "body";
    private String bodyType;
    private HashMap<String, BodyPart> bodyParts; // name to bodyPart
    private ArrayList<String> bodyPartsToCover;

    public BodyAspect(Unit unit) {
        super(unit);
        bodyParts = new HashMap<>();
        bodyPartsToCover = new ArrayList<>();
    }

    public class Organ {
        public String name;
    }

    public HashMap<String, BodyPart> getBodyParts() {
        return bodyParts;
    }

    public void setBodyParts(HashMap<String, BodyPart> bodyParts) {
        this.bodyParts = bodyParts;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public void setBodyPartsToCover(ArrayList<String> bodyPartsToCover) {
        this.bodyPartsToCover = bodyPartsToCover;
    }

    public ArrayList<String> getBodyPartsToCover() {
        return new ArrayList<>();
    }

    public void addBodyPart(BodyPart bodyPart) {
        bodyParts.put(bodyPart.title, bodyPart);
    }
}
