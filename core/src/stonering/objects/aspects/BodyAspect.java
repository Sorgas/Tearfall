package stonering.objects.aspects;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import stonering.objects.creatures.BodyPart;

import java.util.ArrayList;

/**
 * Created by Alexander on 19.10.2017.
 */
public class BodyAspect extends Aspect {
    private ArrayList<BodyPart> bodyParts;

    public BodyAspect() {
        bodyParts = new ArrayList<BodyPart>();
    }

    public void addBodyPart(BodyPart bodyPart) {
        bodyParts.add(bodyPart);
    }

}
