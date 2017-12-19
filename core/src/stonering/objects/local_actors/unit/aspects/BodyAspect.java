package stonering.objects.local_actors.unit.aspects;

import stonering.game.core.model.GameContainer;
import stonering.objects.local_actors.unit.BodyPart;

import java.util.ArrayList;

/**
 * Created by Alexander on 19.10.2017.
 *
 * Holds creature's body parts and connections between them.
 */
public class BodyAspect extends Aspect {
    private ArrayList<BodyPart> bodyParts;
    private ArrayList<Integer> limbs;

    public BodyAspect() {
        bodyParts = new ArrayList<BodyPart>();

    }

    public void addBodyPart(BodyPart bodyPart) {
        bodyParts.add(bodyPart);
    }

    public ArrayList<BodyPart> getBodyParts() {
        return bodyParts;
    }

    public void setBodyParts(ArrayList<BodyPart> bodyParts) {
        this.bodyParts = bodyParts;
    }

    public ArrayList<Integer> getLimbs() {
        return limbs;
    }

    public void setLimbs(ArrayList<Integer> limbs) {
        this.limbs = limbs;
    }

    @Override
    public void init(GameContainer gameContainer) {

    }
}
