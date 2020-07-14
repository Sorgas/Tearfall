package stonering.game.model;

import java.util.ArrayList;
import java.util.List;

import stonering.game.model.system.ModelComponent;

/**
 * Component for storing player's settlement properties, technologies, diplomacy, etc. 
 * 
 * @author Alexander on 14.07.2020.
 */
public class PlayerSettlementProperties implements ModelComponent {
    public final List<String> availablePlants = new ArrayList<>(); // plant, available for player to plant on farms
    
}
