/**
 * Package contains actions for working with unit's equipment.
 * 
 * All items transitions in/out unit's equipment slots are passing through unit's grab slots. 
 * E.g when unit equips wear item, it is first taken in hands, and then moved to appropriate wear slot.
 * This is done for more decomposed handling different item sources for unit (on the ground, container, other unit),
 * and item destinations on unit (tool item for hands, wear items for limb slots, other items for worn containers).
 * 
 * Same is done for unequipping items.
 */
package stonering.entity.job.action.equipment;