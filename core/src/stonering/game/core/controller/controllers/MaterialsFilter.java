package stonering.game.core.controller.controllers;

import stonering.enums.buildings.BuildingType;
import stonering.enums.buildings.BuildingMap;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.GameMvc;
import stonering.game.core.model.GameContainer;

import java.util.HashMap;
/**
 * @author Alexander Kuzyakov
 * created on 28.06.2018
 */
public class MaterialsFilter extends Controller {
    private GameContainer gameContainer;

    public MaterialsFilter(GameMvc gameMvc) {
        super(gameMvc);
//        listsMap = ListsMap.getInstance();
    }

    @Override
    public void init() {
        gameContainer = gameMvc.getModel();
    }

    public HashMap<String, Integer> getAvailableMaterialsForBuilding(String buildingTitle) {
        BuildingType buildingType = BuildingMap.getInstance().getBuilding(buildingTitle);
        MaterialMap materialMap = MaterialMap.getInstance();
        if (buildingType != null) {
            HashMap<String, Integer> map = new HashMap<>();
            gameContainer.getItemContainer().getMaterialList(buildingType.getAmount(), buildingType.getItems(), buildingType.getMaterials()).forEach((item) -> {
                String key = materialMap.getMaterial(item.getMaterial()).getName() + item.getTitle();
                int prev = map.containsKey(item.getTitle()) ? map.get(item.getTitle()) : 0;
                map.put(item.getTitle(), prev + 1);
            });
            return map;
        }
        return new HashMap<>();
    }
}
