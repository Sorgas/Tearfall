package stonering.stage.entity_menu.zone;

import java.util.Optional;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import stonering.enums.plants.PlantTypeMap;

/**
 * @author Alexander on 14.07.2020.
 */
public class FarmPlantDetailsTab extends Table {
    private Image image;
    private Label title;
    private Label description;
    
    public FarmPlantDetailsTab() {
        
    }
    
    public void setFor(String plant) {
        Optional.ofNullable(PlantTypeMap.getPlantType(plant))
                .ifPresent(type -> {
//                    image.setDrawable(AtlasesEnum.plants.getBlockTile(type.lifeStages));
                    // picture
                    // title
                    // products
                    // description

                });
    }
}
