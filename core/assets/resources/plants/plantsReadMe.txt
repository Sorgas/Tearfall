temperatureBounds:
    1,2: temperature for plant to occur during generation,
    3,4: max/min temperature to grow in realtime.
placingTags:     // determines special requirements of this plant to environment
    //TODO distinct running and stagnant water
    water_near   // 1-2 tiles to water source.
    water_far    // farer than 1-2 tiles.         default
    water_on     // surface of full water tiles.
    water_under  // bottom of water pool.

    light_underground  // only tiles in caves
    light_low          // low light like in the tree shadow
    light_high         // direct acces to light
    light_open         // low or high                   default

    soil_soil          // grows on soil                default
    soil_stone         // grows on stones (mosses)
    soil_wood          // grows on wooden floors

treeType:
    crownRadius, height, rootDepth, rootRadius for trees
lifeStages:
    for single tiled plants sprite is taken with a number of life stage as x offset.
    stages with length, products, material.
harvestProduct:
    item title // all item products are plant_product items.
    number formula (per block), // format: [min]-[max]/[divider] roll [min, max] will be lowered by divider and rounded up
    months // numbers of months (0,11) or 'all' keyword when product can be got.
    tags // list of tags to be copied to item.
    aspects // list of aspects and parameters
    // for trees, products got only from crone.
productTags: tags of products
seedItem : // points to basic item

default values are set in PlantMap
