package stonering.game.model.system.substrate;

import stonering.entity.plant.SubstratePlant;
import stonering.game.model.system.EntitySystem;

/**
 * System for growing substrates. Substrates, like grass, grow in spring, change their color in autumn and die in winter.
 * Subterranean substrates do not depend on seasons above.
 * 
 * @author Alexander on 27.02.2020.
 */
public class SubstrateGrowingSystem extends EntitySystem<SubstratePlant> {
    private SubstrateContainer container;
    
    public SubstrateGrowingSystem(SubstrateContainer container) {
        this.container = container;
    }

    @Override
    public void update(SubstratePlant entity) {
        
    }
}
